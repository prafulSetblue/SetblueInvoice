package com.setblue.invoice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonMethods;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.MySessionManager;
import com.setblue.invoice.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView back;
    private Intent i;
    private String st_username;
    private String st_email;
    private Button send;
    private MySessionManager sessionManager;
    private AQuery aq;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_forgotpassword);
            aq =new AQuery(this);
            setUpActionBar();
            init();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    private void setUpActionBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        back = (ImageView) tb.findViewById(R.id.iv_back);
        back.setOnClickListener(this);

    }
    private void init(){
        sessionManager = new MySessionManager(this);
        send = (Button)findViewById(R.id.btn_forgot);
        send.setOnClickListener(this);;
        email = (EditText)findViewById(R.id.edt_email);
    }
    @Override
    public void onClick(View v) {
        if(v == back){
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        }
        else if(v == send){
            st_email = email.getText().toString().trim();
            if(Validation.isEmptyEdittext(email)){
                email.setError("Enter Email Address");
            }
            else if(!Validation.isValidEmail(st_email)){
                email.setError("Enter Valid Email Address");

            }
            else {

                if(CommonMethods.knowInternetOn(this)){
                    Send();
                }
                else {
                    CommonMethods.showInternetAlert(this);
                }
               // sessionManager.setUserId("1");

                /*i = new Intent(this, MainActivity.class);
                finish();
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);*/

            }
        }
    }

    private void Send() {
        String url = Apis.ForgotPassword+"email="+st_email;
        Log.d(CommonVariables.TAG,url);
        //Make Asynchronous call using AJAX method
        aq.progress(new ProgressDialog(this,R.style.CustomProgressDialog)).ajax(url, String.class, this,"jsonCallback");

    }

    public void jsonCallback(String url, String json, AjaxStatus status){

        if(json != null){
            //successful ajax call
            Log.d(CommonVariables.TAG,json.toString());
            try {
                JSONObject object = new JSONObject(json);
                if(object.optInt("resid")>0){
                    Toast.makeText(this,"Successfully sent email",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this,"Your Email address is not registered.",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            //ajax error
            Log.d(CommonVariables.TAG,status.toString());
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!CommonMethods.knowInternetOn(this)){
            CommonMethods.showInternetAlert(this);
        }
    }
}
