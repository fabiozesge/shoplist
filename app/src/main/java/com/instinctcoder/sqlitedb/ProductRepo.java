package com.instinctcoder.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductRepo {
    private DBHelper dbHelper;

    public ProductRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Product product) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Product.KEY_description, product.description);
        values.put(Product.KEY_quantity,product.quantity);
        values.put(Product.KEY_unit, product.unit);
        values.put(Product.KEY_brand, product.brand);

        long product_Id = db.insert(Product.TABLE, null, values);
        db.close();
        return (int) product_Id;
    }

    public void delete(int product_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Product.TABLE, Product.KEY_ID + "= ?", new String[] { String.valueOf(product_Id) });
        db.close();
    }

    public void update(Product product) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Product.KEY_description, product.description);
        values.put(Product.KEY_quantity,product.quantity);
        values.put(Product.KEY_unit, product.unit);
        values.put(Product.KEY_brand, product.brand);

        db.update(Product.TABLE, values, Product.KEY_ID + "= ?", new String[] { String.valueOf(Product.product_ID) });
        db.close();
    }

    public ArrayList<HashMap<String, String>>  getProductList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Product.KEY_ID + "," +
                Product.KEY_description + "," +
                Product.KEY_quantity + "," +
                Product.KEY_unit + "," +
                Product.KEY_brand +
                " FROM " + Product.TABLE;

        ArrayList<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> product = new HashMap<String, String>();
                product.put("id", cursor.getString(cursor.getColumnIndex(Product.KEY_ID)));
                product.put("description", cursor.getString(cursor.getColumnIndex(Product.KEY_description)));
                product.put("unit", cursor.getString(cursor.getColumnIndex(Product.KEY_unit)));
                product.put("quantity", cursor.getString(cursor.getColumnIndex(Product.KEY_quantity)));
                product.put("brand", cursor.getString(cursor.getColumnIndex(Product.KEY_brand)));
                productList.add(product);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return productList;

    }

    public Product getProductById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Product.KEY_ID + "," +
                Product.KEY_description + "," +
                Product.KEY_quantity + "," +
                Product.KEY_unit + "," +
                Product.KEY_brand +
                " FROM " + Product.TABLE
                + " WHERE " +
                Product.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        Product product = new Product();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                product.product_ID =cursor.getInt(cursor.getColumnIndex(Product.KEY_ID));
                product.description =cursor.getString(cursor.getColumnIndex(Product.KEY_description));
                product.quantity  = cursor.getDouble(cursor.getColumnIndex(Product.KEY_quantity));
                product.unit = cursor.getString(cursor.getColumnIndex(Product.KEY_unit));
                product.brand = cursor.getString(cursor.getColumnIndex(Product.KEY_brand));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return product;
    }

}