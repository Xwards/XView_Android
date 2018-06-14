package com.xwards.xview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xwards.xview.respmodel.AdVideoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nithinjith on 3/4/2017.
 * DB- Mapping to Advertisement
 */

public class AdvertDAO implements DBConstInterface {

    private static final String TAG = "AdvertDAO";
    private final SQLiteDatabase db;

    public AdvertDAO(Context context) {
        SuperSQLiteHelper dbHelper = new SuperSQLiteHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * select A.salesman_about,
     * from Salesman_Table as A where A.salesman_username
     * = "" and A.salesman_password = ""
     */
    public List<AdVideoModel> getAllAdvList() {
        List<AdVideoModel> goldModelList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBConstInterface.ADV_TABLE, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    goldModelList.add(getFilledAdvObj(cursor));
                } while (cursor.moveToNext());
            }
            return goldModelList;
        } catch (Exception ignored) {
            Log.d(TAG, ignored.getMessage());
        } finally {
            cursor.close();
        }
        return null;
    }

    public boolean hasObject(int id) {

        String selectString = "SELECT * FROM " + ADV_TABLE + " WHERE " + ADV_ID + " =" + id;

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

    private AdVideoModel getFilledAdvObj(Cursor cursor) {
        AdVideoModel model = new AdVideoModel();
        model.setAdvId(cursor.getInt(cursor.getColumnIndex(ADV_ID)));
        model.setAdvName(cursor.getString(cursor.getColumnIndex(ADV_NAME)));
        model.setAdvPreviewUrl(cursor.getString(cursor.getColumnIndex(ADV_REVIEW_URL)));
        model.setAdvUrl(cursor.getString(cursor.getColumnIndex(ADV_URL)));
        model.setAdvEndDate(cursor.getString(cursor.getColumnIndex(ADV_END_DATE)));
        model.setAdvStartDate(cursor.getString(cursor.getColumnIndex(ADV_START_DATE)));
        model.setBannerUrl(cursor.getString(cursor.getColumnIndex(ADV_BANNER_URL)));
        return model;
    }

    public long insertObject(AdVideoModel object) {
        try {
            /*if (hasObject(object.getAdvId())) {
                updateRaw(object);
                return object.getAdvId();
            }*/

            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstInterface.ADV_ID, object.getAdvId());
            contentValues.put(DBConstInterface.ADV_NAME, object.getAdvName());
            contentValues.put(DBConstInterface.ADV_URL, object.getAdvUrl());
            contentValues.put(DBConstInterface.ADV_REVIEW_URL, object.getAdvPreviewUrl());
            contentValues.put(DBConstInterface.ADV_START_DATE, object.getAdvStartDate());
            contentValues.put(DBConstInterface.ADV_END_DATE, object.getAdvEndDate());
            contentValues.put(DBConstInterface.ADV_BANNER_URL, object.getBannerUrl());
            contentValues.put(DBConstInterface.ADV_BANNER_SITE_URL, object.getBannerSiteURL());
            contentValues.put(DBConstInterface.ADV_VIDEO_SITE_URL, object.getAdvSiteURL());
            /**
             * Insert the value to DB
             */
            long id = db.insert(DBConstInterface.ADV_TABLE, null, contentValues);
            Log.i(TAG, "Submit Data Raw Id " + id);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }

    private long updateRaw(AdVideoModel object){
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstInterface.ADV_ID, object.getAdvId());
            contentValues.put(DBConstInterface.ADV_NAME, object.getAdvName());
            contentValues.put(DBConstInterface.ADV_URL, object.getAdvUrl());
            contentValues.put(DBConstInterface.ADV_REVIEW_URL, object.getAdvPreviewUrl());
            contentValues.put(DBConstInterface.ADV_START_DATE, object.getAdvStartDate());
            contentValues.put(DBConstInterface.ADV_END_DATE, object.getAdvEndDate());
            contentValues.put(DBConstInterface.ADV_BANNER_URL, object.getBannerUrl());
            contentValues.put(DBConstInterface.ADV_BANNER_SITE_URL, object.getBannerSiteURL());
            contentValues.put(DBConstInterface.ADV_VIDEO_SITE_URL, object.getAdvSiteURL());
            return db.update(DBConstInterface.ADV_TABLE, contentValues, ADV_ID+" = "+object.getAdvId(), null);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return object.getAdvId();
    }


    //delete from adv_table where adv_id = 12
    public void deleteFromTable(int advId) {
        String rawQuery = "DELETE FROM " + ADV_TABLE + " WHERE " + ADV_ID + " = " + advId;

    }

}
