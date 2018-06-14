package com.xwards.xview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xwards.xview.respmodel.EventModel;

import java.util.ArrayList;
import java.util.List;

import static com.xwards.xview.database.DBConstInterface.EVENT_DATE;
import static com.xwards.xview.database.DBConstInterface.EVENT_END_DATE;
import static com.xwards.xview.database.DBConstInterface.EVENT_HEADER;
import static com.xwards.xview.database.DBConstInterface.EVENT_ID;
import static com.xwards.xview.database.DBConstInterface.EVENT_LOGO_URL;
import static com.xwards.xview.database.DBConstInterface.EVENT_MESSAGE;
import static com.xwards.xview.database.DBConstInterface.EVENT_SITE_URL;
import static com.xwards.xview.database.DBConstInterface.EVENT_START_DATE;
import static com.xwards.xview.database.DBConstInterface.EVENT_TABLE;
import static com.xwards.xview.database.DBConstInterface.EVENT_VELUE;


/**
 * Created by Nithin on 21-03-2018.
 */

public class EventDAO {

    private static final String TAG = "EventDAO";
    private final SQLiteDatabase db;

    public EventDAO(Context context) {
        SuperSQLiteHelper dbHelper = new SuperSQLiteHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * select A.salesman_about,
     * from Salesman_Table as A where A.salesman_username
     * = "" and A.salesman_password = ""
     */
    public List<EventModel> getAllEventData() {
        List<EventModel> goldModelList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EVENT_TABLE, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    goldModelList.add(getFilledAdvObj(cursor));
                } while (cursor.moveToNext());
            }
            return goldModelList;
        } catch (Exception ignored) {
            Log.e(TAG, ignored.getMessage());
        } finally {
            cursor.close();
        }
        return null;
    }

    public boolean hasObject(int id) {

        String selectString = "SELECT * FROM " + EVENT_TABLE + " WHERE " + EVENT_ID + " =" + id;

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[]{"" + id});

        boolean hasObject = false;
        if (cursor.moveToFirst()) {
            hasObject = true;
            //region if you had multiple records to check for, use this region.

            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
            //here, count is records found
            Log.d(TAG, String.format("%d records found", count));
            //endregion
        }
        cursor.close();          // Dont forget to close your cursor
        return hasObject;
    }

    private EventModel getFilledAdvObj(Cursor cursor) {
        EventModel model = new EventModel();
        model.setEventId(cursor.getInt(cursor.getColumnIndex(EVENT_ID)));
        model.setEventHeader(cursor.getString(cursor.getColumnIndex(EVENT_HEADER)));
        model.setEventUrl(cursor.getString(cursor.getColumnIndex(EVENT_SITE_URL)));
        model.setEventLogoURl(cursor.getString(cursor.getColumnIndex(EVENT_LOGO_URL)));
        model.setEventMessage(cursor.getString(cursor.getColumnIndex(EVENT_MESSAGE)));
        model.setVenue(cursor.getString(cursor.getColumnIndex(EVENT_VELUE)));
        model.setEventDate(cursor.getString(cursor.getColumnIndex(EVENT_DATE)));
        model.setStartDateTime(cursor.getString(cursor.getColumnIndex(EVENT_START_DATE)));
        model.setEndDateTime(cursor.getString(cursor.getColumnIndex(EVENT_END_DATE)));
        return model;
    }

    public long insertObject(EventModel object) {
        try {
           /* if (hasObject(object.getEventId())) {
                return updateRaw(object);
            }*/
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstInterface.EVENT_ID, object.getEventId());
            contentValues.put(DBConstInterface.EVENT_HEADER, object.getEventHeader());
            contentValues.put(DBConstInterface.EVENT_SITE_URL, object.getEventUrl());
            contentValues.put(DBConstInterface.EVENT_LOGO_URL, object.getEventLogoURl());
            contentValues.put(DBConstInterface.EVENT_MESSAGE, object.getEventMessage());
            contentValues.put(DBConstInterface.EVENT_VELUE, object.getVenue());
            contentValues.put(DBConstInterface.EVENT_DATE, object.getEventDate());
            contentValues.put(DBConstInterface.EVENT_START_DATE, object.getStartDateTime());
            contentValues.put(DBConstInterface.EVENT_END_DATE, object.getEndDateTime());
            /**
             * Insert the value to DB
             */
            long id = db.insert(DBConstInterface.EVENT_TABLE, null, contentValues);
            Log.i(TAG, "Submit Data Raw Id " + id);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }

    private long updateRaw(EventModel object){
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstInterface.EVENT_ID, object.getEventId());
            contentValues.put(DBConstInterface.EVENT_HEADER, object.getEventHeader());
            contentValues.put(DBConstInterface.EVENT_SITE_URL, object.getEventUrl());
            contentValues.put(DBConstInterface.EVENT_LOGO_URL, object.getEventLogoURl());
            contentValues.put(DBConstInterface.EVENT_MESSAGE, object.getEventMessage());
            contentValues.put(DBConstInterface.EVENT_VELUE, object.getVenue());
            contentValues.put(DBConstInterface.EVENT_DATE, object.getEventDate());
            contentValues.put(DBConstInterface.EVENT_START_DATE, object.getStartDateTime());
            contentValues.put(DBConstInterface.EVENT_END_DATE, object.getEndDateTime());
            /**
             * Insert the value to DB
             */
            long id = db.update(DBConstInterface.EVENT_TABLE, contentValues, EVENT_ID+" = "+object.getEventId(), null);
            return id;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return object.getEventId();
    }


    //delete from adv_table where adv_id = 12
    public void deleteFromTable(int advId) {
        String rawQuery = "DELETE FROM " + EVENT_TABLE + " WHERE " + EVENT_ID + " = " + advId;

    }
}
