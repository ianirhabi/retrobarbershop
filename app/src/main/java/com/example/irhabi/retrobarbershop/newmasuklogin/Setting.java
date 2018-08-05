package com.example.irhabi.retrobarbershop.newmasuklogin;

/**
 * Created BY Programmer Jalan on January 2018
 */

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.irhabi.retrobarbershop.Maps.KonekMaps;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.alert.ViewDialog;
import com.example.irhabi.retrobarbershop.barbermen.HapusKaryawan;
import com.example.irhabi.retrobarbershop.model.Upload;
import com.example.irhabi.retrobarbershop.model.User;
import com.example.irhabi.retrobarbershop.model.Usr;
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

public class Setting extends AppCompatActivity {

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
    private RetrofitInstance retro;
    private ViewDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);

        if (ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Setting.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST);
        }
        userfoto = (ImageView) findViewById(R.id.lod);
        upld = (Button) findViewById(R.id.upload);
        tk = (Button) findViewById(R.id.TAKE);
        username=(TextView)findViewById(R.id.username);
        password=(TextView)findViewById(R.id.password) ;
        passwordlama=(EditText)findViewById(R.id.Passwordlama);
        passwordbaru=(EditText)findViewById(R.id.Passwordbaru);
        passwordbarulagi=(EditText)findViewById(R.id.Passwordbarulagi);
        simpan = (Button)findViewById(R.id.simpan);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String lama = passwordlama.getText().toString();
                final String baru = passwordbaru.getText().toString();
                final String barulagi = passwordbarulagi.getText().toString();

                if (TextUtils.isEmpty(lama)) {
                    passwordlama.setError("password lama tidak boleh kosong");
                    return;
                }else if (TextUtils.isEmpty(baru)) {
                    passwordbaru.setError("password baru tidak boleh kosong");
                    return;
                }else if (TextUtils.isEmpty(barulagi)) {
                    passwordbarulagi.setError("tidak boleh kosong");
                    return;
                }else if (barulagi.length() < 6) {
                   passwordbaru.setError("password baru harus lebih dari 6 karakter");
                }else if(lama.equals(baru)){
                    alert.showDialog(Setting.this, "Password lama tidak boleh sama dengan baru!!", Setting.this);
                }else if(baru.equals(barulagi)) {
                    dialog(lama, baru);

                }else{
                    alert = new ViewDialog();
                    alert.showDialog(Setting.this, "Password Baru Tidak Sama Mohon Ulangi !!", Setting.this);
                }
            }
        });

        sesi = new SessionManager(getApplicationContext());
        HashMap<String, String> usersesion = sesi.getUserDetails();
        String image = usersesion.get(SessionManager.KEY_IMAGE);
        Log.d("DEBUG ", "BARU activity setting " + image);
        Glide
                .with(Setting.this)
                .load(URL + "getimage/" + image)
                .into(userfoto);

        upld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImageFromCamera();
            }
        });

        tk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_FROM_GALERY_REQUEST);
            }
        });

        sesi = new SessionManager(getApplicationContext());
        id = usersesion.get(SessionManager.KEY_ID);
        String token = usersesion.get(SessionManager.TOKEN);
        Log.d("debug token ", " : " + token);
        retro = new RetrofitInstance(token);
        Router service = retro.getRetrofitInstanceall().create(Router.class);
        int idDetail = Integer.parseInt(id);
        Call<Usr> call = service.retro(idDetail);
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
    public void update(User user) {
        sesi = new SessionManager(getApplicationContext());
        final HashMap<String, String> usersesion = sesi.getUserDetails();
        id = usersesion.get(SessionManager.KEY_ID);
        String token = usersesion.get(SessionManager.TOKEN);
        retro = new RetrofitInstance(token);
        Router service = retro.getRetrofitInstanceall().create(Router.class);

        id = usersesion.get(SessionManager.KEY_ID);
        Call<User> call = service.Updateuser(user,id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getStatus().equals("sukses")){
                    Toast.makeText(getApplicationContext(),"Berhasil Update data", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Setting.this,Setting.class);
                    startActivity(i);
                    finish();
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


          final HashMap<String, String> usersesion = sesi.getUserDetails();
          sesi = new SessionManager(getApplicationContext());
          id = usersesion.get(SessionManager.KEY_ID);
          String token = usersesion.get(SessionManager.TOKEN);
          retro = new RetrofitInstance(token);

          service = retro.getRetrofitInstanceall().create(Router.class);
          id = usersesion.get(SessionManager.KEY_ID);

          String a = file.getName();
         // ld = (EditText)findViewById(R.id.lod);
          String[] kf = a.split("\\.");
          final String first = kf[0];

          Log.d("DEBUG ", "BARU activity seting " + first);
          Glide
                  .with(Setting.this)
                  .load(URL + "getimage/" + first)
                  .into(userfoto);

          sesi = new SessionManager(getApplicationContext());
          sesi.createImage(first);

          int Barberid = Integer.parseInt(id);

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
                  Toast.makeText(Setting.this, "Gagal Upload Foto", Toast.LENGTH_SHORT).show();
              }
          });

          //post file name
          call2.enqueue(new Callback<Upload>() {
              @Override
              public void onResponse(Call<Upload> call, Response<Upload> response) {
                  Toast.makeText(Setting.this, response.body().getRespon(), Toast.LENGTH_SHORT).show();
                  Intent i=new Intent(Setting.this, Setting.class);
                  startActivity(i);
                  finish();
              }

              @Override
              public void onFailure(Call<Upload> call, Throwable t) {
                  Toast.makeText(Setting.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
              }
          });
    }


    //method untuk mengcapture gambar menggunakan kamera bisa di letakan saat onClick
    public void takeImageFromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, PICK_IMAGE);
    }

    //untuk mengambil gambar hasil capture camera tadi kita harus override onActivityResult dan membaca resultCode apakah sukses dan requestCode apakah dari Camera_Request
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            //panggil method uploadImage
            uploadImage(mphoto);
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

    //TODO mengambil gambar menggunakan resource dari folder drawable bisa di letakan saat onClick, bedanya yang kita upload ini adalah file yang dari folder drawable project bukan foto kamera seperti di atas.
    public void getImageDrawable() {
        Bitmap dummybm = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_store_white_24dp);
        //panggil method uploadImage
        uploadImage(dummybm);
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

    public void uploadImage(Bitmap gambarbitmap) {
         HashMap<String, RequestBody> map = new HashMap<>();
         map.put("cycle_time_begin", createPartFromString("2017-07-19"));
         map.put("fish_species", createPartFromString("LELE SUPER"));
         map.put("crop_species", createPartFromString("BAYAM MERAH"));
         map.put("id", createPartFromString("323278djsadkhjye2"));

        //convert gambar jadi File terlebih dahulu dengan memanggil createTempFile yang di atas tadi.
        File file = createTempFile(gambarbitmap);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody df = RequestBody.create(MediaType.parse("barbershop manado"),"082188352121");
        MultipartBody.Part body = MultipartBody.Part.createFormData("cycle", file.getName(), reqFile);
        MultipartBody.Part body2 = MultipartBody.Part.createFormData("diran", "andrian", df);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(Router.class);

        String a = file.getName();
//        ld = (EditText)findViewById(R.id.lod);
//        ld.setText(a);

        sesi = new SessionManager(getApplicationContext());
        sesi = new SessionManager(getApplicationContext());
        sesi.createImage(a);
        HashMap<String, String> usersesion = sesi.getUserDetails();
        id = usersesion.get(SessionManager.KEY_ID);
        int Barberid = Integer.parseInt(id);
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
                Toast.makeText(Setting.this, "Gagal Upload", Toast.LENGTH_SHORT).show();
            }
        });

        //post file name
        call2.enqueue(new Callback<Upload>() {
            @Override
            public void onResponse(Call<Upload> call, Response<Upload> response) {
                Toast.makeText(Setting.this, response.body().getRespon(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Upload> call, Throwable t) {
                Toast.makeText(Setting.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent i = new Intent(Setting.this,KonekMaps.class);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void dialog(final String lama, final String baru) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
        builder.setMessage("Password baru anda adalah " + baru + " apakah Anda yakin ingin melakukan perubahan password")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            User user = new User(lama,baru);
                            update(user);
                        }catch (Exception e){
                            Toast.makeText(Setting.this, "Ada Masalah dengan Server", Toast.LENGTH_SHORT).show();
                        }
                        // untuk finish keluar
                        // LoginActivity.this.finish();
                    }
                })
                .setNegativeButton("Tidak",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        }).show();
    }






}
