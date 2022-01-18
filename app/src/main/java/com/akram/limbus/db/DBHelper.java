package com.akram.limbus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "token.db";
    public static final String TABLE_NAME = "token_table";
    public static final String COL_TOKEN = "token";
    public static final String NAIMENOVANIE = "naimenovanie";
    public static final String ADDRES = "addres";
    public static final String FIO = "FIO";
    public static final String PHONE = "phone";

    public DBHelper(Context context){
        super(context, DB_NAME,  null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (token TEXT PRIMARY KEY, naimenovanie TEXT, addres TEXT, FIO TEXT, phone TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String token, String naimenovanie, String addres, String fio, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TOKEN, token);
        contentValues.put(NAIMENOVANIE, naimenovanie);
        contentValues.put(ADDRES, addres);
        contentValues.put(FIO, fio);
        contentValues.put(PHONE, phone);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    public void deletData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }
}
