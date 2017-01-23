package com.example.pri2si17.dreamhouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.vision.text.Line;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Redirect_otp extends AppCompatActivity {

    private TextView otp;
    private TextView click;
    private TextView resend_otp;
    private AppCompatButton submit;
    private String user_email;
    private String user_id;
    private String user_hash;
    private ProgressDialog hash_verification;
    private EditText user_otp;
    ServerResponse resp;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect_otp);
        user_email = getIntent().getStringExtra("USER_EMAIL");
        user_id = getIntent().getStringExtra("USER_UNIQUE_ID");
        user_hash = getIntent().getStringExtra("USER_REGISTERED_HASH");
        Toast.makeText(Redirect_otp.this, user_email, Toast.LENGTH_LONG).show();
        Toast.makeText(Redirect_otp.this, user_id, Toast.LENGTH_LONG).show();
        Toast.makeText(Redirect_otp.this, user_hash, Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        otp = (TextView) findViewById(R.id.textviewotp);
        otp.setClickable(true);
        otp.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "Enter otp";
        otp.setText(text);
        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout otp_panel = (LinearLayout) findViewById(R.id.otp);
                otp_panel.setVisibility(View.VISIBLE);
                user_otp = (EditText) findViewById(R.id.user_otp);
                /*String OTP = user_otp.getText().toString();
                getOTPVerified(OTP);*/
            }
        });

        click = (TextView) findViewById(R.id.textviewlink);
        click.setClickable(true);
        click.setMovementMethod(LinkMovementMethod.getInstance());
        String text2 = "Click Here";
        click.setText(text2);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHashVerified();
            }
        });

        resend_otp = (TextView) findViewById(R.id.id_resend_otp);
        resend_otp.setClickable(true);
        resend_otp.setMovementMethod(LinkMovementMethod.getInstance());
        String text3 = "Resend otp";
        resend_otp.setText(text3);

        submit = (AppCompatButton) findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Redirect_otp.this, ProfileTab.class);
                startActivity(intent);*/
                String OTP = user_otp.getText().toString();
                if(!OTP.isEmpty())
                {
                    getOTPVerified(OTP);
                }
                else
                {
                    //Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(Redirect_otp.this, "Fields are empty", Toast.LENGTH_LONG).show();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Redirect_otp Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private void getHashVerified()
    {
        hash_verification = new ProgressDialog(Redirect_otp.this);
        hash_verification.setMessage("Loading...");
        hash_verification.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        hash_verification.setIndeterminate(true);
        hash_verification.show();
        Retrofit registerRequest = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = registerRequest.create(RequestInterface.class);
        User user = new User();
        user.setEmail(user_email);
        user.setUser_hash(user_hash);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.HASH_OPERATION);
        request.setUser(user);

        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                resp = response.body();
                //Snackbar.make(, resp.getMessage(), Snackbar.LENGTH_LONG).show();
                Toast.makeText(Redirect_otp.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    redirectToProfile();
                }
                //regProgress.setVisibility(View.INVISIBLE);
                hash_verification.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                //regProgress.setVisibility(View.INVISIBLE);
                hash_verification.dismiss();
                Log.d(Constants.TAG, "failed");
                //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                Toast.makeText(Redirect_otp.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void getOTPVerified(String OTP)
    {
        hash_verification = new ProgressDialog(Redirect_otp.this);
        hash_verification.setMessage("Loading...");
        hash_verification.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        hash_verification.setIndeterminate(true);
        hash_verification.show();
        Retrofit registerRequest = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = registerRequest.create(RequestInterface.class);
        User user = new User();
        user.setEmail(user_email);
        user.setOTP(OTP);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.OTP_OPERATION);
        request.setUser(user);

        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                resp = response.body();
                //Snackbar.make(, resp.getMessage(), Snackbar.LENGTH_LONG).show();
                Toast.makeText(Redirect_otp.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    redirectToProfile();
                }
                //regProgress.setVisibility(View.INVISIBLE);
                hash_verification.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                //regProgress.setVisibility(View.INVISIBLE);
                hash_verification.dismiss();
                Log.d(Constants.TAG, "failed");
                //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                Toast.makeText(Redirect_otp.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }
    private void redirectToProfile()
    {
        Intent intent = new Intent(Redirect_otp.this, ProfileTab.class);
        intent.putExtra("SUCCESS_MESSAGE", resp.getMessage());
        intent.putExtra("USER_EMAIL", user_email);
        intent.putExtra("USER_UNIQUE_ID", user_id);
        startActivity(intent);
    }
}
