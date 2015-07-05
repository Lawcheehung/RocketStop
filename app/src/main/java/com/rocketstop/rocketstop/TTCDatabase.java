package com.rocketstop.rocketstop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

//Code adapted from: http://developer.android.com/training/basics/data-storage/databases.html

public class TTCDatabase extends SQLiteOpenHelper{

    public static final class TTCDatabaseContract {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public TTCDatabaseContract() {}

        /* Inner class that defines the table contents */
        /*MasterStops is a table that contains ALL stops and it's related data such as the route it belongs to, the lat and lon, etc.*/
        public static abstract class FeedEntry implements BaseColumns {
            public static final String TABLE_NAME = "MasterStops";
            public static final String COLUMN_NAME_ID = "entryid";
            public static final String COLUMN_NAME_ROUTENUM = "routenum";
            public static final String COLUMN_NAME_LAT = "lat";
            public static final String COLUMN_NAME_LON = "lon";
        }
    }

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TTCDatabase.db";

    public TTCDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
