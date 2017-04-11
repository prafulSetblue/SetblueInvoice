package com.setblue.invoice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.Fragments.InvoiceFragment;
import com.setblue.invoice.adapter.ClientListAdapter;
import com.setblue.invoice.adapter.InvoiceListAdapter;
import com.setblue.invoice.components.CatLoadingView;
import com.setblue.invoice.model.Clients;
import com.setblue.invoice.model.Invoice;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonMethods;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.DividerItemDecoration;
import com.setblue.invoice.utils.ExceptionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class InvoiceListActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView back;
    RecyclerView listviewInvoice;
    ArrayList<Invoice> invoiceArrayList;
    InvoiceListAdapter invoiceListAdapter;
    private SearchView search;
    AQuery aq;
    private ImageView addInvoice;
    private SwipeRefreshLayout swipeView;
    private CatLoadingView mView;
    private InvoiceFragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_invoice_list);
            aq = new AQuery(this);
            setUpActionBar();
            init();
            InvoiceList();

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
        EditText searchEditText = (EditText)search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHint("Search");
        searchEditText.setBackground(ContextCompat.getDrawable(this,R.drawable.bottom_line));
        //search.setQueryHint("Search");
        //search.setQueryHint(Html.fromHtml("<font color = #ffffff>" + "Search" + "</font>"));

        //*** setOnQueryTextFocusChangeListener ***
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                /*Toast.makeText(getBaseContext(), String.valueOf(hasFocus),
                        Toast.LENGTH_SHORT).show();*/
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


                    invoiceListAdapter.filter(newText);
                return false;
            }
        });

    }
    private void init(){
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        listviewInvoice = (RecyclerView)findViewById(R.id.lv_invoice);
        addInvoice = (ImageView)findViewById(R.id.fab);
        addInvoice.setOnClickListener(this);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                Log.d("Swipe", "Refreshing Number");
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                        InvoiceList();
                    }
                }, 000);
            }
        });

    }
    private void InvoiceList() {
        mView = new CatLoadingView();
        mView.show((this).getSupportFragmentManager(), "load");
        String url = Apis.InvoiceList;
        //Make Asynchronous call using AJAX method
        aq.ajax(url, String.class, this,"jsonCallback");

    }
    public void jsonCallback(String url, String json, AjaxStatus status){

        if(mView != null)
            mView.dismiss();

        if(json != null){
            //successful ajax call
            Log.d(CommonVariables.TAG,json.toString());
            try {
                JSONObject object = new JSONObject(json);
                if(object.optInt("resid")>0) {
                    if (object.optJSONArray("resData").length() != 0) {
                        JSONArray jsonArray = object.optJSONArray("resData");
                        invoiceArrayList = new ArrayList<Invoice>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            invoiceArrayList.add(new Invoice("" + jsonObject.optInt("InvoiceId"), jsonObject.optString("ClientName"), jsonObject.optString("TotalAmount"), jsonObject.optString("InvoiceNo"), jsonObject.optString("InvoiceDate"),jsonObject.optString("Company")));
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                        listviewInvoice.setLayoutManager(layoutManager);
                        invoiceListAdapter = new InvoiceListAdapter(this, invoiceArrayList);
                        listviewInvoice.setAdapter(invoiceListAdapter);
                       // listviewInvoice.addItemDecoration(new DividerItemDecoration(this));
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
    public void onClick(View v) {
        if(v == back){
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }
        else if(v == addInvoice){
           /* Intent i = new Intent(this,MainActivity.class);
            i.putExtra("from","invoicelist");
            finish();
            startActivity(i);*/
             fragment = new InvoiceFragment(0,"invoicelist");
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
    @Override
    protected void onResume() {
        super.onResume();
        if(!CommonMethods.knowInternetOn(this)){
            CommonMethods.showInternetAlert(this);
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        final EditText editText = (EditText) menu.findItem(
                R.id.menu_search).getActionView();
       *//* editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*//*
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                editText.clearFocus();
                return true; // Return true to expand action view
            }
        });
       return true;
    }


    public TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

           *//* if (null != mAdapter) {
                mAdapter.getFilter().filter(s);
            }*//*
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };*/
}
