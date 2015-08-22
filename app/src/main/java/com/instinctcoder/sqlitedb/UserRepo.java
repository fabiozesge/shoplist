package com.instinctcoder.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class UserRepo {
    private DBHelper dbHelper;

    public UserRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(User user) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.KEY_name, user.name);
        values.put(User.KEY_email,user.email);
        values.put(User.KEY_latitude, user.latitude);
        values.put(User.KEY_longitude, user.longitude);
        values.put(User.KEY_photo, user.photo);

        long user_Id = db.insert(User.TABLE, null, values);
        db.close();
        return (int) user_Id;
    }

    public void delete(int user_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(User.TABLE, User.KEY_ID + "= ?", new String[] { String.valueOf(user_Id) });
        db.close();
    }

    public void deleteall() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(User.TABLE, "", null);
        db.close();
    }

    public void update(User user) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(User.KEY_name, user.name);
        values.put(User.KEY_email,user.email);
        values.put(User.KEY_latitude, user.latitude);
        values.put(User.KEY_longitude, user.longitude);
        values.put(User.KEY_photo, user.photo);

        db.update(User.TABLE, values, User.KEY_ID + "= ?", new String[] { String.valueOf(User.user_ID) });
        db.close();
    }

    public ArrayList<HashMap<String, String>>  getProductList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                User.KEY_ID + "," +
                User.KEY_name + "," +
                User.KEY_email + "," +
                User.KEY_latitude + "," +
                User.KEY_longitude + "," +
                User.KEY_photo+
                " FROM " + User.TABLE;

        ArrayList<HashMap<String, String>> userList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> user = new HashMap<String, String>();
                user.put("id", cursor.getString(cursor.getColumnIndex(User.KEY_ID)));
                user.put("name", cursor.getString(cursor.getColumnIndex(User.KEY_name)));
                user.put("email", cursor.getString(cursor.getColumnIndex(User.KEY_email)));
                user.put("longitude", cursor.getString(cursor.getColumnIndex(User.KEY_longitude)));
                user.put("latitude", cursor.getString(cursor.getColumnIndex(User.KEY_latitude)));
                user.put("photo", cursor.getString(cursor.getColumnIndex(User.KEY_photo)));
                userList.add(user);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;

    }

    public User getUserById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                User.KEY_ID + "," +
                User.KEY_name + "," +
                User.KEY_email + "," +
                User.KEY_latitude + "," +
                User.KEY_longitude + "," +
                User.KEY_photo+
                " FROM " + User.TABLE;
                //+ " WHERE " +
                //User.KEY_ID + "=? or ?=0";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        User user = new User();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { });//String.valueOf(Id) ,String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                user.user_ID =cursor.getInt(cursor.getColumnIndex(User.KEY_ID));
                user.name =cursor.getString(cursor.getColumnIndex(User.KEY_name));
                user.email  = cursor.getString(cursor.getColumnIndex(User.KEY_email));
                user.longitude = cursor.getDouble(cursor.getColumnIndex(User.KEY_longitude));
                user.latitude = cursor.getDouble(cursor.getColumnIndex(User.KEY_latitude));
                user.photo  = cursor.getString(cursor.getColumnIndex(User.KEY_photo));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return user;
    }

}