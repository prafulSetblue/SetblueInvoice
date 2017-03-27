package com.setblue.invoice.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.ClientDetailActivity;
import com.setblue.invoice.ClientListActivity;
import com.setblue.invoice.MainActivity;
import com.setblue.invoice.R;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonMethods;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.MySessionManager;
import com.setblue.invoice.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by praful on 01-Feb-17.
 */

public class CustomerFragment extends Fragment implements View.OnClickListener {

    private Button bt_save;
    Intent i;
    MySessionManager session;
    private EditText et_fname;
    private EditText et_lname;
    private EditText et_company;
    private EditText et_mobile;
    private EditText et_address;
    private EditText et_city;
    private EditText et_state;
    private EditText et_country;
    private EditText et_zip;
    private String stFname;
    private String stLname;
    private String stCompany;
    private String stMobile;
    private String stAddress;
    private String stCity;
    private String stState;
    private String stCountry;
    private String stZip;
    private EditText et_email;
    private String stEmail;
    AQuery aq;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cutomer, container, false);
        session = new MySessionManager(getActivity());
        aq = new AQuery(getActivity());
        init(view);

        return view;

    }

    private void init(View v) {
        et_fname = (EditText)v.findViewById(R.id.edt_fname);
        et_lname = (EditText)v.findViewById(R.id.edt_lname);
        et_company = (EditText)v.findViewById(R.id.edt_company);
        et_mobile = (EditText)v.findViewById(R.id.edt_mobile);
        et_email = (EditText)v.findViewById(R.id.edt_email);
        et_address = (EditText)v.findViewById(R.id.edt_address);
        et_city = (EditText)v.findViewById(R.id.edt_city);
        et_state = (EditText)v.findViewById(R.id.edt_state);
        et_country = (EditText)v.findViewById(R.id.edt_country);
        et_zip = (EditText)v.findViewById(R.id.edt_zip);
        bt_save = (Button)v.findViewById(R.id.btn_save);
        bt_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
            else if(Validation.isEmptyEdittext(et_email)){
                et_email.setError("Enter Email Address");
            }
            else {
                    if(CommonMethods.knowInternetOn((AppCompatActivity) getActivity())){
                        AddClient();
                    }
                else {
                        CommonMethods.showInternetAlert((AppCompatActivity) getActivity());
                    }
            }
          //  i = new Intent(getActivity(), ClientDetailActivity.class);
            //getActivity().finish();
            //startActivity(i);
        }
    }

    private void AddClient() {
        String url = Apis.AddClient+"FirstName="+stFname+"&LastName="+stLname+"&email="+stEmail+"&Company="+stCompany+"&mobile="+stMobile+"&address="+stAddress+"&pincode="+stZip+"&city="+stCity+"&state="+stState+"&Country="+stCountry+"&AdminId="+session.getUserId();
        //Make Asynchronous call using AJAX method
        aq.progress(new ProgressDialog(getActivity(),R.style.CustomProgressDialog)).ajax(url, String.class, this,"jsonCallback");

    }
    public void jsonCallback(String url, String json, AjaxStatus status){

        if(json != null){
            //successful ajax call
            Log.d(CommonVariables.TAG,json.toString());
            try {
                JSONObject object = new JSONObject(json);
                if(object.optInt("resid")>0){
                    Toast.makeText(getActivity(),object.optString("res"),Toast.LENGTH_LONG).show();
                    i = new Intent(getActivity(), ClientDetailActivity.class);
                    //getActivity().finish();
                    startActivity(i);
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                else {
                    Toast.makeText(getActivity(),object.optString("res"),Toast.LENGTH_LONG).show();
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
