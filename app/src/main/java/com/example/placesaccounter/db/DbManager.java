package com.example.placesaccounter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.placesaccounter.models.ModelLearner;
import com.example.placesaccounter.models.ModelRoom;

import java.io.IOException;
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
        try {
            dbHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        db = dbHelper.getWritableDatabase();
    }

    public void closeDb() {
        dbHelper.close();
    }

    public long insertToDb(int room_number, int stream_number, String check_in_date, String check_out_date) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.ROOM_NUMBER, room_number);
        cv.put(MyConstants.STREAM_NUMBER, stream_number);
        cv.put(MyConstants.CHECK_IN_DATE, check_in_date);
        cv.put(MyConstants.CHECK_OUT_DATE, check_out_date);
        long result = db.insert(MyConstants.TABLE_LEARNER_NAME, null, cv);

        return result; // Return the row ID of the newly inserted row
    }

    public void deleteFromDb(long _id) {
        db.delete(MyConstants.TABLE_LEARNER_NAME, MyConstants._ID + "=?", new String[]{String.valueOf(_id)});
    }

    public void updateDbEntry(int stream_number, String check_in_date, String check_out_date, long _id) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.STREAM_NUMBER, stream_number);
        cv.put(MyConstants.CHECK_IN_DATE, check_in_date);
        cv.put(MyConstants.CHECK_OUT_DATE, check_out_date);

        db.update(MyConstants.TABLE_LEARNER_NAME, cv, MyConstants._ID + "=?",
                new String[] { String.valueOf(_id) });
    }

    public List<ModelRoom> readFromDb() {
        List<ModelRoom> roomList = new ArrayList<>();

        Cursor cursor_rooms = db.query(
                MyConstants.TABLE_ROOM_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        while (cursor_rooms.moveToNext()) { // Find all residents who live in certain room
            int roomNumber = cursor_rooms.getInt(cursor_rooms.getColumnIndexOrThrow(MyConstants.ROOM_NUMBER));
            List<ModelLearner> residentList = new ArrayList<>();

            Cursor cursor_residents = db.rawQuery("SELECT * FROM learners WHERE room_number = ?",
                    new String[]{String.valueOf(roomNumber)});

            while (cursor_residents.moveToNext()) {
                residentList.add(new ModelLearner(cursor_residents.getInt(cursor_residents.getColumnIndexOrThrow(MyConstants._ID)),
                                cursor_residents.getInt(cursor_residents.getColumnIndexOrThrow(MyConstants.ROOM_NUMBER)),
                                cursor_residents.getInt(cursor_residents.getColumnIndexOrThrow(MyConstants.STREAM_NUMBER)),
                                cursor_residents.getString(cursor_residents.getColumnIndexOrThrow(MyConstants.CHECK_IN_DATE)),
                                cursor_residents.getString(cursor_residents.getColumnIndexOrThrow(MyConstants.CHECK_OUT_DATE))));
            }

            roomList.add(new ModelRoom(cursor_rooms.getInt(cursor_rooms.getColumnIndexOrThrow(MyConstants.FLOOR_NUMBER)),
                    cursor_rooms.getInt(cursor_rooms.getColumnIndexOrThrow(MyConstants.ROOM_NUMBER)),
                    cursor_rooms.getInt(cursor_rooms.getColumnIndexOrThrow(MyConstants.BEDS_NUMBER)),
                    residentList));

            if (cursor_residents.isLast()) cursor_residents.close();
        }

        cursor_rooms.close();
        return roomList;
    }
}
