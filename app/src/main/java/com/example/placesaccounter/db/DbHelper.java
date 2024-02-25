package com.example.placesaccounter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DbHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "";
    private SQLiteDatabase db;
    private final Context context;
    private boolean needUpdate = false;

    public DbHelper(@Nullable Context context) {
        super(context, MyConstants.DB_NAME, null, MyConstants.DB_VERSION);

        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.context = context;

        copyDataBase();

        this.getReadableDatabase();
    }

    public void updateDataBase() throws IOException {
        if (needUpdate) {
            File dbFile = new File(DB_PATH + MyConstants.DB_NAME);

            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            needUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + MyConstants.DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();

            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream input = context.getAssets().open(MyConstants.DB_NAME);
        OutputStream output = Files.newOutputStream(Paths.get(DB_PATH + MyConstants.DB_NAME));

        byte[] buffer = new byte[1024];
        int length;

        while ((length = input.read(buffer)) > 0)
            output.write(buffer, 0, length);

        output.flush();
        output.close();
        input.close();
    }

    @Override
    public synchronized void close() {
        if (db != null)
            db.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            needUpdate = true;
    }
}
