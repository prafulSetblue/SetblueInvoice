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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.Fragments.AddItemsFragment;
import com.setblue.invoice.Fragments.CustomerFragment;
import com.setblue.invoice.adapter.ClientListAdapter;
import com.setblue.invoice.adapter.InvoiceItemListAdapter;
import com.setblue.invoice.model.Clients;
import com.setblue.invoice.model.InvoiceItem;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonMethods;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.DividerItemDecoration;
import com.setblue.invoice.utils.ExceptionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class InvoiceItemActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView back;
    private ImageView editClient;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    private ImageView addItems;
    private RecyclerView listviewItems;
    AQuery aq;
    private ArrayList<InvoiceItem> clientsArrayList;
    private InvoiceItemListAdapter itemListAdapter;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_invoice_items);
            aq = new AQuery(this);
            setUpActionBar();
            init();

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
        listviewItems = (RecyclerView)findViewById(R.id.lv_items);
        save = (Button)findViewById(R.id.btn_save);
        save.setOnClickListener(this);
        addItems = (ImageView)findViewById(R.id.iv_add_items);
        addItems.setOnClickListener(this);


    }
    public void setData(){
        ItemList();
    }
    @Override
    public void onClick(View v) {

        if(v == back){
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }
        else  if(v == save){
            Intent i = new Intent(this,InvoiceDetailActivity.class);
            i.putExtra("invoiceID",getIntent().getIntExtra("InvoiceId",0));
            finish();
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        else  if(v == addItems){

            fragment = new AddItemsFragment(getIntent().getIntExtra("InvoiceId",0));
            replaceFragment(fragment);

        }

    }

    private void ItemList() {
        String url = Apis.InvoiceItemList+"InvoiceId="+getIntent().getIntExtra("InvoiceId",0);
        //Make Asynchronous call using AJAX method
        Log.d(CommonVariables.TAG,"Url: "+url);
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
                        clientsArrayList = new ArrayList<InvoiceItem>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            clientsArrayList.add(new InvoiceItem(jsonObject.optInt("InvocieItemId"), jsonObject.optInt("InvoiceId"), jsonObject.optString("ItemName")
                                    , jsonObject.optString("Term"), jsonObject.optInt("Qty"), jsonObject.optInt("Rate"), jsonObject.optBoolean("IsDeleted")));
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                        listviewItems.setLayoutManager(layoutManager);
                        itemListAdapter = new InvoiceItemListAdapter(this, clientsArrayList);
                        listviewItems.setAdapter(itemListAdapter);
                        listviewItems.addItemDecoration(new DividerItemDecoration(this));
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
    private void replaceFragment(Fragment fragment){
        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
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

    @Override
    public void onResume() {
        super.onResume();
        if(!CommonMethods.knowInternetOn(this)){
            CommonMethods.showInternetAlert(this);
        }
        else {
            setData();
        }
    }

}
