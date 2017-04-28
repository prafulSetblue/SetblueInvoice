package com.setblue.invoice;

import android.content.Intent;
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
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.Fragments.InvoiceFragment;
import com.setblue.invoice.components.CatLoadingView;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonMethods;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.ExceptionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CompanyDetailActivity extends AppCompatActivity implements View.OnClickListener {


    private AQuery aq;
    private ImageView back;
    private Button Company;
    private ImageView editCompany;
    private CatLoadingView mView;
    private TextView title;
    private TextView companyName;
    private EditText email;
    private EditText mobile;
    private EditText address;
    private EditText taxNo;
    private EditText panNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_company_detail);
            aq = new AQuery(this);
            setUpActionBar();
            init();
            setData();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setUpActionBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        title = (TextView) tb.findViewById(R.id.tv_title);
        back = (ImageView) tb.findViewById(R.id.iv_back);
        back.setOnClickListener(this);
        editCompany = (ImageView) tb.findViewById(R.id.iv_edit);
        editCompany.setOnClickListener(this);

    }

    private void init() {
        Company = (Button) findViewById(R.id.iv_company);
        Company.setOnClickListener(this);
        companyName = (TextView) findViewById(R.id.tv_company_name);
        email = (EditText) findViewById(R.id.edt_email);
        mobile = (EditText) findViewById(R.id.edt_mobile);
        address = (EditText) findViewById(R.id.edt_address);
        taxNo = (EditText) findViewById(R.id.edt_taxno);
        panNo = (EditText) findViewById(R.id.edt_pan);
    }

    private void setData() {

    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        } else if (v == editCompany) {

            Intent i = new Intent(this, CreateCompanyActivity.class);
            // i.putExtra("id", clientID);
            startActivity(i);

        } else if (v == Company) {
            Intent i = new Intent(this, CreateCompanyActivity.class);
            // i.putExtra("id", clientID);
            i.putExtra("from", "CompanyDetail");
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void CompanyDetail() {
        mView = new CatLoadingView();
        mView.show((this).getSupportFragmentManager(), "load");
        String url = Apis.CompanyDetail + "id=" + getIntent().getIntExtra("id", 0);
        Log.d(CommonVariables.TAG, "Url: " + url);
        aq.ajax(url, String.class, this, "jsonCallback");

    }

    public void jsonCallback(String url, String json, AjaxStatus status) {

        if (mView != null)
            mView.dismiss();


        if (json != null) {
            //successful ajax call
            Log.d(CommonVariables.TAG, json.toString());
            try {
                JSONObject object = new JSONObject(json);
                if (object.optString("Name").equalsIgnoreCase("CompanyDetail")) {
                    if (object.optInt("resid") > 0) {


                        JSONObject c = object.optJSONObject("resData");
                        String Address = c.optString("AddressLine1") + "," + c.optString("AddressLine2") + "," + c.optString("City");
                        companyName.setText(c.optString("CompanyName"));
                        email.setText(c.optString("EmailId"));
                        mobile.setText(c.optString("MobileNo"));
                        address.setText(Address);
                        taxNo.setText(c.optString("ServiceTaxNo"));
                        panNo.setText(c.optString("PANNumber"));

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            //ajax error
            Log.d(CommonVariables.TAG, "" + status.getCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.setText("Company Details");
        if (!CommonMethods.knowInternetOn(this)) {
            CommonMethods.showInternetAlert(this);
        } else {
            CompanyDetail();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);*/
    }
}
