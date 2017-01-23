package com.example.pri2si17.dreamhouse;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Saumya Joshi on 12/5/2016.
 */

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table if not exists flats(projectname varchar(50),buildername varchar(50),price varchar(50)," +
                "lrating varchar(50),brating varchar(50),bhk varchar(10),type varchar(50),lat varchar(15),lng varchar(15));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createFlats(Dataset dataset) throws Exception
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("projectname",dataset.getProjectName());
        contentValues.put("buildername",dataset.getBuilderName());
        contentValues.put("price",dataset.getPrice());
        contentValues.put("lrating",dataset.getL_rating());
        contentValues.put("brating",dataset.getB_rating());
        contentValues.put("bhk",dataset.getBhk());
        contentValues.put("type",dataset.getType());
        contentValues.put("lat",dataset.getLatitude());
        contentValues.put("lng",dataset.getLongitude());
        db.insert("flats",null,contentValues);
    }
}
