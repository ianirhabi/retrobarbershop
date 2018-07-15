package com.example.irhabi.retrobarbershop.newmasuklogin;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.LoginActivity;
import com.example.irhabi.retrobarbershop.Maps.KonekMaps;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.Upload;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.irhabi.retrobarbershop.rest.AppConfig.URL;

/**
 * created by codeIrhabi
 */

public class Setting extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 100;
    private int PICK_IMAGE_FROM_GALERY_REQUEST = 1;
    public static final int PICK_IMAGE = 100;
    private Button upld, tk;
    private EditText ld;
    Router service;
    private SessionManager sesi;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);

        if (ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Setting.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST);
        }

        upld = (Button) findViewById(R.id.upload);
        tk = (Button) findViewById(R.id.TAKE);

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

          Retrofit retrofit = new Retrofit.Builder()
                  .baseUrl(URL)
                  .addConverterFactory(GsonConverterFactory.create())
                  .build();

          service = retrofit.create(Router.class);

          String a = file.getName();
          ld = (EditText)findViewById(R.id.lod);


          sesi = new SessionManager(getApplicationContext());
          sesi.createImage(a);
          HashMap<String, String> usersesion = sesi.getUserDetails();
          id = usersesion.get(SessionManager.KEY_ID);
          String image = usersesion.get(SessionManager.KEY_IMAGE);
          int Barberid = Integer.parseInt(id);
          ld.setText(image);
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
        ld = (EditText)findViewById(R.id.lod);
        ld.setText(a);

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
}
