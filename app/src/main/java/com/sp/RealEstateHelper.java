package com.sp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RealEstateHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "realestatelist.db";
    private static final int SCHEMA_NAME = 1;

    public RealEstateHelper(Context context){
        super(context, DATABASE_NAME, null, SCHEMA_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //Will be called once the database is not created
        db.execSQL("CREATE TABLE realestates_table ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " realestatePrice TEXT, realestateAddress TEXT, realestateType TEXT, realestateSize TEXT, realestateAgent TEXT," +
                " realestateStatus TEXT, lat REAL, lon REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Will not be called until SCHME_VERSION increases
        //Here we can upgrade the database e.g. add more tables
    }

    //Read all records from realestate_table
    public Cursor getAll(){
        return (getReadableDatabase().rawQuery(
                "SELECT _id, realestatePrice, realestateAddress, realestateType, realestateSize," +
                        " realestateAgent, realestateStatus, lat, lon FROM realestates_table ORDER BY realestateAddress", null));
    }

    //Read a particular record from realestates_table
    public Cursor getById(String id){
        String[] args = {id};

        return (getReadableDatabase().rawQuery(
                "SELECT _id, realestatePrice, realestateAddress, realestateType, realestateSize," +
                        " realestateAgent, realestateStatus, lat, lon FROM realestates_table WHERE _ID = ?", args));
    }

    //Write a record into realestates_table
    public void  insert(String realestatePrice, String realestateAddress,
                        String realestateType, String realestateSize, String realestateAgent, String realestateStatus,
                        double lat, double lon){
        ContentValues cv = new ContentValues();

        cv.put("RealEstatePrice", realestatePrice);
        cv.put("RealEstateAddress", realestateAddress);
        cv.put("RealEstateType", realestateType);
        cv.put("RealEstateSize", realestateSize);
        cv.put("RealEstateAgent", realestateAgent);
        cv.put("RealEstateStatus", realestateStatus);
//        cv.put("RealEstateImage", realestateImage);
        cv.put("lat", lat);
        cv.put("lon", lon);

        getWritableDatabase().insert("realestates_table", "realestateAddress", cv);
    }

    //Update a particular record in realestates_table with id provided
    public void update(String id, String realestatePrice, String realestateAddress,
                       String realestateType, String realestateSize, String realestateAgent, String realestateStatus,
                       double lat, double lon){
        ContentValues cv = new ContentValues();
        String[] args = {id};
        cv.put("realestatePrice", realestatePrice);
        cv.put("realestateAddress", realestateAddress);
        cv.put("RealEstateType", realestateType);
        cv.put("RealEstateSize", realestateSize);
        cv.put("RealEstateAgent", realestateAgent);
        cv.put("realestateStatus", realestateStatus);
//        cv.put("realestateImage", realestateImage);
        cv.put("lat", lat);
        cv.put("lon", lon);

        getWritableDatabase().update("realestates_table", cv, " _ID = ?", args);
    }

    //Read a record id value from realestates_table
    public String getID(Cursor c) {return (c.getString(0));}

    public String getRealEstatePrice(Cursor c){ return (c.getString(1)); }

    public String getRealEstateAddress(Cursor c){ return (c.getString(2)); }

    public String getRealEstateType(Cursor c){ return (c.getString(3)); }

    public String getRealEstateSize(Cursor c){ return (c.getString(4)); }

    public String getRealEstateAgent(Cursor c){
        return (c.getString(5));
    }

    public String getRealEstateStatus(Cursor c){
        return (c.getString(6));
    }

//    public byte[] getRealestateImage(Cursor c){ return (c.getBlob(6)); }

    public double getLatitude(Cursor c){ return (c.getDouble(7)); }

    public double getLongitude(Cursor c){ return (c.getDouble(8)); }
}