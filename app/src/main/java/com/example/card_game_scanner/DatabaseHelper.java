package com.example.card_game_scanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private Context context;
    public  static final String DATABASE_NAME = "CardLibary.db";
    public  static final int DATABASE_VERSION = 1;

    public  static final String TABLE_NAME = "cards";
    public  static final String COLUMN_ID = "_id";
    public  static final String COLUMN_GAME = "card_game";
    public  static final String COLUMN_TITLE = "card_title";
    public  static final String COLUMN_EFFECT = "card_effect";
    public  static final String COLUMN_PICTURE = "card_image";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}