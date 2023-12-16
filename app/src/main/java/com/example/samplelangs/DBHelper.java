package com.example.samplelangs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;



public class DBHelper extends SQLiteOpenHelper {
    public enum VehicleType {
        CAR,
        MOTOR,
        ALL
    }
    public DBHelper(Context context) {
        super(context, "Mechdata.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Mechdetails(owner TEXT, garagename TEXT, username TEXT primary key, password TEXT, reenter TEXT, address TEXT, contact TEXT, vehicleType TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("drop Table if exists Mechdetails");
    }
    public Boolean insertmechdata(String owner, String garagename, String username, String password,
                                  String reenter, String address, String contact,VehicleType vehicleType) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("owner", owner);
        contentValues.put("garagename", garagename);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("reenter", reenter);
        contentValues.put("address", address);
        contentValues.put("contact", contact);
        contentValues.put("vehicleType", vehicleType.name()); // Store the enum name as a String
        long result = DB.insert("Mechdetails", null, contentValues);
        return result != -1;
    }

    public Boolean checkusername(String username)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Mechdetails where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepass(String username, String password)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Mechdetails where username = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkgaragename(String garagename)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Mechdetails where garagename = ?", new String[]{garagename});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Cursor getdata ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Mechdetails", null);
        return cursor;
    }

    // Add this modified method to DBHelper class
    public List<Mechanic> getMechanicData() {
        List<Mechanic> mechanicList = new ArrayList<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select username, owner, garagename, address, contact, vehicleType from Mechdetails", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
                @SuppressLint("Range") String owner = cursor.getString(cursor.getColumnIndex("owner"));
                @SuppressLint("Range") String garageName = cursor.getString(cursor.getColumnIndex("garagename"));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
                @SuppressLint("Range") String contact = cursor.getString(cursor.getColumnIndex("contact"));
                @SuppressLint("Range") String vehicleTypeString = cursor.getString(cursor.getColumnIndex("vehicleType"));
                VehicleType vehicleType = VehicleType.valueOf(vehicleTypeString);

                Mechanic mechanic = new Mechanic(username,owner,garageName,address,contact,vehicleType.name());

                mechanicList.add(mechanic);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return mechanicList;
    }


}
