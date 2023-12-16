package com.example.samplelangs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserRegDB extends SQLiteOpenHelper{
    public UserRegDB(Context context) {
            super(context, "Regdata.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table userinfo(name TEXT, username TEXT primary key, password TEXT, reenter TEXT, address TEXT, contact INTEGER )");
    }
    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("drop Table if exists userinfo");
    }
    public Boolean insertuserinfo(String name, String username, String password, String reenter, String address, String contact)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("reenter", reenter);
        contentValues.put("address", address);
        contentValues.put("contact", contact);
        long result=DB.insert("userinfo", null, contentValues);
        return result != -1;
    }

    public Boolean checkusername(String username)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from userinfo where username = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public Boolean checkusernamepass(String username, String password)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from userinfo where username = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    public Cursor getdata ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from userinfo", null);
        return cursor;
    }
}


