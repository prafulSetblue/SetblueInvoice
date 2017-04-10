package com.setblue.invoice.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.ClientDetailActivity;
import com.setblue.invoice.ClientListActivity;
import com.setblue.invoice.InvoiceItemActivity;
import com.setblue.invoice.InvoiceListActivity;
import com.setblue.invoice.MainActivity;
import com.setblue.invoice.R;
import com.setblue.invoice.components.CatLoadingView;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.MySessionManager;
import com.setblue.invoice.utils.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by praful on 01-Feb-17.
 */

@SuppressLint("ValidFragment")
public class InvoiceFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private EditText invoice_date;
    private EditText due_date;
    private EditText term;
    private String CurrentDate = "";
    public String dueDate = "";
    private String Duration = "";
    private Calendar myCalendar;
    private DatePickerDialog dpd;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private Button save;
    private EditText client;
    private String clientName = "";
    private int clientId = 0;
    private EditText mobile;
    private String stMobile = "";
    private String stCompany = "";
    private String stAddress = "";
    private String stPincode = "";
    private String stCity = "";
    private String stState = "";
    private String stCountry = "";
    private EditText email;
    private EditText invoiceNumber;
    MySessionManager session;
    private String DueDate;
    private String stTerm = "";
    AQuery aq;
    private EditText note;
    private String stInvoiceNo = "";
    private String stNote = "";
    private String stClient = "";
    int id = 0;
    String from = "null";
    private CatLoadingView mView;

    @SuppressLint("ValidFragment")
    public InvoiceFragment(int id, String from) {
        this.id = id;
        this.from = from;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);
        session = new MySessionManager(getActivity());
        aq = new AQuery(getActivity());
        init(view);
        if(from != null ){
            clientDetail();
        }

        return view;

    }

    private void init(View v) {
        myCalendar = Calendar.getInstance();

        invoice_date = (EditText)v.findViewById(R.id.edt_invoice_date);
        client = (EditText)v.findViewById(R.id.edt_customer);
        email = (EditText)v.findViewById(R.id.edt_Email);
        invoiceNumber = (EditText)v.findViewById(R.id.edt_invoice_number);
        due_date = (EditText)v.findViewById(R.id.edt_due_date);
        term = (EditText)v.findViewById(R.id.edt_term);
        note = (EditText)v.findViewById(R.id.edt_notes);
        save = (Button)v.findViewById(R.id.btn_save);
        save.setOnClickListener(this);
        client.setInputType(InputType.TYPE_NULL);
        invoiceNumber.setText("V2"+session.getNumber());
        //client.setOnFocusChangeListener(this);
        client.setOnClickListener(this);
        invoice_date.setOnClickListener(this);
        due_date.setOnClickListener(this);
        term.setOnClickListener(this);
        invoice_date.setInputType(InputType.TYPE_NULL);
        due_date.setInputType(InputType.TYPE_NULL);
        term.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // invoice_date.setText("");
               // due_date.setText("");
                if (!term.getText().toString().equalsIgnoreCase("")) {

                    String strDate = CurrentDate;
                    if(!strDate.equalsIgnoreCase("")) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = null;
                        try {
                            date = dateFormat.parse(strDate);
                            //dueDate = dateFormat.format(date);
                            //System.out.println(dueDate);
                            myCalendar.setTime(date);
                            myCalendar.add(Calendar.DATE, Integer.valueOf(term.getText().toString()));
                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                            dueDate = sdf1.format(myCalendar.getTime());
                            due_date.setText(dueDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
         /* invoice_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getDate();
            }
        });*/
     /* term.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

               *//* Calendar c = Calendar.getInstance();


                //String dt = "2012-01-04";  // Start date
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                CurrentDate =sdf.format(c.getTime());

                sdf.format(c.getTime());
                //Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(CurrentDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE, Integer.valueOf(s.toString()));  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                dueDate = sdf1.format(c.getTime());
                Log.d(CommonVariables.TAG,dueDate);*//*

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });*/
    }
    private void setDate() {
        invoice_date.setText(CurrentDate);
        //due_date.setText(dueDate);

    }
    public void clientDetail() {
        String url = Apis.ClientDetails + "id=" + id;
        Log.d(CommonVariables.TAG, "Url: " + url);
        aq.ajax(url, String.class, this, "jsonCallback");

    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // myCalendar.set(Calendar.YEAR, year);
        // myCalendar.set(Calendar.MONTH, monthOfYear);
        // myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // final String strDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        if(dpd.isShowing()) {
            dpd.cancel();
        }



        myCalendar.set(year, monthOfYear, dayOfMonth);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CurrentDate = sdf.format(myCalendar.getTime());
        invoice_date.setText(CurrentDate);
        stTerm = term.getText().toString();
        if(!stTerm.equalsIgnoreCase("")) {
            myCalendar.add(Calendar.DATE, Integer.valueOf(term.getText().toString()));
        }// number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        dueDate = sdf1.format(myCalendar.getTime());
        due_date.setText(dueDate);
       // setDate();
    }
    public void getDate() {

        dpd = new DatePickerDialog(getActivity(), R.style.DialogTheme,this, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        try {
            dpd.getDatePicker().setMinDate(new Date().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        dpd.show();
        Log.v("TAG", "I AM ON");
    }


    @Override
    public void onClick(View v) {

         if(v == save){
             stClient = client.getText().toString();
             stInvoiceNo = invoiceNumber.getText().toString();
             stTerm = term.getText().toString();
             CurrentDate = invoice_date.getText().toString();
             dueDate = due_date.getText().toString();
             stNote = note.getText().toString();
             if(Validation.isEmptyEdittext(client) && Validation.isEmptyEdittext(email) && Validation.isEmptyEdittext(invoiceNumber)
                     && Validation.isEmptyEdittext(term) && Validation.isEmptyEdittext(invoice_date) && Validation.isEmptyEdittext(due_date)
                        && Validation.isEmptyEdittext(note)){
                 client.setError("Enter Client Name");
                 email.setError("Enter Email Address");
                 invoiceNumber.setError("Enter Invoice Number");
                 term.setError("Enter Term");
                 invoice_date.setError("Enter Invoice Date");
                 due_date.setError("Enter Due Date");
                 note.setError("Enter Note");
             }
             else if(Validation.isEmptyEdittext(client)){
                 client.setError("Enter Client Name");
             }
             else if(Validation.isEmptyEdittext(invoiceNumber)){
                 invoiceNumber.setError("Enter Invoice Number");
             }
             else if(Validation.isEmptyEdittext(term)){
                 term.setError("Enter Term");
             }
             else if(Validation.isEmptyEdittext(invoice_date)){
                 invoice_date.setError("Enter Invoice Date");
             }
             else if(Validation.isEmptyEdittext(due_date)){
                 due_date.setError("Enter Due Date");
             }
             else if(Validation.isEmptyEdittext(note)){
                 note.setError("Enter Notes");
             }
             else {
                 AddInvoice();
                /* Intent i = new Intent(getActivity(), InvoiceItemActivity.class);
                 i.putExtra("InvoiceId",2);
                 getActivity().finish();
                 startActivity(i);
                 getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);*/
             }

        }else if(v == client){

            Intent i = new Intent(getActivity(), ClientListActivity.class);
            i.putExtra("from","Invoice");
            startActivityForResult(i,CommonVariables.KEY_CUSTOMER_LIST);
            getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }
        else if(v == invoice_date){
            getDate();
        }
       /* else if(v == term){
            Calendar c = Calendar.getInstance();


            //String dt = "2012-01-04";  // Start date
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            CurrentDate =sdf.format(c.getTime());

            sdf.format(c.getTime());
            //Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(CurrentDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DATE, Integer.valueOf(term.getText().toString()));  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
            dueDate = sdf1.format(c.getTime());
            Log.d(CommonVariables.TAG,dueDate);

        }*/

    }
    private void replaceFragment(Fragment fragment){
        if (fragment != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
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


    private void AddInvoice() {
        mView = new CatLoadingView();
        mView.show(getActivity().getSupportFragmentManager(), "load");
        String url = Apis.AddInvoice+"ClientId="+clientId+"&"+"MobileNo="+stMobile+"&"+"CompanyAddress="+stAddress+"&"+"City="+stCity
                +"&"+"State="+stState+"&"+"Country="+stCountry+"&"+"Pincode="+stPincode+"&"+"InvoiceDate="+ URLEncoder.encode(CurrentDate)+"&"+"DueDate="+ URLEncoder.encode(dueDate)+"&"+"Note="+stNote+"&"+"invoiceno="+stInvoiceNo+"&"+"AdminId="+Integer.parseInt(session.getUserId());

        Log.d(CommonVariables.TAG,"url: "+ url);
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
                if(object.optString("api").equalsIgnoreCase("AddInvoice")) {
                    if (object.optInt("resid") > 0) {
                        //Toast.makeText(getActivity(), "Successfully create invoice", Toast.LENGTH_LONG).show();
                        session.setNumber(session.getNumber() + 1);

                        Intent i = new Intent(getActivity(), InvoiceItemActivity.class);
                        i.putExtra("InvoiceId", object.optInt("InvoiceId"));
                        getActivity().finish();
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        //getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), object.optString("res"), Toast.LENGTH_LONG).show();
                    }
                }
                if (object.optString("api").equalsIgnoreCase("ClientDetails")) {
                    if (object.optInt("resid") > 0) {
                        JSONArray jsonArray = object.optJSONArray("resData");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.optJSONObject(i);
                            clientName = c.optString("FirstName") + " " + c.optString("LastName");
                            clientId = c.optInt("ClientId");
                            stMobile = c.optString("Mobile");
                            stCompany = c.optString("Company");
                            stAddress = c.optString("Address");
                            stPincode = c.optString("Pincode");
                            stCity = c.getString("City");
                            stState = c.optString("State");
                            stCountry = c.optString("Country");
                            client.setText(clientName);


                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            //ajax error
            Toast.makeText(getActivity(),"Something Wrong.",Toast.LENGTH_SHORT).show();
            Log.d(CommonVariables.TAG,""+status.getCode());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK && data != null) {
                clientName = data.getExtras().getString("name");
                clientId = data.getExtras().getInt("id");
                stMobile = data.getExtras().getString("mobile");
                stCompany = data.getExtras().getString("company");
                stAddress = data.getExtras().getString("address");
                stPincode = data.getExtras().getString("pincode");
                stCity = data.getExtras().getString("city");
                stState = data.getExtras().getString("state");
                stCountry = data.getExtras().getString("country");
                client.setText(clientName);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if(from == null){
                            getFragmentManager().popBackStack();
                        }
                        else if(from.equalsIgnoreCase("null") ) {
                            getFragmentManager().popBackStack();
                        }else if(from.equalsIgnoreCase("ClientDetail")){
                            Intent i = new Intent(getActivity(), ClientDetailActivity.class);
                            i.putExtra("from","ClientDetail");
                            i.putExtra("id",id);
                            getActivity().finish();
                            startActivity(i);
                            getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        }
                        else {
                            Intent i = new Intent(getActivity(), InvoiceListActivity.class);
                            i.putExtra("from","InvoiceList");
                            getActivity().finish();
                            startActivity(i);
                            getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
