package com.example.irhabi.retrobarbershop.newmasuklogin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.User;
import com.example.irhabi.retrobarbershop.rest.Router;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.irhabi.retrobarbershop.AppConfig.URL;

public class Setting extends Fragment {

    private static final int MY_PERMISSION_REQUEST = 100;
    private int PICK_IMAGE_FROM_GALERY_REQUEST = 1;
    Button upld ;
    EditText ld;

    public Setting(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        // Inflate the layout for this fragment
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                             != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST);
        }
        upld= (Button)view.findViewById(R.id.upload);
        ld = (EditText)view.findViewById(R.id.lod);

        upld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(
                        Intent.createChooser(i, "Pilih Gambar"),
                        PICK_IMAGE_FROM_GALERY_REQUEST);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void onRequsetPermissionResult(int requestCode,
                                          String permission[], int[] grantResults){
    }

    private void uploadFile(Uri fileUri){
        final EditText name = (EditText) findViewById(R.id.input_description);

        RequestBody descriptionpart = RequestBody.create(MultipartBody.FORM, name.getText().toString());

        File originalFile =  FileUtils.getFile(this, fileUri);

        RequestBody filePart = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)),originalFile;

        MultipartBody.Part file = MultipartBody.Part.createFormData("photo", originalFile.getName(), filePart);


        retrofit2.Retrofit.Builder builder = new retrofit2.Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create());
         Retrofit retrofit = builder.build();
        Router client  = retrofit.create(Router.class);
        Call<Upload> call = client.Login(user);
        call.enqueue(new Callback<User>() {
    }
}
