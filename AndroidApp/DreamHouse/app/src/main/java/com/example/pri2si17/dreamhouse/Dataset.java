package com.example.pri2si17.dreamhouse;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by Saumya Joshi on 12/2/2016.
 */

public class Dataset implements Serializable{

    Context context;
    String ProjectName;
    String BuilderName;
    int price;
    double b_rating;
    double l_rating;
    String type;
    int bhk;
    double latitude;
    double longitude;

    public Dataset(Context context, String projectName, String builderName, int price, double b_rating, double l_rating, String type, int bhk, double latitude, double longitude) {
        this.context = context;
        ProjectName = projectName;
        BuilderName = builderName;
        this.price = price;
        this.b_rating = b_rating;
        this.l_rating = l_rating;
        this.type = type;
        this.bhk = bhk;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void create() throws Exception
    {
        DbHelper dbHelper=new DbHelper(context,"DreamHouse",null,1);
        dbHelper.createFlats(this);
    }

    public String getProjectName() {
        return ProjectName;
    }

    public String getBuilderName() {
        return BuilderName;
    }

    public int getPrice() {
        return price;
    }

    public double getB_rating() {
        return b_rating;
    }

    public double getL_rating() {
        return l_rating;
    }

    public String getType() {
        return type;
    }

    public int getBhk() {
        return bhk;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
