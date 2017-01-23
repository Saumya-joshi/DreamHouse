package com.example.pri2si17.dreamhouse;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saumya Joshi on 11/16/2016.
 */
public class Step1 extends Fragment implements View.OnClickListener{

    private AppCompatButton nextProfile;
    private EditText fName;
    private EditText mName;
    private EditText lName;
    private RadioGroup radioSex;
    private RadioButton gender;
    private EditText usrBirthday;
    private EditText usrPhone;
    private ProgressDialog profile_progress;
    private String user_email;
    private String user_id;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        user_email = getActivity().getIntent().getStringExtra("USER_EMAIL");
        user_id = getActivity().getIntent().getStringExtra("USER_UNIQUE_ID");
        //Snackbar.make(getView(), user_id, Snackbar.LENGTH_LONG).show();
        View view = inflater.inflate(R.layout.step1_profile, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view)
    {
        nextProfile = (AppCompatButton) view.findViewById(R.id.btn_step2);
        fName = (EditText) view.findViewById(R.id.input_first_name);
        mName = (EditText) view.findViewById(R.id.input_middle_name);
        lName = (EditText) view.findViewById(R.id.input_last_name);
        usrBirthday = (EditText) view.findViewById(R.id.input_dob);
        /*myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dayOfMonth,monthOfYear, year);
            }

        };

        usrBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(getActivity(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });*/

        usrPhone = (EditText) view.findViewById(R.id.input_mobile);
        radioSex = (RadioGroup) view.findViewById(R.id.radioSex);
        nextProfile.setOnClickListener(this);
    }

    public void onClick(View V)
    {
        switch (V.getId()) {
            case R.id.btn_step2:
                int selectedSex = radioSex.getCheckedRadioButtonId();
                gender = (RadioButton) getActivity().findViewById(selectedSex);
                String usr_first_name = fName.getText().toString();
                String usr_middle_name = mName.getText().toString();
                String usr_last_name = lName.getText().toString();
                String usr_date_of_birth = usrBirthday.getText().toString();
                String usr_mobile_number = usrPhone.getText().toString();
                String usr_sex = gender.getText().toString();
                if(!usr_first_name.isEmpty() && !usr_last_name.isEmpty() && !usr_date_of_birth.isEmpty() && !usr_mobile_number.isEmpty() && !usr_sex.isEmpty())
                {
                    savePersonalProfile(usr_first_name, usr_middle_name, usr_last_name, usr_date_of_birth, usr_mobile_number, usr_sex);
                   // goToResidentSection();
                }
                else
                {
                    Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void savePersonalProfile(String fName, String mName, String lName, String dob, String mobile, String gender)
    {
        profile_progress = new ProgressDialog(getActivity());
        profile_progress.setMessage("Loading...");
        profile_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        profile_progress.setIndeterminate(true);
        profile_progress.show();
        Retrofit savePersonalProfileRequest = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = savePersonalProfileRequest.create(RequestInterface.class);
        User user = new User();
        user.setEmail(user_email);
        user.setUnique_id(user_id);
        user.setFirstName(fName);
        user.setLastName(lName);
        user.setMiddleName(mName);
        user.setDateOfBirth(dob);
        user.setMobileNumber(mobile);
        user.setSex(gender);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.SAVE_PERSONAL_INFO);
        request.setUser(user);

        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    goToResidentSection();
                }
                //regProgress.setVisibility(View.INVISIBLE);
                profile_progress.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                //regProgress.setVisibility(View.INVISIBLE);
                profile_progress.dismiss();
                Log.d(Constants.TAG, "failed");
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void goToResidentSection()
    {
        Fragment step2 = new Step2();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, step2);
        ft.commit();
    }
    /*private void updateLabel(int dayOfMonth, int monthOfYear, int year) {

        usrBirthday.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(dayOfMonth).append("/").append(monthOfYear+1).append("/").append(year).append(" "));
    }*/

}