package com.setblue.invoice.components;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.setblue.invoice.MainActivity;
import com.setblue.invoice.R;
import com.setblue.invoice.adapter.ExpandableListAdapter;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.DividerItemDecoration;
import com.setblue.invoice.utils.MySessionManager;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NavDrawerView extends LinearLayout {


    RecyclerView recyclerview;
    ArrayList<String> listCategory = new ArrayList<String>();
    private MaterialBetterSpinner spCompany;
    private static final String[] COMPANY = new String[] {
            "SETBLUE", "V2IDEAS", "SAREEBAZAR"
    };
    private MySessionManager session;

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
        session = new MySessionManager(getContext());
        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        spCompany = (MaterialBetterSpinner)  findViewById(R.id.sp_company);
        setMenuData();


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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, COMPANY);

        spCompany.setAdapter(adapter);
        /*spCompany.setOnItemClickListener((adapterView, view, i, l) -> {
            strCompanyId = COMPANY[i];
        });
*/
        if(session.getPosition() < 0){
            spCompany.setText("");
        }else {
            spCompany.setText(COMPANY[session.getPosition()]);
        }

        spCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //strCompanyId = COMPANY[position];
                Log.d(CommonVariables.TAG,COMPANY[position]);
                session.setCompanyID(position);
                session.setPosition(position);
            }
        });


    }



}
