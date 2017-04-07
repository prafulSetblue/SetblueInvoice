package com.setblue.invoice;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.setblue.invoice.Fragments.CustomerFragment;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonMethods;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.ExceptionHandler;
import com.setblue.invoice.utils.MySessionManager;
import com.setblue.invoice.utils.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EditClientActivity extends AppCompatActivity implements View.OnClickListener {


    public ImageView back;
    public ImageView editClient;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private TextView clientName;
    private TextView companyName;
    private EditText email;
    private EditText mobile;
    private EditText address;
    AQuery aq;
    private EditText et_fname;
    private EditText et_lname;
    private EditText et_company;
    private EditText et_mobile;
    private EditText et_email;
    private EditText et_address;
    private EditText et_city;
    private EditText et_state;
    private EditText et_country;
    private EditText et_zip;
    private Button bt_save;
    private String stFname;
    private String stLname;
    private String stCompany;
    private String stMobile;
    private String stEmail;
    private String stAddress;
    private String stCity;
    private String stState;
    private String stCountry;
    private String stZip;
    MySessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_cutomer);
            aq = new AQuery(this);
            session = new MySessionManager(this);
            setUpActionBar();
            init();
            setData();
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
        et_fname = (EditText)findViewById(R.id.edt_fname);
        et_lname = (EditText)findViewById(R.id.edt_lname);
        et_company = (EditText)findViewById(R.id.edt_company);
        et_mobile = (EditText)findViewById(R.id.edt_mobile);
        et_email = (EditText)findViewById(R.id.edt_email);
        et_address = (EditText)findViewById(R.id.edt_address);
        et_city = (EditText)findViewById(R.id.edt_city);
        et_state = (EditText)findViewById(R.id.edt_state);
        et_country = (EditText)findViewById(R.id.edt_country);
        et_zip = (EditText)findViewById(R.id.edt_zip);
        bt_save = (Button)findViewById(R.id.btn_save);
        bt_save.setOnClickListener(this);


    }
    private void setData(){

    }
    @Override
    public void onClick(View v) {
        if(v == back){
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }
        if(v == bt_save) {
            stFname = et_fname.getText().toString().trim();
            stLname = et_lname.getText().toString().trim();
            stCompany = et_company.getText().toString().trim();
            stMobile = et_mobile.getText().toString().trim();
            stEmail = et_email.getText().toString().trim();
            stAddress = et_address.getText().toString().trim();
            stCity = et_city.getText().toString().trim();
            stState = et_state.getText().toString().trim();
            stCountry = et_country.getText().toString().trim();
            stZip = et_zip.getText().toString().trim();
            if(Validation.isEmptyEdittext(et_fname) && Validation.isEmptyEdittext(et_lname)&& Validation.isEmptyEdittext(et_email)){
                et_fname.setError("Enter First Name");
                et_lname.setError("Enter Last Name");
                et_email.setError("Enter Email");
            }
            else if(Validation.isEmptyEdittext(et_fname)){
                et_fname.setError("Enter First Name");
            }
            else if(Validation.isEmptyEdittext(et_lname)){
                et_lname.setError("Enter Last Name");
            }
            else {
                if(CommonMethods.knowInternetOn(this)){
                    UpdateClient();
                }
                else {
                    CommonMethods.showInternetAlert(this);
                }
            }
            //  i = new Intent(getActivity(), ClientDetailActivity.class);
            //getActivity().finish();
            //startActivity(i);
        }
    }
    public void  clientDetail(){
        String url = Apis.ClientDetails+"id="+getIntent().getIntExtra("id",0);
        Log.d(CommonVariables.TAG,"Url: "+url);
        aq.progress(new ProgressBar(this)).ajax(url, String.class, this,"jsonCallback");

    }
    public void  UpdateClient(){
        String url = Apis.UpdateClient+"FirstName="+stFname+"&LastName="+stLname+"&email="+stEmail+"&Company="+stCompany+"&mobile="+stMobile+"&address="+stAddress+"&pincode="+stZip+"&city="+stCity+"&state="+stState+"&Country="+stCountry+"&AdminId="+session.getUserId()+"&id="+getIntent().getIntExtra("id",0);
        //Make Asynchronous call using AJAX method
        Log.d(CommonVariables.TAG,url);
        aq.progress(new ProgressDialog(this,R.style.CustomProgressDialog)).ajax(url, String.class, this,"jsonCallback");


    }
    public void jsonCallback(String url, String json, AjaxStatus status){

        if(json != null){
            //successful ajax call
            Log.d(CommonVariables.TAG,json.toString());
            try {
                JSONObject object = new JSONObject(json);

                    if(object.optString("api").equalsIgnoreCase("ClientDetails")) {
                        if(object.optInt("resid")>0) {
                            JSONArray jsonArray = object.optJSONArray("resData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.optJSONObject(i);
                                et_fname.setText(c.optString("FirstName"));
                                et_lname.setText(c.optString("LastName"));
                                et_company.setText(c.optString("Company"));
                                et_mobile.setText(c.optString("Mobile"));
                                et_email.setText(c.optString("Email"));
                                et_address.setText(c.optString("Address"));
                                et_city.setText(c.optString("City"));
                                et_state.setText(c.optString("State"));
                                et_country.setText(c.optString("Country"));
                                et_zip.setText(c.optString("Pincode"));

                            }
                        }else {
                            Toast.makeText(this,"Something Wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(object.optString("api").equalsIgnoreCase("UpdateClient")) {
                        if(object.optInt("resid")>0) {
                            finish();
                        }
                        else {
                            Toast.makeText(this,"Something Wrong",Toast.LENGTH_SHORT).show();
                        }

                    }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            //ajax error
            Log.d(CommonVariables.TAG,""+status.getCode());
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!CommonMethods.knowInternetOn(this)){
            CommonMethods.showInternetAlert(this);
        }
        else {

                clientDetail();

        }
    }
}
