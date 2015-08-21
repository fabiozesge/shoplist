package com.instinctcoder.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "crud.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_PRODUCT = "CREATE TABLE " + Product.TABLE  + "("
                + Product.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Product.KEY_description + " TEXT, "
                + Product.KEY_quantity + " DOUBLE, "
                + Product.KEY_unit + " TEXT, "
                + Product.KEY_brand+ " TEXT )";

        String CREATE_TABLE_USER = "CREATE TABLE " + User.TABLE  + "("
                + User.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + User.KEY_name + " TEXT, "
                + User.KEY_email + " TEXT, "
                + User.KEY_photo + " TEXT, "
                + User.KEY_latitude + " DOUBLE, "
                + User.KEY_longitude+ " DOUBLE )";

        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + Product.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);

        // Create tables again
        onCreate(db);
    }

}
