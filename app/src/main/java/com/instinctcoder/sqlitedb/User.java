package com.instinctcoder.sqlitedb;

public class User {

    public static final String TABLE = "User";

    public static final String KEY_ID = "id";
    public static final String KEY_name = "name";
    public static final String KEY_email = "email";
    public static final String KEY_latitude = "latitude";
    public static final String KEY_longitude = "longitude";
    public static final String KEY_photo = "photo";

    public static int user_ID;
    public String name;
    public String email;
    public double latitude;
    public double longitude;
    public String photo;
}
