package com.dileepkumar.passwordmanager.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "password_manager.db";
    public static final String TABLE_NAME = "password_table";

    public static final String Col_1 = "ID";
    public static final String Col_2 = "Website";
    public static final String Col_3 = "Email";
    public static final String Col_4 = "Password";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " +
                TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Website TEXT," +
                "Email TEXT," +
                "Password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // Getting all the data from the database
    public Cursor getAllData() {
        // Creating an instance of the database
        SQLiteDatabase db = this.getWritableDatabase();

        // Querying the database using cursor to get all the data
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        return cursor;
    }

    // Inserting Operation Is Handled In This Method
    public boolean insertData(String website, String email, String password) {
        // Creating an instance of the database
        SQLiteDatabase db = this.getWritableDatabase();

        // Creating a contentValue to store and add the data to database
        ContentValues contentValue = new ContentValues();
        contentValue.put(Col_2, website);
        contentValue.put(Col_3, email);
        contentValue.put(Col_4, password);
        long result = db.insert(TABLE_NAME, null, contentValue);
        return result != -1;
    }

    // Deleting data row from the database
    public void deleteData(String id) {
        // Creating an instance of the database
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete and return the number of row deleted
        db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

    // Update the data in the database
    public boolean updateData(String id, String website, String email, String password) {

        Log.i("Update","In SQLite Database");

        // Creating an instance of the database
        SQLiteDatabase db = this.getWritableDatabase();

        // Creating a contentValue to store and add the data to database
        ContentValues contentValue = new ContentValues();

        contentValue.put(Col_1, id);
        contentValue.put(Col_2, website);
        contentValue.put(Col_3, email);
        contentValue.put(Col_4, password);

        db.update(TABLE_NAME, contentValue, "ID = ?", new String[]{id});

        return true;
    }

}
