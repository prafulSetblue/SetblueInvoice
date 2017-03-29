package com.setblue.invoice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.setblue.invoice.adapter.ClientListAdapter;
import com.setblue.invoice.model.Clients;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonMethods;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.DividerItemDecoration;
import com.setblue.invoice.utils.ExceptionHandler;
import com.setblue.invoice.utils.MySessionManager;
import com.setblue.invoice.utils.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ClientListActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView back;
    RecyclerView listviewClient;
    ArrayList<Clients> clientsArrayList;
    ClientListAdapter clientListAdapter;
    private SearchView search;
    AQuery aq;
    private String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_client_list);
            aq = new AQuery(this);
            from = getIntent().getStringExtra("from");
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
        search=(SearchView) findViewById(R.id.iv_search);
        search.setQueryHint("Search");
        //*** setOnQueryTextFocusChangeListener ***
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub


            }
        });

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub



                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub

                clientListAdapter.filter(newText);
                return false;
            }
        });


    }
    private void init(){
        listviewClient = (RecyclerView)findViewById(R.id.lv_clients);

    }
    private void setData(){

        if(CommonMethods.knowInternetOn(this)){
            ClientList();
        }
        else {
            CommonMethods.showInternetAlert(this);
        }



    }
    @Override
    public void onClick(View v) {
        if(v == back){
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        }
    }

    private void ClientList() {
        String url = Apis.ClientList;
        //Make Asynchronous call using AJAX method
        aq.progress(new ProgressDialog(this,R.style.CustomProgressDialog)).ajax(url, String.class, this,"jsonCallback");

    }
    public void jsonCallback(String url, String json, AjaxStatus status){

        if(json != null){
            //successful ajax call
            Log.d(CommonVariables.TAG,json.toString());
            try {
                JSONObject object = new JSONObject(json);
                if(object.optInt("resid")>0) {
                    if (object.optJSONArray("resData").length() != 0) {
                        JSONArray jsonArray = object.optJSONArray("resData");
                        clientsArrayList = new ArrayList<Clients>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            clientsArrayList.add(new Clients(jsonObject.optInt("ClientId"), jsonObject.optString("FirstName") + " " + jsonObject.optString("LastName")
                                    , jsonObject.optString("Email"), jsonObject.optString("Mobile"), jsonObject.optString("Company"), jsonObject.optString("Address"),
                                    jsonObject.optString("Pincode"), jsonObject.optString("City"), jsonObject.optString("State"), jsonObject.optString("Country")));
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                        listviewClient.setLayoutManager(layoutManager);
                        clientListAdapter = new ClientListAdapter(this, clientsArrayList, from);
                        listviewClient.setAdapter(clientListAdapter);
                        listviewClient.addItemDecoration(new DividerItemDecoration(this));
                    } else {
                        //Toast.makeText(this,object.optString("res"),Toast.LENGTH_LONG).show();
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
    }
}
