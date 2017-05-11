package com.wbigger.sqlexample.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wbigger.sqlexample.data.TeamContract.*;
/**
 * Created by claudio on 5/11/17.
 */

public class TeamDbHelper extends SQLiteOpenHelper {
    // The database name
    private static final String DATABASE_NAME = "team.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public TeamDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold team data
        final String SQL_CREATE_TEAM_TABLE = "CREATE TABLE " + TeamEntry.TABLE_NAME + " (" +
                TeamEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TeamEntry.COLUMN_PLAYER_NAME + " TEXT NOT NULL, " +
                TeamEntry.COLUMN_PLAYER_NUMBER + " INTEGER NOT NULL, " +
                TeamEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_TEAM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TeamEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
