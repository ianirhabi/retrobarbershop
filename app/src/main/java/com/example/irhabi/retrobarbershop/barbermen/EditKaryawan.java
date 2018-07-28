package com.example.irhabi.retrobarbershop.barbermen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.Upload;
import com.example.irhabi.retrobarbershop.model.User;
import com.example.irhabi.retrobarbershop.model.Usr;
import com.example.irhabi.retrobarbershop.newmasuklogin.Setting;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.irhabi.retrobarbershop.rest.AppConfig.URL;

public class EditKaryawan extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST = 100;
    private int PICK_IMAGE_FROM_GALERY_REQUEST = 1;
    public static final int PICK_IMAGE = 100;
    private Button upld, tk, simpan;
    private EditText ld, passwordlama, passwordbaru, passwordbarulagi;
    Router service;
    private ImageView userfoto;
    private SessionManager sesi;
    private String id;
    private TextView username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_karyawan);
        Bundle b = getIntent().getExtras();

        final int get_id = b.getInt("parse_id");
        final String get_foto = b.getString("parse_foto");

        if (ContextCompat.checkSelfPermission(EditKaryawan.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditKaryawan.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST);
        }
        userfoto = (ImageView) findViewById(R.id.fotokaryawan);
        tk = (Button) findViewById(R.id.uploadfoto);
        username=(TextView)findViewById(R.id.usernamekaryawan);
        password=(TextView)findViewById(R.id.passwordkaryawan) ;
        passwordlama=(EditText)findViewById(R.id.Passwordlamakaryawan);
        passwordbaru=(EditText)findViewById(R.id.Passwordbarukaryawan);
        passwordbarulagi=(EditText)findViewById(R.id.Passwordbarulagikaryawan);
        simpan = (Button)findViewById(R.id.simpankaryawan);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String lama = password.getText().toString();
                final String baru = passwordbaru.getText().toString();
                final String barulagi = passwordbarulagi.getText().toString();

                if(lama.equals(baru)){
                    Toast.makeText(getApplicationContext(), "Password lama tidak boleh sama dengan baru" , Toast.LENGTH_SHORT).show();
                }else if(baru.equals(barulagi)) {
                    User user = new User(lama,baru);
                    updatekaryawan(user, get_id);
                }else{
                    Toast.makeText(getApplicationContext(), "Password baru tidak sama " , Toast.LENGTH_SHORT).show();
                }
            }
        });

        Glide
                .with(EditKaryawan.this)
                .load(URL + "upload/" + get_foto)
                .into(userfoto);

        tk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_FROM_GALERY_REQUEST);
            }
        });

        Router service = RetrofitInstance.getRetrofitInstance().create(Router.class);

        Call<Usr> call = service.retro(get_id);
        call.enqueue(new Callback<Usr>() {

            @Override
            public void onResponse(Call<Usr> call, Response<Usr> response) {
                username.setText(response.body().getname());
                password.setText(response.body().getPass());
            }

            @Override
            public void onFailure(Call<Usr> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Gagal Mengambil Data Dari Server Pastikan Anda Terhubung Dengan Internet " , Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void updatekaryawan(User user, int id){
        Router service = RetrofitInstance.getRetrofitInstance().create(Router.class);
        String iduser = String.valueOf(id);
        Call<User> call = service.Updateuser(user,iduser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getStatus().equals("sukses")){
                    Toast.makeText(getApplicationContext(),"Berhasil Update data", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Gagal Update data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Gagal Mengambil Data Dari Server Pastikan Anda Terhubung Dengan Internet " , Toast.LENGTH_SHORT).show();
            }
        });
    }




    // Izin Akses Camera
    public void onRequestPermissionsResult(int requestCode,
                                           String permission[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do code
                } else {
                    // write code now
                }
            }
        }
    }


    //untuk mengambil gambar hasil capture camera tadi kita harus override onActivityResult dan membaca resultCode apakah sukses dan requestCode apakah dari Camera_Request
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            //panggil method uploadImage dengan mengambil gambar langsung dengan kamera
            //uploadImage(mphoto);
        }else if(requestCode == PICK_IMAGE_FROM_GALERY_REQUEST && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Bitmap mphoto = null;
            try {
                mphoto = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                uploadFile(mphoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            /// Toast.makeText(Setting.this, "uri nya " + uri, Toast.LENGTH_SHORT).show();

        }
    }

    /*
        TODO mengconvert Bitmap menjadi file dikarenakan retrofit hanya mengenali tipe file untuk upload gambarnya sekaligus mengcompressnya menjadi WEBP dikarenakan size bisa sangat kecil dan kualitasnya pun setara dengan PNG.
    */
    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() + ".jpg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bitmap = BitmapFactory.decodeStream(getAssets().open("1024x768.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }





    private void uploadFile(Bitmap gambarbitmap){

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("cycle_time_begin", createPartFromString("2017-07-19"));
        map.put("fish_species", createPartFromString("LELE SUPER"));
        map.put("crop_species", createPartFromString("BAYAM MERAH"));
        map.put("id", createPartFromString("323278djsadkhjye2"));

        File file = createTempFile(gambarbitmap);

        RequestBody filePart =RequestBody.create(MediaType.parse("image/*"), file);


        MultipartBody.Part body = MultipartBody.Part.createFormData("cycle", file.getName(), filePart);
        MultipartBody.Part body2 = MultipartBody.Part.createFormData("diran", "andrian", filePart);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(Router.class);

        String a = file.getName();
        // ld = (EditText)findViewById(R.id.lod);
        String[] kf = a.split("\\.");
        final String first = kf[0];

        Log.d("DEBUG ", "BARU activity seting " + first);
        Glide
                .with(EditKaryawan.this)
                .load(URL + "upload/" + first)
                .into(userfoto);

        sesi = new SessionManager(getApplicationContext());
        sesi.createImage(first);

        Bundle b = getIntent().getExtras();
        final int Barberid= b.getInt("parse_id");
        Upload upload = new Upload(a, Barberid);

        Call<Upload> call2 = service.uploadnameImage(upload);

        // finally, kirim map dan body pada param interface retrofit
        Call<ResponseBody> call = service.uploadImage(body,body2, map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditKaryawan.this, "Gagal Upload Foto", Toast.LENGTH_SHORT).show();
            }
        });

        //post file name
        call2.enqueue(new Callback<Upload>() {
            @Override
            public void onResponse(Call<Upload> call, Response<Upload> response) {
                Toast.makeText(EditKaryawan.this, response.body().getRespon(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Upload> call, Throwable t) {
                Toast.makeText(EditKaryawan.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
