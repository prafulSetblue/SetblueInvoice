package com.setblue.invoice;

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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.Fragments.CustomerFragment;
import com.setblue.invoice.adapter.ClientListAdapter;
import com.setblue.invoice.adapter.Company1ListAdapter;
import com.setblue.invoice.components.CatLoadingView;
import com.setblue.invoice.model.Clients;
import com.setblue.invoice.model.Company;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonMethods;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.ExceptionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CompanyListActivity extends AppCompatActivity implements View.OnClickListener {


    private AQuery aq;
    private String from = "";
    private ImageView back;
    private TextView title;
    private SearchView search;
    private SwipeRefreshLayout swipeView;
    private RecyclerView listviewCompany;
    private TextView no_company;
    private ImageView addCompany;
    private CatLoadingView mView;
    private ArrayList<Company> companyArrayList;
    private Company1ListAdapter companyListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_company_list);
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
        title = (TextView) tb.findViewById(R.id.tv_title);

        back.setOnClickListener(this);
        search = (SearchView) findViewById(R.id.iv_search);
        // search.setQueryHint("Search");
        EditText searchEditText = (EditText) search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHint("Search");
        searchEditText.setBackground(ContextCompat.getDrawable(this, R.drawable.bottom_line));
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

                companyListAdapter.filter(newText);
                return false;
            }
        });


    }

    private void init() {
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        listviewCompany = (RecyclerView) findViewById(R.id.lv_company);
        no_company = (TextView) findViewById(R.id.tv_no_company);
        addCompany = (ImageView) findViewById(R.id.fab);
        addCompany.setOnClickListener(this);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                Log.d("Swipe", "Refreshing Number");
                (new Handler()).postDelayed(new Runnable() {
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

    private void setData() {

        if (CommonMethods.knowInternetOn(this)) {
            CompanyList();
        } else {
            CommonMethods.showInternetAlert(this);
        }


    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else if (v == addCompany) {
            Intent i = new Intent(this,CreateCompanyActivity.class);
            i.putExtra("from","companylist");
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
    }


    private void CompanyList() {
        mView = new CatLoadingView();
        mView.show((this).getSupportFragmentManager(), "load");
        String url = Apis.GetCompanyFullList;


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
                if (object.optInt("resid") > 0) {
                    if (object.optJSONArray("resData").length() != 0) {
                        no_company.setVisibility(View.GONE);
                        JSONArray jsonArray = object.optJSONArray("resData");
                        companyArrayList = new ArrayList<Company>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            String Address = jsonObject.optString("AddressLine1")+","+jsonObject.optString("AddressLine2")+","+jsonObject.optString("City");
                            companyArrayList.add(new Company(jsonObject.optString("CompanyName"),jsonObject.optInt("CompanyId"),Address));
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                        listviewCompany.setLayoutManager(layoutManager);
                        companyListAdapter = new Company1ListAdapter(this, companyArrayList, from);
                        listviewCompany.setAdapter(companyListAdapter);
                        // listviewClient.addItemDecoration(new DividerItemDecoration(this));
                    } else {
                        no_company.setVisibility(View.VISIBLE);
                        //Toast.makeText(this,object.optString("res"),Toast.LENGTH_LONG).show();
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
        title.setText("Company List");
        if (!CommonMethods.knowInternetOn(this)) {
            CommonMethods.showInternetAlert(this);
        }
    }
}
