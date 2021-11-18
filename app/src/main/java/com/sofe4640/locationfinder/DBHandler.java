package com.sofe4640.locationfinder;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DBHandler.db";
    public static final String LOCATIONS_TABLE_NAME = "locations";
    public static final String LOCATIONS_COL_ID = "id";
    public static final String LOCATIONS_COL_ADDRESS = "address";
    public static final String LOCATIONS_COL_LATITUDE = "latitude";
    public static final String LOCATIONS_COL_LONGITUDE = "longitude";

    public DBHandler(@Nullable Context context) { super(context, DATABASE_NAME, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists locations" +
                "(id integer primary key autoincrement not null unique, " +
                "address text, latitude real, longitude real)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS locations");
        onCreate(sqLiteDatabase);
    }

    public boolean addLocation(String address, double latitude, double longitude) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LOCATIONS_COL_ADDRESS, address);
        cv.put(LOCATIONS_COL_LATITUDE, latitude);
        cv.put(LOCATIONS_COL_LONGITUDE, longitude);

        return sqldb.insert(LOCATIONS_TABLE_NAME, null, cv) > 0;
    }

    public boolean deleteLocation(String address) {
        SQLiteDatabase sqldb = this.getWritableDatabase();

        return sqldb.delete(LOCATIONS_TABLE_NAME, LOCATIONS_COL_ADDRESS + "=?", new String[]{address}) > 0;
    }

    public boolean updateLocation(String address, double latitude, double longitude) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LOCATIONS_COL_ADDRESS, address);
        cv.put(LOCATIONS_COL_LATITUDE, latitude);
        cv.put(LOCATIONS_COL_LONGITUDE, longitude);

        return sqldb.update(LOCATIONS_TABLE_NAME, cv, LOCATIONS_COL_ADDRESS + "=?", new String[]{address}) > 0;
    }

    public Cursor searchAddress(String searchable) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + LOCATIONS_TABLE_NAME + " where address LIKE '%" + searchable +"%'";

        Cursor cursor = null;
        if(db != null ){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    // Returns all notes in db
    public Cursor getLocations(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + LOCATIONS_TABLE_NAME;

        Cursor cursor = null;
        if(db != null ){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    @SuppressLint("Range")
    public int getLocationId(double latitude, double longitude) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ROWID, * FROM " + LOCATIONS_TABLE_NAME + " WHERE latitude = '" + latitude + "' AND longitude = '" + longitude +"';";

        Cursor res = db.rawQuery(query, null);
        int id=-1;
        if(res!=null&&res.moveToFirst()) {
            id = res.getInt(res.getColumnIndex("id"));
        }
        return id;
    }
}
