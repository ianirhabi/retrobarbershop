package com.example.irhabi.retrobarbershop.newmasuklogin;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.irhabi.retrobarbershop.R;
import com.example.irhabi.retrobarbershop.sesionmenyimpan.SessionManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;

import static com.example.irhabi.retrobarbershop.newmasuklogin.ScanFragment.QRcodeWidth;

public class Profil extends Fragment {
    public final static int QRcodeWidth = 350 ;
    private Bitmap bitmap ;
    private SessionManager sesi;
    private String usr, usrgrup, id, lo, la;
    private ImageView barber;
    Handler mHandler ;


    public Profil(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profil_dua,container,false);



        sesi = new SessionManager(getActivity());
        HashMap<String, String> usersesion = sesi.getUserDetails();
        usr = usersesion.get(SessionManager.KEY_USER);
        usrgrup = usersesion.get(SessionManager.KEY_USERGRUP);
        id = usersesion.get(SessionManager.KEY_ID);
        la = usersesion.get(SessionManager.LATITUDE);
        lo = usersesion.get(SessionManager.LONGTITUDE);
        int Barberid = Integer.parseInt(id);
        Toast.makeText(getActivity(),usr + " " + usrgrup + " " + Barberid +" " + la + " "+ lo, Toast.LENGTH_LONG).show();
        barber = (ImageView)view.findViewById(R.id.codeQR);

        try {
            bitmap = TextToImageEncode(usr + " " + usrgrup + " " + la + " "+ lo);
            barber.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }


        return view;
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 350, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
