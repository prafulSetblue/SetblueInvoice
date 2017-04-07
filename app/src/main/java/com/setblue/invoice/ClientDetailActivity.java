package com.setblue.invoice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.Fragments.CustomerFragment;
import com.setblue.invoice.adapter.ClientListAdapter;
import com.setblue.invoice.model.Clients;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonMethods;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.DividerItemDecoration;
import com.setblue.invoice.utils.ExceptionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ClientDetailActivity extends AppCompatActivity implements View.OnClickListener {


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
    private int clientID = 0;
    private Button invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_client_detail);
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

        back = (ImageView) tb.findViewById(R.id.iv_back);
        back.setOnClickListener(this);
        editClient = (ImageView) tb.findViewById(R.id.iv_edit);
        editClient.setOnClickListener(this);

    }

    private void init() {
        clientName = (TextView) findViewById(R.id.tv_client_name);
        companyName = (TextView) findViewById(R.id.tv_client_company);
        email = (EditText) findViewById(R.id.edt_email);
        mobile = (EditText) findViewById(R.id.edt_mobile);
        address = (EditText) findViewById(R.id.edt_address);
        invoice = (Button) findViewById(R.id.iv_invoice);
        invoice.setOnClickListener(this);


    }

    private void setData() {

    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);


        } else if (v == editClient) {

            Intent i = new Intent(this, EditClientActivity.class);
            i.putExtra("id", clientID);
            startActivity(i);

        } else if(v == invoice){
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("id", clientID);
            i.putExtra("from","ClientDetail");
            finish();
            startActivity(i);

        }
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
            // fragmentManager.beginTransaction()
            //         .replace(R.id.content_frame, fragment).setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).addToBackStack(null).commit();

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    public void clientDetail() {
        String url = Apis.ClientDetails + "id=" + getIntent().getIntExtra("id", 0);
        Log.d(CommonVariables.TAG, "Url: " + url);
        aq.progress(new ProgressDialog(this, R.style.CustomProgressDialog)).ajax(url, String.class, this, "jsonCallback");

    }

    public void jsonCallback(String url, String json, AjaxStatus status) {

        if (json != null) {
            //successful ajax call
            Log.d(CommonVariables.TAG, json.toString());
            try {
                JSONObject object = new JSONObject(json);
                if (object.optInt("resid") > 0) {
                    if (object.optString("api").equalsIgnoreCase("ClientDetails")) {
                        JSONArray jsonArray = object.optJSONArray("resData");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.optJSONObject(i);
                            clientName.setText(c.optString("FirstName") + " " + c.optString("LastName"));
                            companyName.setText(c.optString("Company"));
                            email.setText(c.optString("Email"));
                            mobile.setText(c.optString("Mobile"));
                            address.setText(c.optString("Address"));
                            clientID = c.optInt("ClientId");


                        }
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
        if (!CommonMethods.knowInternetOn(this)) {
            CommonMethods.showInternetAlert(this);
        } else {
            clientDetail();
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
