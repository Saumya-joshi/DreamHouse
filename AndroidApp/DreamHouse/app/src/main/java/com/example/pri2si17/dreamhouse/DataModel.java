package com.example.pri2si17.dreamhouse;

/**
 * Created by Saumya Joshi on 12/1/2016.
 */

public class DataModel {

    String project_name;
    String builder_name;
    int Price;
    double builder_rating;
    String type;
    double locality_rating;
    int bhk;

    public DataModel(String project_name, String builder_name, int Price, double b_rating, double l_rating, int bhk,String type ) {
        this.project_name=project_name;
        this.builder_name=builder_name;
        this.Price=Price;
        this.type=type;
        this.builder_rating=b_rating;
        this.locality_rating=l_rating;
        this.bhk=bhk;

    }

    public String getPName() {
        return project_name;
    }

    public String getBName() {
        return builder_name;
    }

    public int getPrice() {
        return Price;
    }

    public double getLRating (){ return locality_rating; }

    public double getBRating(){return  builder_rating; }

    public int getBhk(){ return bhk;}

    public String getType() {   return type; }
}
