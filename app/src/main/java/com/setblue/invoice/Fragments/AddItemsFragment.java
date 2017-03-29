package com.setblue.invoice.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.AbstractAQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.InvoiceItemActivity;
import com.setblue.invoice.R;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by praful on 01-Feb-17.
 */
@SuppressLint("ValidFragment")
public class AddItemsFragment extends Fragment implements View.OnClickListener {


    private Button addItems;
    private EditText itemName;
    private EditText itemTerm;
    private EditText itemQty;
    private EditText itemRate;
    private String stName;
    private String stTerm;
    private String stQty;
    private String stRate;
    private AQuery aq;
    private int InvoiceId;


    public AddItemsFragment(int invoiceId) {
        InvoiceId = invoiceId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        aq = new AQuery(getActivity());
        init(view);



        return view;

    }


    private void init(View v) {
        itemName = (EditText)v.findViewById(R.id.edt_items_name);
        itemTerm = (EditText)v.findViewById(R.id.edt_term);
        itemQty = (EditText)v.findViewById(R.id.edt_qty);
        itemRate = (EditText)v.findViewById(R.id.edt_rate);
        addItems = (Button)v.findViewById(R.id.btn_add);
        addItems.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v == addItems){
            stName = itemName.getText().toString().trim();
            stTerm = itemTerm.getText().toString().trim();
            stQty = itemQty.getText().toString().trim();
            stRate = itemRate.getText().toString().trim();
            if(Validation.isEmptyEdittext(itemName) && Validation.isEmptyEdittext(itemTerm) && Validation.isEmptyEdittext(itemTerm) && Validation.isEmptyEdittext(itemRate)){
                itemName.setError("Enter Item Name");
                itemTerm.setError("Enter Term");
                itemQty.setError("Enter Qty");
                itemRate.setError("Enter Item Rate");
            }else if(Validation.isEmptyEdittext(itemName)){
                itemName.setError("Enter Item Name");
            }else if(Validation.isEmptyEdittext(itemTerm)){
                itemTerm.setError("Enter Term");
            }else if(Validation.isEmptyEdittext(itemQty)){
                itemQty.setError("Enter Qty");
            }else if(Validation.isEmptyEdittext(itemRate)){
                itemRate.setError("Enter Item Rate");
            }
            else {

               String url = Apis.AddInvoiceItem+"InvoiceId="+InvoiceId+"&"+"ItemName="+ URLEncoder.encode(stName)+"&"+"Term="+Integer.parseInt(stTerm)+"&"+"Qty="+stQty+"&"+"Rate="+stRate;
                Log.d(CommonVariables.TAG,"Url: "+url);
                //Make Asynchronous call using AJAX method
                aq.progress(new ProgressDialog(getActivity(),R.style.CustomProgressDialog)).ajax(url, String.class, this,"jsonCallback");

            }
        }
    }


    public void jsonCallback(String url, String json, AjaxStatus status){

        if(json != null){
            //successful ajax call
            Log.d(CommonVariables.TAG,json.toString());
            try {
                JSONObject object = new JSONObject(json);
                if(object.optInt("resid")>0){
                    //Toast.makeText(getActivity(),"Successfully add invoice item",Toast.LENGTH_LONG).show();
                    ((InvoiceItemActivity) getActivity()).onResume();
                    getActivity().getSupportFragmentManager().popBackStack();
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
