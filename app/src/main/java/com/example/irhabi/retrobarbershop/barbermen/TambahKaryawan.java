package com.example.irhabi.retrobarbershop.barbermen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.ModelTambahAKun;
import com.example.irhabi.retrobarbershop.model.User;
import com.example.irhabi.retrobarbershop.model.Usr;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKaryawan extends AppCompatActivity {
    EditText nama, gender, alamat, nik, usergrup, username, password;
    Button simpan ;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_karyawan);



        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        nama = (EditText)findViewById(R.id.namaret);

        alamat=(EditText)findViewById(R.id.alamatret);
        nik = (EditText)findViewById(R.id.nikret);
        usergrup = (EditText)findViewById(R.id.usergrupret);
        username = (EditText)findViewById(R.id.usernameret);
        password = (EditText)findViewById(R.id.passwordret);
        simpan = (Button)findViewById(R.id.simpandata);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioSexGroup.getCheckedRadioButtonId();

                radioSexButton = (RadioButton) findViewById(selectedId);

                final  String Nama = nama.getText().toString();
                final String Gender =  radioSexButton.getText().toString();
                final String Alamat = alamat.getText().toString();
                final  String Nik = nik.getText().toString();
                final String Usergrup = usergrup.getText().toString();
                final String Username = username.getText().toString();
                final String Password = password.getText().toString();

                ModelTambahAKun user = new ModelTambahAKun(Nama, Gender, Alamat, Nik, Usergrup, Username, Password);

                Router service = RetrofitInstance.getRetrofitInstance().create(Router.class);
                Call<ModelTambahAKun> call = service.Postuser(user);
                call.enqueue(new Callback<ModelTambahAKun>() {
                    @Override
                    public void onResponse(Call<ModelTambahAKun> call, Response<ModelTambahAKun> response) {
                        if(response.body().getstatus().equals("berhasil")) {
                            Toast.makeText(getApplication(), "Anda berhasil memasukan data status " + response.body().getstatus(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(TambahKaryawan.this, ControlStylish.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(getApplication(), "Anda gagal menambah data ", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelTambahAKun> call, Throwable t) {
                        Toast.makeText(getApplication(), "gagal menghubungi server", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });


    }
}
