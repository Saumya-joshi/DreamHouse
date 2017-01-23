package com.example.pri2si17.dreamhouse;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Saumya Joshi on 11/16/2016.
 */
public class Step2 extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View view= inflater.inflate(R.layout.step2_profile, container, false);
        final AppCompatButton next =
                (AppCompatButton) view.findViewById(R.id.btn_cont2);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment step3 = new Step3();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_frame,step3);
                ft.commit();
            }
        });

        final AppCompatButton back =
                (AppCompatButton) view.findViewById(R.id.btn_back1);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment step1 = new Step1();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_frame,step1);
                ft.commit();
            }
        });

        return view;
    }
}
