package com.example.pri2si17.dreamhouse;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

//Implementing the interface OnTabSelectedListener to our MainActivity
//This interface would help in swiping views

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

//Implementing the interface OnTabSelectedListener to our MainActivity
//This interface would help in swiping views
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    //This is our tablayout
    private TabLayout tabLayout;

    Handler handler=new Handler();
    //This is our viewPager
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DbHelper dbHelper=new DbHelper(this,"DreamHouse",null,1);
        dbHelper.onCreate(openOrCreateDatabase("DreamHouse",MODE_PRIVATE,null));
        new DataTask().execute();
        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("LOGIN"));
        tabLayout.addTab(tabLayout.newTab().setText("REGISTER"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    class DataTask extends AsyncTask<String,String,String>
    {
        Dataset data[]=new Dataset[36];
        @Override
        protected String doInBackground(String... params) {
            try {

                data[0] = new Dataset(MainActivity.this, "Intellicity", "Airwil", 5000000, 3.5, 2.0, "Flat", 2, 28.40, 80);
                data[1] = new Dataset(MainActivity.this, "AIRWIL", "Airwil", 5000000, 3.5, 2.0, "Flat", 2, 28.53, 77.39);
                data[2] = new Dataset(MainActivity.this, "Alpine Vistula", "Alpine Housing", 3000000, 3.0, 3.45, "Villa", 5, 28.5, 80);
                data[3] = new Dataset(MainActivity.this, "Alpine Housing Farms", "Alpine Housing", 2000000, 4.5, 3.45, "Flat", 4, 28.23, 79);
                data[4] = new Dataset(MainActivity.this, "Silicon City", "Amrapali", 7000, 2.5, 1.74, "Paying Guest", 2, 28.2, 80.2);
                data[5] = new Dataset(MainActivity.this, "Trident Embassy", "Arsh Group", 80000000, 3.0, 3.08, "Bungalow", 7, 27.69, 79.5);
                data[6] = new Dataset(MainActivity.this, "Celeste Towers", "Assotech", 300000, 3.5, 1.67, "Flat", 3, 28.40, 79.1);
                data[7] = new Dataset(MainActivity.this, "Cresterra", "Assotech", 4000000, 3.5, 1.67, "Flat", 2, 28.40, 80.5);
                data[8] = new Dataset(MainActivity.this, "Grand Stand", "ATS", 25000, 2.0, 3.0, "Paying Guest", 4, 28.5, 77.4);
                data[9] = new Dataset(MainActivity.this, "ATS Greens Village", "ATS", 5000, 3.5, 3.0, "Rent", 2, 28.43, 77.2);
                data[10] = new Dataset(MainActivity.this, "Annexe", "Dasnac", 3000, 3.0, 4.47, "Rent", 2, 27.99, 76.99);
                data[11] = new Dataset(MainActivity.this, "The jewel Of Noida", "Dasnac", 20000000, 4.5, 4.47, "Villa", 7, 28, 77.6);
                data[12] = new Dataset(MainActivity.this, "Devika Gold Homz", "Devika", 5000000, 3.5, 3.47, "Flat", 2, 27.53, 77.39);
                data[13] = new Dataset(MainActivity.this, "Great Value", "Great Value", 7000, 2.5, 1.0, "Paying Guest", 2, 28.01, 78.23);
                data[14] = new Dataset(MainActivity.this, "Dream Residency", "Jain Group", 80000, 3.0, 1.33, "Flat", 2, 28.1, 77.5);
                data[15] = new Dataset(MainActivity.this, "Jaypee Greens", "Jaypee Greens", 300000, 3.5, 2.5, "Flat", 3, 28.48, 78.1);
                data[16] = new Dataset(MainActivity.this, "Kingswood Oriental Villas", "Jaypee Greens", 4000000, 3.5, 2.5, "Villa", 4, 27.940,78.2);
                data[17] = new Dataset(MainActivity.this, "Logix Zest", "Logix", 25000, 2.0, 3.41, "Paying Guest", 4, 28.56, 77.1);
                data[18] = new Dataset(MainActivity.this, "Logix Blossom", "Logix", 5000000, 3.5, 3.41, "Flat", 2, 28.43, 77.3);
                data[19] = new Dataset(MainActivity.this, "Logix Blossom Greens", "Logix", 3000000, 3.0, 3.41, "Villa", 2, 28, 78.5);
                data[20] = new Dataset(MainActivity.this, "Lotus Greens", "Lotus Greens", 2000000, 4.5, 3.3, "Flat", 3, 28.61, 77.36);
                data[21] = new Dataset(MainActivity.this, "Mahagun Moderne", "Mahagun", 5000000, 3.5, 1.75, "Bungalow", 5, 28.63, 77.39);
                data[22] = new Dataset(MainActivity.this, "Mahagun Mirabella", "Mahagun", 7000, 2.5, 1.75, "Paying Guest", 2, 28.60, 77.3);
                data[23] = new Dataset(MainActivity.this, "Mapsko Royal Towers", "Mapsko", 40000000, 3.0, 4.8, "Villa", 2, 28.45, 77.2);
                data[24] = new Dataset(MainActivity.this, "National Employee Housing Society", "National Employee Housing Society", 300000, 3.5, 2.51, "Flat", 3, 28.40, 77.2);
                data[25] = new Dataset(MainActivity.this, "Omaxe Grand", "Omaxe", 4000, 3.5, 3.25, "Rent", 2, 27.94, 77.5);
                data[26] = new Dataset(MainActivity.this, "Curio City", "Orris", 25000, 2.0, 2.0, "Paying Guest", 3, 28.85, 77.9);
                data[27] = new Dataset(MainActivity.this, "Greenbay Golf Homes", "Orris", 5000, 3.5, 2.0, "Rent", 1, 28.56, 76.5);
                data[28] = new Dataset(MainActivity.this, "Panchsheel Pratishtha", "Panchsheel", 3000000, 3.0, 2.19, "Villa", 5, 28.36, 77.2);
                data[29] = new Dataset(MainActivity.this, "Parasect", "Paras", 2000000, 4.5, 3.0, "Flat", 4, 28.5, 78.2);
                data[30] = new Dataset(MainActivity.this, "Parsvnath Gardenia", "Parsvnath", 5500000, 3.5, 3.0, "Bungalow", 2, 28.83, 77.39);
                data[31] = new Dataset(MainActivity.this, "Neotown", "Patel", 7000, 2.5, 2.18, "Paying Guest", 2, 27.46, 80);
                data[32] = new Dataset(MainActivity.this, "Reputed Hosing", "Reputed Builder", 120000, 3.0, 2.31, "Flat", 2, 27.96, 81.2);
                data[33] = new Dataset(MainActivity.this, "Shiv Shakti Apartments", "Shakti", 300000, 3.5, 2.17, "Rent", 3, 28.85, 76.95);
                data[34] = new Dataset(MainActivity.this, "Matrott", "SkyTech Group", 4000000, 3.5,1.67, "Villa", 2, 28.84, 78.36);
                data[35] = new Dataset(MainActivity.this, "Ridge Residency", "Today Homes", 2500, 2.0, 1.0, "Paying Guest", 1, 28.5, 77.2);

                for (int i = 0; i < 36; i++) {
                    data[i].create();
                }
            }
            catch (final Exception e)
            {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}