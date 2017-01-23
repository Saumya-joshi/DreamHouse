package com.example.pri2si17.dreamhouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saumya Joshi on 11/5/2016.
 */

public class LoginTab extends Fragment implements View.OnClickListener {

    //Overriden method onCreateView
    private AppCompatButton btnLogin;
    private EditText loginEmail;
    private EditText loginPwd;
    private TextView newUsr;
    private TextView frgotPwd;
    private ProgressBar loginProgress;
    private TabLayout tabLayout;
    private EditText et_userInput;
    private AppCompatButton generateMailOtp;
    private ProgressBar progressFrgtPwd;
    private AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.login, container, false);
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayout);
        btnLogin = (AppCompatButton) view.findViewById(R.id.btn_login);
        loginEmail = (EditText) view.findViewById(R.id.et_email);
        loginPwd = (EditText) view.findViewById(R.id.et_password);
        newUsr = (TextView) view.findViewById(R.id.newUSER);
        frgotPwd = (TextView) view.findViewById(R.id.frgtPwd);
        loginProgress = (ProgressBar) view.findViewById(R.id.progress);

        btnLogin.setOnClickListener(this);
        frgotPwd.setOnClickListener(this);
        newUsr.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.newUSER:
                goToRegister();
                break;

            case R.id.frgtPwd:
                openModal();
                break;

            case R.id.btn_login:
                String loginId = loginEmail.getText().toString();
                String loginPassword = loginPwd.getText().toString();
                if(!loginId.isEmpty() && !loginPassword.isEmpty())
                {
                    getuserLoggedIn(loginId, loginPassword);
                }
                else
                {
                    Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }
                break;

            default:
                return;
        }
    }

    private void goToRegister()
    {
        tabLayout.getTabAt(1).select();
    }

    private void getuserLoggedIn(String loginId, String loginPwd)
    {
        Retrofit registerRequest = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = registerRequest.create(RequestInterface.class);

        User user = new User();
        user.setEmail(loginId);
        user.setPassword(loginPwd);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.LOGIN_OPERATION);
        request.setUser(user);

        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    goToUserSearch();
                }
                loginProgress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                loginProgress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG, "failed");
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void openModal()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_forgot_password, null);
        et_userInput = (EditText) view.findViewById(R.id.input_email_phone);
        generateMailOtp = (AppCompatButton) view.findViewById(R.id.btn_send_verification_code_mail);
        progressFrgtPwd = (ProgressBar) view.findViewById(R.id.progress_forgot_pwd);
        builder.setView(view);
        builder.setTitle("Forgot Password");
        dialog = builder.create();
        dialog.show();

        generateMailOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usrIp = et_userInput.getText().toString();
                if(!usrIp.isEmpty())
                    sendMail(usrIp);
                else
                    Snackbar.make(getView(), "Field is empty !", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void sendMail(String usr_mailId)
    {
        Snackbar.make(getView(), "Function Called !", Snackbar.LENGTH_LONG).show();
    }

    private void goToUserSearch()
    {
        Intent intent = new Intent(getActivity(), UserSearch.class);
        startActivity(intent);
    }
}
