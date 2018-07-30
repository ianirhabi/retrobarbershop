package com.example.irhabi.retrobarbershop.download;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.example.irhabi.retrobarbershop.R;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class Download extends AppCompatActivity {
    private ProgressDialog pDialog;
    private static final int progress_bar_type = 0;
    private static String file_url = "https://wirasetiawan29.files.wordpress.com/2016/01/tentang-material-design1.png";
    ImageView my_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        my_image = (ImageView) findViewById(R.id.my_image);
        Updateaplikasi();
    }

    private void Updateaplikasi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Download.this);
        builder.setMessage("Update aplikasi tersedia ")
                .setCancelable(false)
                .setPositiveButton("Download Aplikasi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DownloadFileFromURL().execute(file_url);
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


    protected Dialog onCreateDialog(int id){
        switch (id){
            case progress_bar_type:
                pDialog = new ProgressDialog(getApplicationContext());
                pDialog.setMessage("Downloading file. Please Wait");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String>

    {
        protected  void onPreExecute(){
            super.onPreExecute();
            //showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int lenghOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                OutputStream output = new FileOutputStream(
                        "/sdcard/downloadedfile.jpg");
                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1){
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e){
                Log.e("Error : ", e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        protected void onPostExecute(String file_url){
            // dismissDialog(progress_bar_type);

            String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";

        }
    }

}
