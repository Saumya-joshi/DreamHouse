package com.example.pri2si17.dreamhouse;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Saumya Joshi on 12/1/2016.
 */

public class ListActivity extends AppCompatActivity  {

    ArrayList<DataModel> dataModels;
    ListView listView,sortView;
    private AlertDialog dialog;
    private static CustomAdapter adapter;
    private TextView loc_rating, builder_rating,lo2hi,hi2lo;
    ArrayList<Dataset> result= new ArrayList<Dataset>();
    String lat,lng,radius;
    ArrayList<Dataset> sortData;
    ArrayList<DataModel> sortDataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lat=getIntent().getStringExtra("lat");
        lng=getIntent().getStringExtra("lng");
        radius=getIntent().getStringExtra("rad");
        ArrayList<Integer> integerArrayList;
//        integerArrayList=getIntent().getIntegerArrayListExtra("ïndices");
        integerArrayList=(ArrayList<Integer>)getIntent().getSerializableExtra("arraylist");

        listView=(ListView)findViewById(R.id.list);
        dataModels= new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("DreamHouse", MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("select * from flats", null);
        sortData=new ArrayList<>();

        for(int i=0;i<integerArrayList.size();i++)
        {
            cursor.moveToPosition(integerArrayList.get(i));
            dataModels.add(new DataModel(cursor.getString(0), cursor.getString(1),Integer.parseInt(cursor.getString(2)),Double.valueOf( cursor.getString(3)),Double.valueOf(cursor.getString(4)),Integer.parseInt(cursor.getString(5)),cursor.getString(6)));
            Dataset d1=new Dataset(ListActivity.this,cursor.getString(0), cursor.getString(1),Integer.parseInt(cursor.getString(2)),Double.valueOf( cursor.getString(3)),Double.valueOf(cursor.getString(4)),(cursor.getString(6)),Integer.parseInt(cursor.getString(5)),Double.valueOf(cursor.getString(7)),Double.valueOf(cursor.getString(8)));
            sortData.add(d1);
        }


        adapter= new CustomAdapter(dataModels,this);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataModel dataModel= dataModels.get(position);

                Snackbar.make(view, dataModel.getPName()+"\n"+"Builder "+dataModel.getBName()+dataModel.getPrice(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                Uri uri= Uri.parse("http://www.99acres.com/search/property/buy/residential-all/noida?search_type=QS&search_location=HP&lstAcn=HP_R&src=CLUSTER&preference=S&selected_tab=1&city=7&res_com=R&property_type=R&isvoicesearch=N&keyword_suggest=noida%3B&fullSelectedSuggestions=noida&strEntityMap=W3sidHlwZSI6ImNpdHkifSx7IjEiOlsibm9pZGEiLCJDSVRZXzcsIFBSRUZFUkVOQ0VfUywgUkVTQ09NX1IiXX1d&texttypedtillsuggestion=noida&refine_results=Y&Refine_Localities=Refine%20Localities&action=%2Fdo%2Fquicksearch%2Fsearch&suggestion=CITY_7%2C%20PREFERENCE_S%2C%20RESCOM_R&searchform=1&price_min=null&price_max=null");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_option1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            sortFlats();
        }
        if(id==R.id.action_map)
        {
            Intent intent= new Intent(ListActivity.this, MapsActivity.class);
            intent.putExtra("Latitude",lat);
            intent.putExtra("Longitude",lng);
            intent.putExtra("Range",radius);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void sortFlats()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
        LayoutInflater inflater = ListActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.sort_popup, null);

        loc_rating=(TextView)view.findViewById(R.id.loc_rating);
        builder_rating=(TextView)view.findViewById(R.id.builder_rating);
        lo2hi=(TextView)view.findViewById(R.id.pricel2h);
        hi2lo=(TextView)view.findViewById(R.id.priceh2l);

        loc_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sort by locality rating
                Collections.sort(sortData, new Comparator<Dataset>() {
                    @Override
                    public int compare(Dataset d1, Dataset d2) {
                        if (d1.l_rating > d2.l_rating)
                            return 1;
                        else if (d1.l_rating < d2.l_rating)
                            return -1;
                        else
                            return 0;
                    }
                });

                sortView=(ListView)findViewById(R.id.list);
                sortDataModel= new ArrayList<DataModel>();
                for(int i=sortData.size()-1;i>=0;i--)
                {
                    sortDataModel.add(new DataModel(sortData.get(i).getProjectName(), sortData.get(i).getBuilderName(),sortData.get(i).getPrice(),sortData.get(i).getB_rating(),sortData.get(i).getL_rating(),sortData.get(i).getBhk(),sortData.get(i).getType()));
                }


                adapter= new CustomAdapter(sortDataModel,ListActivity.this);
                dialog.dismiss();
                listView.setAdapter(adapter);

            }
        });

        builder_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sort by builder rating
                Collections.sort(sortData, new Comparator<Dataset>() {
                    @Override
                    public int compare(Dataset d1, Dataset d2) {
                        if (d1.b_rating > d2.b_rating)
                            return 1;
                        else if (d1.b_rating < d2.b_rating)
                            return -1;
                        else
                            return 0;
                    }
                });

                sortView=(ListView)findViewById(R.id.list);
                sortDataModel= new ArrayList<DataModel>();
                for(int i=sortData.size()-1;i>=0;i--)
                {
                    sortDataModel.add(new DataModel(sortData.get(i).getProjectName(), sortData.get(i).getBuilderName(),sortData.get(i).getPrice(),sortData.get(i).getB_rating(),sortData.get(i).getL_rating(),sortData.get(i).getBhk(),sortData.get(i).getType()));
                }


                adapter= new CustomAdapter(sortDataModel,ListActivity.this);
                dialog.dismiss();
                listView.setAdapter(adapter);
            }
        });



        lo2hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sort by low to high price
                Collections.sort(sortData, new Comparator<Dataset>() {
                    @Override
                    public int compare(Dataset d1, Dataset d2) {
                        if (d1.price > d2.price)
                            return 1;
                        else if (d1.price < d2.price)
                            return -1;
                        else
                            return 0;
                    }
                });

                sortView=(ListView)findViewById(R.id.list);
                sortDataModel= new ArrayList<DataModel>();
                for(int i=0;i<sortData.size();i++)
                {
                    sortDataModel.add(new DataModel(sortData.get(i).getProjectName(), sortData.get(i).getBuilderName(),sortData.get(i).getPrice(),sortData.get(i).getB_rating(),sortData.get(i).getL_rating(),sortData.get(i).getBhk(),sortData.get(i).getType()));
                }


                adapter= new CustomAdapter(sortDataModel,ListActivity.this);
                dialog.dismiss();
                listView.setAdapter(adapter);
            }
        });


        hi2lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sort by high to low price
                Collections.sort(sortData, new Comparator<Dataset>() {
                    @Override
                    public int compare(Dataset d1, Dataset d2) {
                        if (d1.price > d2.price)
                            return 1;
                        else if (d1.price < d2.price)
                            return -1;
                        else
                            return 0;
                    }
                });

                sortView=(ListView)findViewById(R.id.list);
                sortDataModel= new ArrayList<DataModel>();
                for(int i=sortData.size()-1;i>=0;i--)
                {
                    sortDataModel.add(new DataModel(sortData.get(i).getProjectName(), sortData.get(i).getBuilderName(),sortData.get(i).getPrice(),sortData.get(i).getB_rating(),sortData.get(i).getL_rating(),sortData.get(i).getBhk(),sortData.get(i).getType()));
                }


                adapter= new CustomAdapter(sortDataModel,ListActivity.this);
                dialog.dismiss();
                listView.setAdapter(adapter);
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();


    }


}