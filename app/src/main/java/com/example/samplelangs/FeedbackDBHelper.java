package com.example.samplelangs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class FeedbackDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FeedbackData.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Feedback";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_MESSAGE = "messsage";
    private static final String COLUMN_RATE = "rate";

    public FeedbackDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_MESSAGE + " TEXT, " +
                COLUMN_RATE + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertFeedback(String username, String message, int rate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_MESSAGE, message);
        contentValues.put(COLUMN_RATE, rate);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public List<Feedback> getFeedback(String username) {
        List<Feedback> serviceList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery("SELECT messsage,rate FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=?", selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String message = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE));
                @SuppressLint("Range") int rate = cursor.getInt(cursor.getColumnIndex(COLUMN_RATE));
                serviceList.add(new Feedback(username, message, rate));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return serviceList;
    }
}
