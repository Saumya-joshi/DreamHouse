package com.example.pri2si17.dreamhouse;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    TextView forgotpw, logout;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        forgotpw=(TextView)findViewById(R.id.forgotpw);
        logout=(TextView)findViewById(R.id.logout);

        forgotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                LayoutInflater inflater = Settings.this.getLayoutInflater();
                View view = inflater.inflate(R.layout.popup_forgot_password, null);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logout. Session over
                Toast.makeText(Settings.this,"You have been logged out",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
