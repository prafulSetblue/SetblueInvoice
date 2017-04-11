package com.setblue.invoice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import com.setblue.invoice.adapter.ClientListAdapter;
import com.setblue.invoice.components.CatLoadingView;
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
    public SearchView search;
    AQuery aq;
    private String from = "";
    private ImageView addClient;
    private FragmentManager fragmentManager;
    private CustomerFragment fragment;
    private SwipeRefreshLayout swipeView;
    private CatLoadingView mView;
    public TextView title;
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
        title = (TextView) tb.findViewById(R.id.tv_title);

        back.setOnClickListener(this);
        search=(SearchView) findViewById(R.id.iv_search);
       // search.setQueryHint("Search");
        EditText searchEditText = (EditText)search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHint("Search");
        searchEditText.setBackground(ContextCompat.getDrawable(this,R.drawable.bottom_line));
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
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        listviewClient = (RecyclerView)findViewById(R.id.lv_clients);

        addClient = (ImageView)findViewById(R.id.fab);
        addClient.setOnClickListener(this);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                Log.d("Swipe", "Refreshing Number");
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                        //Toast.makeText(getActivity(),"Refresh",Toast.LENGTH_LONG).show();
                       setData();
                        //onResume();
                        //double f = Math.random();
                        //rndNum.setText(String.valueOf(f));
                    }
                }, 000);
            }
        });

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
            //getFragmentManager().popBackStack();
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        }
        else if(v == addClient){
           /* Intent i = new Intent(this,MainActivity.class);
            i.putExtra("from","clientlist");
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(i);*/
            fragment = new CustomerFragment("clientlist");
            replaceFragment(fragment);
        }
    }
    private void replaceFragment(Fragment fragment){
        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
            ft.replace(R.id.content_frame, fragment,"Client Fragment");
            ft.addToBackStack(null);
            ft.commit();
            // fragmentManager.beginTransaction()
            //         .replace(R.id.content_frame, fragment).setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).addToBackStack(null).commit();

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private void ClientList() {
        mView = new CatLoadingView();
        mView.show((this).getSupportFragmentManager(), "load");
        String url = Apis.ClientList;
        //Make Asynchronous call using AJAX method
        aq.ajax(url, String.class, this,"jsonCallback");

    }
    public void jsonCallback(String url, String json, AjaxStatus status){
        if (mView != null)
            mView.dismiss();
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
                       // listviewClient.addItemDecoration(new DividerItemDecoration(this));
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
        title.setText("Client List");
        if(!CommonMethods.knowInternetOn(this)){
            CommonMethods.showInternetAlert(this);
        }
    }
}
