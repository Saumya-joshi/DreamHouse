package com.example.pri2si17.dreamhouse;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Saumya Joshi on 11/16/2016.
 */
public class Step3 extends Fragment {

    private ImageButton fb, google;
    private String user_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        user_id = getActivity().getIntent().getStringExtra("USER_UNIQUE_ID");
        View view= inflater.inflate(R.layout.step3_profile, container, false);
        final AppCompatButton next =
                (AppCompatButton) view.findViewById(R.id.btn_cont3);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),UserSearch.class);
                startActivity(intent);
            }
        });


        final AppCompatButton back =
                (AppCompatButton) view.findViewById(R.id.btn_back2);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment step2 = new Step2();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_frame,step2);
                ft.commit();
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri= Uri.parse("http://192.168.43.220/minor/FacebookLogin/index.php?userId="+user_id);
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri= Uri.parse("http://192.168.43.220/minor/GoogleLogin");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        return view;
    }
}

