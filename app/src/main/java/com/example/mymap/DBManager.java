package com.example.mymap;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static final String DATABASE_NAME = "LostAndFoundDB";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_ADVERTS = "CREATE TABLE IF NOT EXISTS adverts (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "type TEXT," +
            "name TEXT," +
            "phone TEXT," +
            "description TEXT," +
            "date TEXT," +
            "location TEXT)";

    private Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBManager(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(this.context);
    }

    public long insertAdvert(Advert advert) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("type", advert.getType());
        initialValues.put("name", advert.getName());
        initialValues.put("phone", advert.getPhone());
        initialValues.put("description", advert.getDescription());
        initialValues.put("date", advert.getDate());
        initialValues.put("location", advert.getLocation());

        return db.insert("adverts", null, initialValues);
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_ADVERTS);
            } catch (android.database.SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // In case of database version upgrade, drop older table and create new one
            db.execSQL("DROP TABLE IF EXISTS adverts");
            onCreate(db);
        }
    }

    // Open the database connection
    public DBManager open() throws android.database.SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection
    public void close() {
        DBHelper.close();
    }


    @SuppressLint("Range")
    public List<Advert> getAllAdverts() {
        List<Advert> adverts = new ArrayList<>();
        String selectQuery = "SELECT * FROM adverts";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Advert advert = new Advert();
                advert.setId(cursor.getInt(cursor.getColumnIndex("id")));
                advert.setType(cursor.getString(cursor.getColumnIndex("type")));
                advert.setName(cursor.getString(cursor.getColumnIndex("name")));
                advert.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                advert.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                advert.setDate(cursor.getString(cursor.getColumnIndex("date")));
                advert.setLocation(cursor.getString(cursor.getColumnIndex("location")));

                adverts.add(advert);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return adverts;
    }

    @SuppressLint("Range")
    public Advert getAdvert(int id) {
        String selectQuery = "SELECT * FROM adverts WHERE id = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Advert advert = new Advert();
            advert.setId(cursor.getInt(cursor.getColumnIndex("id")));
            advert.setType(cursor.getString(cursor.getColumnIndex("type")));
            advert.setName(cursor.getString(cursor.getColumnIndex("name")));
            advert.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            advert.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            advert.setDate(cursor.getString(cursor.getColumnIndex("date")));
            advert.setLocation(cursor.getString(cursor.getColumnIndex("location")));

            cursor.close();
            return advert;
        }

        cursor.close();
        return null; // Return null if no advert found
    }

    public void deleteAdvert(int id) {
        db.delete("adverts", "id = ?", new String[]{String.valueOf(id)});
    }

}

