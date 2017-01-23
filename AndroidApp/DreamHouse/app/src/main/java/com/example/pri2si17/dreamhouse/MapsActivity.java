package com.example.pri2si17.dreamhouse;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.IntegerRes;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pri2si17.dreamhouse.Dataset;
import com.example.pri2si17.dreamhouse.Friend;
import com.example.pri2si17.dreamhouse.GeoLocation;
import com.example.pri2si17.dreamhouse.GeoLocationDemo;
import com.example.pri2si17.dreamhouse.ListActivity;
import com.example.pri2si17.dreamhouse.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String Latitude, Longitude,Range;
    private AlertDialog dialog;
    int bhk=0,range=0;
    private Spinner spinner;
    private SeekBar seekbar;
    private CheckBox ch1, ch2, ch3, ch4,ch5;
    boolean bungalow=false, villa=false, flat=false, rent=false, pg=false;
    ArrayList<Dataset> result= new ArrayList<Dataset>();
    Cursor cursor;
    ArrayList<Integer> integerArrayList=new ArrayList<>();
    Friend[] data= new Friend[15];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Latitude=getIntent().getStringExtra("Latitude");
        Longitude=getIntent().getStringExtra("Longitude");
        Range=getIntent().getStringExtra("Range");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;

            LatLng place = new LatLng(Double.parseDouble(Latitude), Double.parseDouble(Longitude));
            mMap.addMarker(new MarkerOptions().position(place).title("Selected Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mMap.addCircle(new CircleOptions().center(place).radius(Double.parseDouble(Range) * 1000).strokeWidth(2).strokeColor(Color.BLUE).fillColor(Color.parseColor("#50ffd700")));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 11));


            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("DreamHouse", MODE_PRIVATE, null);
            cursor = sqLiteDatabase.rawQuery("select * from flats", null);
            if (cursor.moveToFirst()) {
                do {
                    if (GeoLocationDemo.findPlacesWithinDistance(6371.01, GeoLocation.fromDegrees(place.latitude, place.longitude), GeoLocation.fromDegrees(Double.valueOf(cursor.getString(7)),Double.valueOf( cursor.getString(8))), Double.parseDouble(Range))) {
                        LatLng lkw = new LatLng(Double.valueOf(cursor.getString(7)),Double.valueOf( cursor.getString(8)));
                        mMap.addMarker(new MarkerOptions().position(lkw).title(cursor.getString(0)));
                        integerArrayList.add(cursor.getPosition());
                    }
                } while (cursor.moveToNext());
            } else
                Toast.makeText(this, "No recs", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            setFilter();
        }

        if (id == R.id.action_list) {
            Intent intent= new Intent(MapsActivity.this, ListActivity.class);
            intent.putExtra("lat",Latitude);
            intent.putExtra("lng",Longitude);
            intent.putExtra("rad",Range);
            intent.putExtra("arraylist",integerArrayList);
//            intent.putIntegerArrayListExtra("indices",integerArrayList);
            startActivity(intent);
        }

        if (id == R.id.action_findFriends) {
            showFriends();
        }


        return super.onOptionsItemSelected(item);
    }

    public void setFilter()
    {
        bungalow=false;
        villa=false;
        flat=false;
        rent=false;
        pg=false;
        range=20000000;
        bhk=1;

        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        LayoutInflater inflater = MapsActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_popup, null);

        spinner= (Spinner) view.findViewById(R.id.bhk_spinner);
        seekbar=(SeekBar)view.findViewById(R.id.range_seekbar);

        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.bhk_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bhk= Integer.parseInt(spinner.getSelectedItem().toString());
                Toast.makeText(MapsActivity.this,bhk+ "",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bhk=1;
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                range=progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MapsActivity.this, range+" ",Toast.LENGTH_SHORT ).show();
            }
        });

        ch1=(CheckBox)view.findViewById(R.id.bungalow);
        ch2=(CheckBox)view.findViewById(R.id.villa);
        ch3=(CheckBox)view.findViewById(R.id.flat);
        ch4=(CheckBox)view.findViewById(R.id.rent);
        ch5=(CheckBox)view.findViewById(R.id.paying_guest);
        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    bungalow=true;
                else
                    bungalow=false;

            }
        });
        ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    villa=true;
                else
                    villa=false;

            }
        });
        ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    flat=true;
                else
                    flat=false;

            }
        });
        ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    rent=true;
                else
                    rent=false;

            }
        });

        ch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    pg=true;

                }
                else
                {
                    pg=false;

                }

            }
        });



        AppCompatButton done= (AppCompatButton)view.findViewById(R.id.btn_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set filters
                ArrayList<Integer> filtered=new ArrayList<Integer>();
                for(int i=0;i<integerArrayList.size();i++)
                {
                    cursor.moveToPosition(integerArrayList.get(i));
                    if(cursor.getString(5).equals(bhk+"") && Integer.parseInt(cursor.getString(2))<=range
                            && ((pg && cursor.getString(6).equals("Paying Guest")) || (villa && cursor.getString(6).equals("Villa")) ||
                            (flat && cursor.getString(6).equals("Flat")) || (bungalow && cursor.getString(6).equals("Bungalow")) ||
                            (rent && cursor.getString(6).equals("Rent"))))
                    {
                        filtered.add(cursor.getPosition());
                    }
                }

                Intent intent= new Intent(MapsActivity.this, ListActivity.class);
                intent.putExtra("lat",Latitude);
                intent.putExtra("lng",Longitude);
                intent.putExtra("rad",Range);
                intent.putExtra("arraylist",filtered);
//            intent.putIntegerArrayListExtra("indices",integerArrayList);
                startActivity(intent);

            }
        });


        builder.setView(view);
        dialog = builder.create();
        dialog.show();



    }


    public void showFriends() {
        data[0] = new Friend("Priyank Jain", "1", 28.53, 77.3);
        data[1] = new Friend("Priyanshu Sinha", "2", 28.63, 77.23);
        data[2] = new Friend("Saumya Joshi", "3", 28.3, 77);
        data[3] = new Friend("Sonalika Sinha", "4", 28.52, 77.13);
        data[4] = new Friend("Yasha Singh", "5", 28.57, 77.25);
        data[5] = new Friend("Aditi Saxena", "6", 27.956, 77.5);
        data[6] = new Friend("Paras Arora", "7", 28, 76.96);
        data[7] = new Friend("Vaanishri Singh", "8", 28.61, 77.24);
        data[8] = new Friend("Vardaan Magon", "9", 28.49, 77.53);
        mMap.clear();
        LatLng place = new LatLng(Double.parseDouble(Latitude), Double.parseDouble(Longitude));
        mMap.addMarker(new MarkerOptions().position(place).title("Selected Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.addCircle(new CircleOptions().center(place).radius(Double.parseDouble(Range) * 1000).strokeWidth(2).strokeColor(Color.BLUE).fillColor(Color.parseColor("#50ffd700")));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 11));

        for (int i = 0; i < 9; i++) {

            LatLng friend = new LatLng(data[i].getLatitude(), data[i].getLongitude());
            mMap.addMarker(new MarkerOptions().position(friend).title(data[i].getFriendName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        }
    }
}