package com.example.pri2si17.dreamhouse;


import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class UserSearch extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {

    private GoogleMap mMap;
    Place loc;
    List<android.location.Address> list;
    EditText range;
    AppCompatButton srch;
    public static boolean isMyLocationSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_user_search);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map1);
            mapFragment.getMapAsync(this);

            range=(EditText) findViewById(R.id.user_range);
            srch=(AppCompatButton) findViewById(R.id.btn_search);
            final Geocoder gc=new Geocoder(this);

            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);



            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    mMap.clear();
                    loc=place;
                    LatLng place1 = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    mMap.addMarker(new MarkerOptions().position(place1).title((String) place.getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place1,11));
                    Log.i("TAG", "Place: " + place.getName());

                    FrameLayout map = (FrameLayout) findViewById(R.id.map_panel);
                    map.setVisibility(View.VISIBLE);

                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i("TAG", "An error occurred: " + status);
                }
            });



            srch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (loc.toString().isEmpty() || range.getText().toString().isEmpty()) {

                        Toast.makeText(UserSearch.this, "Please fill the fields",Toast.LENGTH_SHORT).show();

                    } else {
                        Intent intent = new Intent(UserSearch.this, MapsActivity.class);
                        intent.putExtra("Latitude", loc.getLatLng().latitude + "");
                        intent.putExtra("Longitude", loc.getLatLng().longitude + "");
                        intent.putExtra("Range", range.getText().toString());
                        startActivity(intent);
                    }
                }
            });
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent= new Intent(UserSearch.this, Settings.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            Intent intent= new Intent(UserSearch.this, ProfileTab.class);
            startActivity(intent);

        } else if (id == R.id.nav_buy) {

            Intent intent=getIntent();
            overridePendingTransition(0,0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0,0);
            startActivity(intent);


        } else if (id == R.id.nav_rent) {

            Intent intent=getIntent();
            overridePendingTransition(0,0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0,0);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            Intent intent= new Intent(UserSearch.this, Settings.class);
            startActivity(intent);

        } else if (id == R.id.nav_feedback) {

            Intent intent= new Intent(UserSearch.this, Feedback.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}