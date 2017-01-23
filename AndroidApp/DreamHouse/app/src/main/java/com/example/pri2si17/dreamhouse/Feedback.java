package com.example.pri2si17.dreamhouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class Feedback extends AppCompatActivity {

    AppCompatButton submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbackform);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        submit=(AppCompatButton)findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Feedback.this,"Your response has been recorded. Thankyou.",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(Feedback.this,UserSearch.class);
                startActivity(intent);
            }
        });
    }
}
