package com.example.flightadmin.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseMng extends SQLiteOpenHelper {

    public static final int DB_VERSION = 6;
    public static final String DB_NAME = "FlightAdmin";


    public static final String TABLE_USER = "user";

    public static final String USER_COL_LOGIN = "_id";

    public static final String USER_COL_PASSWD = "passwd";


    public static final String TABLE_FLIGHT = "flight";

    public static final String FLIGHT_COL_DESTINATION = "_id";
    public static final String FLIGHT_COL_AIRLINE = "airline";
    public static final String FLIGHT_COL_DATE = "date";
    public static final String FLIGHT_COL_TIME = "time";
    public static final String FLIGHT_COL_ORIGIN = "origin";
    public static final String FLIGHT_COL_LOGIN = "login";

    public DatabaseMng(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DatabaseMng", "Creating DB" + DB_NAME + " v" + DB_VERSION);
        try{
            db.beginTransaction();
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER + "( " + USER_COL_LOGIN + " string(255) PRIMARY KEY NOT NULL, " + USER_COL_PASSWD + " string(255) NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FLIGHT + "( "
                    + FLIGHT_COL_DESTINATION + " string(255) NOT NULL, "
                    + FLIGHT_COL_AIRLINE + " string(255) NOT NULL, "
                    + FLIGHT_COL_DATE + " string(255) NOT NULL, "
                    + FLIGHT_COL_TIME + " string(255) NOT NULL, "
                    + FLIGHT_COL_ORIGIN + " string(255) NOT NULL, "
                    + FLIGHT_COL_LOGIN + " string(255) NOT NULL, "
                    + "PRIMARY KEY (" + FLIGHT_COL_DESTINATION + " ," + FLIGHT_COL_LOGIN + "))");
            db.setTransactionSuccessful();
        }catch(SQLException exc){
            Log.e("DatabaseMng.onCreate", exc.getMessage());
        }finally{
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DatabaseMng",
                "DB: " +  DB_NAME + ": v " +  oldVersion + " -> v" + newVersion);
        try{
            db.beginTransaction();
            db.execSQL("DROP TABLE IF EXISTS "+  TABLE_USER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLIGHT);
            db.setTransactionSuccessful();
        }catch(SQLException exc){
            Log.e("DatabaseMng.onUpgrade", exc.getMessage());
        }finally{
            db.endTransaction();
        }
        this.onCreate(db);
    }



    public Cursor getFlightsAirline(String airline, String login){
        return this.getReadableDatabase().query(TABLE_FLIGHT,
                null, FLIGHT_COL_AIRLINE + "=? AND " + FLIGHT_COL_LOGIN + "=?", new String[]{airline, login}, null, null, null);
    }

    public Cursor getFlights(String login){
        return this.getReadableDatabase().query(TABLE_FLIGHT,
                null, FLIGHT_COL_LOGIN + "=?", new String[]{login}, null, null, null);
    }

    public boolean addFlight(String destination, String airline, String date, String time, String origin, String login){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = null;
        boolean toret = false;

        values.put(FLIGHT_COL_DESTINATION, destination);
        values.put(FLIGHT_COL_AIRLINE, airline);
        values.put(FLIGHT_COL_DATE, date);
        values.put(FLIGHT_COL_TIME, time);
        values.put(FLIGHT_COL_ORIGIN, origin);
        values.put(FLIGHT_COL_LOGIN, login);


        try{
            db.beginTransaction();
            cursor = db.query(TABLE_FLIGHT,
                    null,
                    FLIGHT_COL_DESTINATION + "=? AND " +  FLIGHT_COL_LOGIN + "=?",
                    new String[]{destination, login},
                    null, null, null, null);
            if(cursor.getCount() > 0){
                db.update(TABLE_FLIGHT,
                        values, FLIGHT_COL_DESTINATION + "=? AND " + FLIGHT_COL_LOGIN + "=?", new String[]{destination, login});
            }else{
                db.insert(TABLE_FLIGHT, null, values);
            }

            db.setTransactionSuccessful();
            toret = true;
        }catch(SQLException exc){
            Log.e("DatabaseMng.addFlight", exc.getMessage());
        }finally{
            if(cursor != null) {
                cursor.close();
            }
            db.endTransaction();
        }
        return toret;
    }

    public boolean deleteFlight(String id, String login){
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            db.beginTransaction();
            db.delete(TABLE_FLIGHT, FLIGHT_COL_DESTINATION + "=? AND " +  FLIGHT_COL_LOGIN + "=?", new String[]{id, login});
            db.setTransactionSuccessful();
            toret = true;
        }catch(SQLException exc){
            Log.e("DBManager.deleteFlight", exc.getMessage());
        }finally {
            db.endTransaction();
        }

        return toret;
    }

    public boolean addUser(String login, String passwd){
        Cursor cursor = null;
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_COL_LOGIN, login);
        values.put(USER_COL_PASSWD, passwd);

        try{
            db.beginTransaction();
            cursor = db.query(TABLE_USER,
                    null,
                    USER_COL_LOGIN + "=?",
                    new String[]{login},
                    null, null, null, null);
            if(cursor.getCount() == 0){
                //En caso de que el usuario no exista
                db.insert(TABLE_USER, null, values);
                db.setTransactionSuccessful();
                toret = true;
            }

        }catch(SQLException exc){
            Log.e("DBManager.addUser", exc.getMessage());
        }finally{
            if(cursor != null){
                cursor.close();
            }
            db.endTransaction();
        }

        return toret;
    }

    public boolean checkLogin(String login, String passwd){
        Cursor cursor = null;
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            db.beginTransaction();

            cursor = db.query(TABLE_USER,
                    null,
                    USER_COL_LOGIN + "=? AND " + USER_COL_PASSWD + "=?",
                    new String[]{login, passwd},
                    null, null, null, null);
            if(cursor.getCount() > 0){
                toret = true;
            }

            db.setTransactionSuccessful();
        }catch(SQLException exc){
            Log.e("DBManager.checkLogin", exc.getMessage());
        }finally{
            if(cursor != null){
                cursor.close();
            }
            db.endTransaction();
        }

        return toret;
    }
}