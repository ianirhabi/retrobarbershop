package com.example.irhabi.retrobarbershop.barbermen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.model.ModelTambahAKun;
import com.example.irhabi.retrobarbershop.rest.RetrofitInstance;
import com.example.irhabi.retrobarbershop.rest.Router;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKaryawan extends AppCompatActivity {
    EditText nama, gender, alamat, nik, usergrup, username, password;
    Button simpan ;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private SessionManager sesi;
    private RetrofitInstance retro;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_karyawan);
        simpan = (Button)findViewById(R.id.simpandata);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tambahdata();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
            {
                if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //Name of Method for Calling Message
                }else{
                    Toast.makeText(this,"You dont have required permission to make the Action",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void MyMessage(String password, String username){
        String myMsg = "Anda telah terdaftar di aplikasi retro barbershop username anda adalah " + username
                + " dan password anda adalah " + password;
        //Begin Check for PhoneNumber
        if(username==null || username.equals("") || myMsg==null  || myMsg.equals("") ){
            Toast.makeText(this,"Field Cant be Empty",Toast.LENGTH_SHORT).show();
        }else{
            if(TextUtils.isDigitsOnly(username)){
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(username, null, myMsg, null, null);
                Toast.makeText(this,"Message Sent",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Please Enter Integer Only",Toast.LENGTH_SHORT).show();
            }
        }
        //End Check for PhoneNumber
   }

    private void Tambahdata() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TambahKaryawan.this);
        builder.setMessage("data username dan password akan di kirim ke karyawaan anda melalui " +
                "pemberitahuan sms dan pastikan anda memiliki pulsa. Nomor tujuan adalah username yang anda input ")
                .setCancelable(false)
                .setPositiveButton("Tambah Data", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
                        nama = (EditText)findViewById(R.id.namaret);

                        alamat=(EditText)findViewById(R.id.alamatret);
                        nik = (EditText)findViewById(R.id.nikret);
                        usergrup = (EditText)findViewById(R.id.usergrupret);
                        username = (EditText)findViewById(R.id.usernameret);
                        password = (EditText)findViewById(R.id.passwordret);
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
                        MyMessage(Password,Username);
                        sesi = new SessionManager(getApplicationContext());
                        final HashMap<String, String> usersesion = sesi.getUserDetails();
                        String token = usersesion.get(SessionManager.TOKEN);
                        Log.d("debug token ", " : " + token);
                        retro = new RetrofitInstance(token);

                        Router service = retro.getRetrofitInstanceall().create(Router.class);
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
                        // untuk finish keluar
                        // LoginActivity.this.finish();
                    }
                })
                .setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        }).show();
    }
}
