package com.andrewsosa.beacon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Jeffrey Kuang on 3/21/2015.
 */
public class BeaconDataSource {

    // Database fields
    private SQLiteDatabase database;
    private BeaconDataHelper dbHelper;

    // Column
    private String[] allColumns = {
            BeaconDataHelper.COLUMN_ID,
            BeaconDataHelper.COLUMN_LATITUDE,
            BeaconDataHelper.COLUMN_LONGITUDE,
            BeaconDataHelper.COLUMN_NAME
    };

    public BeaconDataSource(Context context) {
        dbHelper = new BeaconDataHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Beacon add_beacon(long Long, long lat, String name) {

        // Pack tuple values
        ContentValues values = new ContentValues();
        values.put(BeaconDataHelper.COLUMN_NAME, name);
        values.put(BeaconDataHelper.COLUMN_LONGITUDE, Long);
        values.put(BeaconDataHelper.COLUMN_LATITUDE, lat);

        // Do insert, get _id
        Long insertId = database.insert(BeaconDataHelper.TABLE_BEACONS, null, values);

        // Reread the tuple
        Cursor cursor = database.query(BeaconDataHelper.TABLE_BEACONS,
                allColumns, BeaconDataHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        // Convert tuple to java object
        cursor.moveToFirst();
        Beacon newList = cursor_to_beacon(cursor);
        cursor.close();
        return newList;
    }

    public void update_beacon(Beacon beacon) {
        long id = beacon.get_id();

        ContentValues values = new ContentValues();
        values.put(BeaconDataHelper.COLUMN_NAME, beacon.getName());

        database.update(BeaconDataHelper.TABLE_BEACONS,
                values, BeaconDataHelper.COLUMN_ID + " = " + id, null);
    }

    public void delete_beacon(Beacon beacon) {
        long id = beacon.get_id();
        database.delete(BeaconDataHelper.TABLE_BEACONS, BeaconDataHelper.COLUMN_ID
            + " = " + id, null);
    }

    public Beacon get_beacon(long id) {

        // Read the tuple
        Cursor cursor = database.query(BeaconDataHelper.TABLE_BEACONS,
                allColumns, BeaconDataHelper.COLUMN_ID + " = " + id, null,
                null, null, null);

        // Convert tuple to java object
        cursor.moveToFirst();
        Beacon newBeacon = cursor_to_beacon(cursor);
        cursor.close();
        return newBeacon;
    }

    public ArrayList<Beacon> get_all_beacon() {
        ArrayList<Beacon> beacons = new ArrayList<Beacon>();

        Cursor c = database.query(BeaconDataHelper.TABLE_BEACONS, allColumns,
                null, null, null, null, null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            Beacon b = cursor_to_beacon(c);
            beacons.add(b);
            c.moveToNext();
        }

        c.close();
        return beacons;
    }

    public Cursor get_cursor() {
        return database.query(BeaconDataHelper.TABLE_BEACONS, allColumns,
                null, null, null, null, null);
    }

    private Beacon cursor_to_beacon(Cursor c) {
        return new Beacon(c.getLong(0), c.getLong(1), c.getLong(2), c.getString(3));
    }

    //TODO general search all fields for beacon
    public Beacon search_beacon(String statement ){
        Cursor cursor = database.query(BeaconDataHelper.TABLE_BEACONS,
                allColumns, BeaconDataHelper.COLUMN_NAME + " = " + statement, null,
                null, null, null);
        Beacon result = cursor_to_beacon(cursor);
        return result;
    }
}
