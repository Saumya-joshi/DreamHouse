package com.example.pri2si17.dreamhouse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.design.widget.Snackbar;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterTab extends Fragment implements View.OnClickListener{

    private AppCompatButton btnRegisterUsr;
    private EditText usrEmail;
    private EditText usrName;
    private EditText usrPwd;
    //private ProgressBar regProgress;
    private TextView alreadyRegistered;
    private TabLayout tabLayout;
    private SharedPreferences pref;
    private ProgressDialog register_progress;
    ServerResponse resp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view)
    {
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayout);
        btnRegisterUsr = (AppCompatButton) view.findViewById(R.id.btn_register);
        usrEmail = (EditText) view.findViewById(R.id.et_email);
        usrName = (EditText) view.findViewById(R.id.et_name);
        usrPwd = (EditText) view.findViewById(R.id.et_password);
        //regProgress = (ProgressBar) view.findViewById(R.id.progress);
        alreadyRegistered = (TextView) view.findViewById(R.id.tv_regLogin);
        btnRegisterUsr.setOnClickListener(this);
        alreadyRegistered.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_regLogin:
                getLogin();
                break;

            case R.id.btn_register:
                String name = usrName.getText().toString();
                String email = usrEmail.getText().toString();
                String password = usrPwd.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty())
                {
                    //regProgress.setVisibility(View.VISIBLE);
                    getUserRegistered(name, email, password);
                }
                else
                {
                    Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void getUserRegistered(String name, String email, String password)
    {
        register_progress = new ProgressDialog(getActivity());
        register_progress.setMessage("Loading...");
        register_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        register_progress.setIndeterminate(true);
        register_progress.show();
        Retrofit registerRequest = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = registerRequest.create(RequestInterface.class);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REGISTER_OPERATION);
        request.setUser(user);

        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                resp = response.body();
                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    redirectToOTP();
                }
                //regProgress.setVisibility(View.INVISIBLE);
                register_progress.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                //regProgress.setVisibility(View.INVISIBLE);
                register_progress.dismiss();
                Log.d(Constants.TAG, "failed");
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }

        });
    }


    private void getLogin()
    {
        //Go to login.
        tabLayout.getTabAt(0).select();
    }

    private void redirectToOTP()
    {
        Intent intent = new Intent(getActivity(), Redirect_otp.class);
        intent.putExtra("USER_EMAIL", resp.getUser().getEmail());
        intent.putExtra("USER_UNIQUE_ID", resp.getUser().getUnique_id());
        intent.putExtra("USER_REGISTERED_HASH", resp.getUser().getUser_hash());
        startActivity(intent);
    }
}