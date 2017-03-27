package com.setblue.invoice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import com.setblue.invoice.utils.ExceptionHandler;
import com.setblue.invoice.utils.MySessionManager;
import com.setblue.invoice.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView back;
    private TextView signin;
    Intent i;
    private EditText et_password;
    private Button signup;
    private EditText et_companyName;
    private EditText et_email;
    private String st_companyName;
    private String st_Email;
    private String st_password;
    private EditText et_mobile;
    private String st_Mobile;
    private AQuery aq;
    private MySessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_signup);
            aq = new AQuery(this);
            sessionManager = new MySessionManager(this);
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

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button

        back = (ImageView) tb.findViewById(R.id.iv_back);
        back.setOnClickListener(this);

    }
    private void init(){
        signup = (Button)findViewById(R.id.btn_signup);
        signup.setOnClickListener(this);
        signin = (TextView)findViewById(R.id.lbl_signin);
        signin.setOnClickListener(this);
        et_companyName = (EditText)findViewById(R.id.edt_company);
        et_email = (EditText)findViewById(R.id.edt_email);
        et_mobile = (EditText)findViewById(R.id.edt_mobile);
        et_password = (EditText)findViewById(R.id.edt_password);


    }

    @Override
    public void onClick(View v) {
        if(v == back){
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        }
        else if(v == signup){
            st_companyName = et_companyName.getText().toString().trim();
            st_Email = et_email.getText().toString().trim();
            st_Mobile = et_mobile.getText().toString().trim();
            st_password = et_password.getText().toString().trim();
            if(Validation.isEmptyEdittext(et_companyName) && Validation.isEmptyEdittext(et_password) && Validation.isEmptyEdittext(et_email) && Validation.isEmptyEdittext(et_mobile)){
                et_companyName.setError("Enter Company Name");
                et_email.setError("Enter Email Address");
                et_password.setError("Enter Password");
                et_mobile.setError("Enter Password");
            }
            else if(Validation.isEmptyEdittext(et_email)){
                et_email.setError("Enter Email Address");
            }
            else if(Validation.isEmptyEdittext(et_mobile)){
                et_mobile.setError("Enter Mobile");
            }

            else if(Validation.isEmptyEdittext(et_password)){
                et_password.setError("Enter Password");
            }
            else if(st_Mobile.length() <= 6){
                et_mobile.setError("Enter Valid Mobile");
            }
            else if(!Validation.isValidEmail(st_Email)){
                et_email.setError("Enter Valid Email Id");
            }
            else {
                if(CommonMethods.knowInternetOn(this)){
                    Register();
                }
                else {
                    CommonMethods.showInternetAlert(this);
                }
            }
        }
        else if(v == signin){
            i = new Intent(this, LoginActivity.class);
            finish();
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);


        }
    }


    private void Register() {
        String url = Apis.Register+"Name="+st_companyName+"&"+"email="+st_Email+"&"+"password="+st_password+"&"+"mobile="+st_Mobile;

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
                    Toast.makeText(this,"Successfully Register",Toast.LENGTH_LONG).show();
                    sessionManager.setUserId(object.optString("AdminId"));
                    i = new Intent(this, MainActivity.class);
                    finish();
                    startActivity(i);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                else {
                    Toast.makeText(this,object.optString("res"),Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            //ajax error
            Log.d(CommonVariables.TAG,""+status.getCode());
        }
    }

}
