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
import com.androidquery.AbstractAQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonMethods;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.ExceptionHandler;
import com.setblue.invoice.utils.MySessionManager;
import com.setblue.invoice.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView back;
    private TextView signup;
    private Intent i;
    private EditText et_username;
    private EditText et_password;
    private String st_username;
    private String st_password;
    private Button login;
    private MySessionManager sessionManager;
    private AQuery aq;
    private TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_login);
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
        login = (Button)findViewById(R.id.btn_login);
        login.setOnClickListener(this);
        forgot = (TextView)findViewById(R.id.tv_forget);
        forgot.setOnClickListener(this);
        signup = (TextView)findViewById(R.id.lbl_signup);
        signup.setOnClickListener(this);
        et_username = (EditText)findViewById(R.id.edt_username);
        et_password = (EditText)findViewById(R.id.edt_password);
    }
    @Override
    public void onClick(View v) {
        if(v == back){
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        }
        else if(v == forgot){
            i = new Intent(this, ForgotPasswordActivity.class);
            //finish();
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        else if(v == signup){
            i = new Intent(this, SignupActivity.class);
            //finish();
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        else if(v == login){
            st_username = et_username.getText().toString().trim();
            st_password = et_password.getText().toString().trim();
            if(Validation.isEmptyEdittext(et_username) && Validation.isEmptyEdittext(et_password)){
                et_username.setError("Enter Username");
                et_password.setError("Enter Password");
            }
            else if(Validation.isEmptyEdittext(et_username) ){
                et_username.setError("Enter Username");

            }
            else if( Validation.isEmptyEdittext(et_password)){
                et_password.setError("Enter Password");
            }
            else if(!Validation.isValidEmail(st_username)){
                et_username.setError("Enter Valid Email Id");
            }
            else {

                if(CommonMethods.knowInternetOn(this)){
                    Login();
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

    private void Login() {
        String url = Apis.Login+"email="+st_username+"&"+"password="+st_password;

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
                    Toast.makeText(this,"Successfully Login",Toast.LENGTH_LONG).show();
                    sessionManager.setUserId(object.optString("AdminId"));
                    i = new Intent(this, MainActivity.class);
                    finish();
                    startActivity(i);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                else {
                    Toast.makeText(this,"Your credentials are incorrect.",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            //ajax error
            Log.d(CommonVariables.TAG,status.toString());
        }
    }
}
