package com.example.samplelangs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ServiceDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ServiceData.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Services";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_SERVICENAME = "servicename";
    private static final String COLUMN_COST = "cost";

    public ServiceDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_SERVICENAME + " TEXT, " +
                COLUMN_COST + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertService(String username, String serviceName, double cost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_SERVICENAME, serviceName);
        contentValues.put(COLUMN_COST, cost);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public List<Service> getServicesForMechanic(String username) {
        List<Service> serviceList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery("SELECT servicename,cost FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=?", selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String serviceName = cursor.getString(cursor.getColumnIndex(COLUMN_SERVICENAME));
                @SuppressLint("Range") double cost = cursor.getDouble(cursor.getColumnIndex(COLUMN_COST));
                serviceList.add(new Service(username, serviceName, cost));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return serviceList;
    }
}
