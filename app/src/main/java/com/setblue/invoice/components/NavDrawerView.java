package com.setblue.invoice.components;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.CreateCompanyActivity;
import com.setblue.invoice.MainActivity;
import com.setblue.invoice.R;
import com.setblue.invoice.adapter.CompanyListAdapter;
import com.setblue.invoice.adapter.ExpandableListAdapter;
import com.setblue.invoice.model.Clients;
import com.setblue.invoice.model.Company;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.DividerItemDecoration;
import com.setblue.invoice.utils.MySessionManager;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NavDrawerView extends LinearLayout implements View.OnClickListener {


    RecyclerView recyclerview;
    ArrayList<String> listCategory = new ArrayList<String>();
    private Spinner spCompany;
    AQuery aq;
    private static final String[] COMPANY = new String[]{
            "SETBLUE", "V2IDEAS", "SAREEBAZAR"
    };
    private MySessionManager session;
    private CatLoadingView mView;
    private ArrayList<Company> companyArrayList;
    private CompanyListAdapter adapter;
    public String st_company;
    private int st_companyID = 0;
    private ImageView add_company;

    public NavDrawerView(Context context) {
        super(context);
        setup();
    }

    public NavDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {

        inflate(getContext(), R.layout.view_navigation, this);
        aq = new AQuery(getContext());
        session = new MySessionManager(getContext());
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        spCompany = (Spinner) findViewById(R.id.sp_company);
        add_company = (ImageView) findViewById(R.id.iv_add_company);
        add_company.setOnClickListener(this);



    }

    public void setMenuData() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, 1, "HOME"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, 1, "CLIENTS"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, 1, "INVOICE"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, 1, "ABOUT US"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, 1, "CONTACT US"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, 1, "LOGOUT"));
        recyclerview.setAdapter(new ExpandableListAdapter(data, (MainActivity) getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext()));
        CompanyList();

        /*spCompany.setOnItemClickListener((adapterView, view, i, l) -> {
            strCompanyId = COMPANY[i];
        });
*/


    }

    public void CompanyList() {
        mView = new CatLoadingView();
        mView.show(((MainActivity) getContext()).getSupportFragmentManager(), "load");
        String url = Apis.GetCompanyList;
        //Make Asynchronous call using AJAX method
        aq.ajax(url, String.class, this, "jsonCallback");

    }

    public void jsonCallback(String url, String json, AjaxStatus status) {

        if (mView != null) {
            mView.dismiss();
        }
        try {
            if (json != null) {
                Log.d(CommonVariables.TAG, json.toString());
                JSONObject object = new JSONObject(json);
                if (object.optInt("resid") > 0) {
                    if (object.optJSONArray("resData").length() != 0) {
                        JSONArray jsonArray = object.optJSONArray("resData");
                        Company company = null;
                        companyArrayList = new ArrayList<Company>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            company = new Company(jsonObject.optString("CompanyName"), jsonObject.optInt("CompanyId"));
                            companyArrayList.add(company);

                        }
                        adapter = new CompanyListAdapter(getContext(), companyArrayList);
                        spCompany.setAdapter(adapter);
                        if (!session.getCompany_name().equalsIgnoreCase("")) {
                            spCompany.setSelection(adapter.getPosition(session.getCompany_name()));
                        }
                        spCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                st_company = companyArrayList.get(position).getComapany_Name();
                                st_companyID = companyArrayList.get(position).getCompany_ID();
                                session.setCompany_name(st_company);
                                session.setCompanyID(st_companyID);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        if (v == add_company) {
            Intent i = new Intent(getContext(), CreateCompanyActivity.class);
            getContext().startActivity(i);
        }
    }
}
