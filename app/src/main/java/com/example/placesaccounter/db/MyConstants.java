package com.example.placesaccounter.db;

public class MyConstants {
    public static final String _ID = "_id";
    public static final String TABLE_ROOM_NAME = "rooms";
    public static final String TABLE_LEARNER_NAME = "learners";
    public static final String FLOOR_NUMBER = "floor_number";
    public static final String ROOM_NUMBER = "room_number";
    public static final String STREAM_NUMBER = "stream_number";
    public static final String CHECK_IN_DATE = "check_in_date";
    public static final String CHECK_OUT_DATE = "check_out_date";
    public static final String BEDS_NUMBER = "beds_number";
    public static final String DB_NAME = "app_db.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_STRUCTURE_ROOM =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ROOM_NAME +
                    " (" + FLOOR_NUMBER + " INTEGER," +
                    ROOM_NUMBER + " INTEGER PRIMARY KEY," +
                    BEDS_NUMBER + " INTEGER)";
    public static final String TABLE_STRUCTURE_LEARNER =
            "CREATE TABLE IF NOT EXISTS " + TABLE_LEARNER_NAME +
                    " (" + _ID + " INTEGER PRIMARY KEY," +
                    ROOM_NUMBER + " INTEGER," +
                    STREAM_NUMBER + " INTEGER," +
                    CHECK_IN_DATE + " TEXT," +
                    CHECK_OUT_DATE + " TEXT)";
    public static final String DROP_ROOM_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_ROOM_NAME;
    public static final String DROP_LEARNER_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_LEARNER_NAME;
}
