package com.xwards.xview.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ZCo developer on 28-06-2016.
 * This is the General Open Helper Class
 * Database configuration has done from here.
 */
class SuperSQLiteHelper extends SQLiteOpenHelper implements DBConstInterface {

    private static final String DATABASE_NAME = "Advertisement.db";
    private static final int DATABASE_VERSION = 1;
    /**
     * Note Table -
     * Values  - Primary key
     * Note time - Time stamp with millisecond format
     * Vin Number- Vehicle Identification number
     * Header - Note header
     * Message - Note message
     */
    private static final String DATABASE_CREATE_ADV = "create table "
            + DBConstInterface.ADV_TABLE + "(" + ADV_ID
            + " integer primary key on conflict replace, " + ADV_NAME
            + " text," + ADV_START_DATE
            + " text," + ADV_END_DATE
            + " text," + ADV_URL
            + " text," + ADV_REVIEW_URL
            + " text," + ADV_BANNER_URL
            + " text," + ADV_BANNER_SITE_URL
            + " text," + ADV_VIDEO_SITE_URL
            + " text);";
    /**
     * EVENT TABLE
     * Values  - Primary key
     * Note time - Time stamp with millisecond format
     * Vin Number- Vehicle Identification number
     * Header - Note header
     * Message - Note message
     */
    private static final String DATABASE_CREATE_EVENT = "create table "
            + DBConstInterface.EVENT_TABLE + "(" + EVENT_ID
            + " integer primary key on conflict replace, " + EVENT_HEADER
            + " text," + EVENT_LOGO_URL
            + " text," + EVENT_MESSAGE
            + " text," + EVENT_SITE_URL
            + " text," + EVENT_START_DATE
            + " text," + EVENT_END_DATE
            + " text," + EVENT_VELUE
            + " text," + EVENT_DATE
            + " text);";


    public SuperSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_ADV);
        db.execSQL(DATABASE_CREATE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
