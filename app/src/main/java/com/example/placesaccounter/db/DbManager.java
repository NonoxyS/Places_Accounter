package com.example.placesaccounter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DbManager {
    private Context context;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    public DbManager(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    public void openDb() {
        db = dbHelper.getWritableDatabase();
    }

    public void closeDb() {
        dbHelper.close();
    }

    public void insertToDb(int room_number,int stream_number, String check_in_date, String check_out_date) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.ROOM_NUMBER, room_number);
        cv.put(MyConstants.STREAM_NUMBER, stream_number);
        cv.put(MyConstants.CHECK_IN_DATE, check_in_date);
        cv.put(MyConstants.CHECK_OUT_DATE, check_out_date);
        db.insert(MyConstants.TABLE_LEARNER_NAME, null, cv);
    }

    public List<String> readFromDb() {
        List<String> tempList = new ArrayList<>();

        Cursor cursor = db.query(
                MyConstants.TABLE_LEARNER_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            tempList.add(cursor.getInt())
        }

        cursor.close();
    }
}
