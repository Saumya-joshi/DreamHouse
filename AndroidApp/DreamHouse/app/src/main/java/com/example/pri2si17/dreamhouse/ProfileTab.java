package com.example.pri2si17.dreamhouse;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;

public class ProfileTab extends AppCompatActivity {

    private AppCompatButton next;
    private String user_email;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user_email = getIntent().getStringExtra("USER_EMAIL");
        user_id = getIntent().getStringExtra("USER_UNIQUE_ID");

        setContentView(R.layout.activity_profile_tab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFragment();

    }

    private void initFragment(){
        Fragment fragment;
        fragment= new Step3();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,fragment);
        ft.commit();
    }
}
