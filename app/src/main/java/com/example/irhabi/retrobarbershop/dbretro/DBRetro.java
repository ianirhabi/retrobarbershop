package com.example.irhabi.retrobarbershop.dbretro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.HashMap;

public class DBRetro extends SQLiteOpenHelper {

    //string nama db
    public static final String NM_RETRO = "retro.db";
    public static final int DATABASE_VERSION = 1;
    public static HashMap hp;

    //string table absensi
    public static  String TABLE_ABSENSI = "absensi";
    public static String TANGGAL = "tanggal";
    public static String HARI = "hari";
    public static String JAM = "jam";
    public static String STATUS_ABSEN = "status";
    public static String LAT = "lat";
    public static String LONT = "lont";

    public static final String create_laporanbsensi = "create table "+TABLE_ABSENSI +" (id integer primary key autoincrement, "+
            TANGGAL+" text not null,"+HARI+" text not null,"+JAM+" text not null," +STATUS_ABSEN+" text not null,"
            +LAT+" text not null,"+LONT+" text not null)";

    public DBRetro(Context context, int version) {
        super(context, NM_RETRO, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(create_laporanbsensi);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("drop table if exists " +TABLE_ABSENSI);
       onCreate(db);
    }

    public boolean insertAbsen(String tanggal, String jam, String hari, String kehadiran, String lat, String lont,Context mContext){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("jam", jam);
        contentValues.put("hari", hari);
        contentValues.put("status", kehadiran);
        contentValues.put("lat", lat);
        contentValues.put("lont", lont);
        contentValues.put("tanggal", tanggal);
        long result = db.insert(TABLE_ABSENSI,null,contentValues);
        if (result == -1) {
            return false;
        }else{
            Toast.makeText(mContext, "berhasil update data", Toast.LENGTH_LONG).show();
             return true;
        }
    }

    public boolean checkDataAbsenIsavalable(String Tanggal){
         SQLiteDatabase db = this.getReadableDatabase();
          Cursor resultCursor = db.rawQuery("select * from "+TABLE_ABSENSI+ " where TRIM(tanggal) " +
                  "= '" + Tanggal.trim()+ "'",null);
        if (resultCursor.getCount()>0){
            return true;
        }else{
            return false;
        }
    }

    public Cursor getListForAbsen(String fromDate, String toDate){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor resultCursor = db.rawQuery("select * from " + TABLE_ABSENSI + " where "
                + TANGGAL + " between '" +fromDate + "' AND '"+toDate+"'",null);
        return resultCursor;
    }
}
