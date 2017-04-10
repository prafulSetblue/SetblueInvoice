package com.setblue.invoice;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.components.CatLoadingView;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonMethods;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.ExceptionHandler;
import com.setblue.invoice.utils.MySessionManager;
import com.setblue.invoice.utils.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EditInvoiceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {


    public ImageView back;
    AQuery aq;
    private int clientID = 0;
    private EditText invoice_date;
    private EditText client;
    private EditText email;
    private EditText invoiceNumber;
    private EditText due_date;
    private EditText term;
    private EditText note;
    private Button save;
    private Calendar myCalendar;
    private DatePickerDialog dpd;
    private String CurrentDate;
    private String stTerm;
    private String dueDate;
    private String stMobile;
    private String stCompany;
    private String stAddress;
    private String stPincode;
    private String stCity;
    private String stState;
    private String stCountry;
    private String stInvoiceNo;
    private String stNote;
    MySessionManager session;
    private String stEmail;
    private CatLoadingView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_edit_invoice);
            aq = new AQuery(this);
            session = new MySessionManager(this);
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


    }

    private void init() {
        myCalendar = Calendar.getInstance();
        invoice_date = (EditText) findViewById(R.id.edt_invoice_date);
        client = (EditText) findViewById(R.id.edt_customer);
        email = (EditText) findViewById(R.id.edt_Email);
        invoiceNumber = (EditText) findViewById(R.id.edt_invoice_number);
        due_date = (EditText) findViewById(R.id.edt_due_date);
        term = (EditText) findViewById(R.id.edt_term);
        note = (EditText) findViewById(R.id.edt_notes);
        save = (Button) findViewById(R.id.btn_save);
        save.setOnClickListener(this);
        invoice_date.setOnClickListener(this);
        invoice_date.setInputType(InputType.TYPE_NULL);
        due_date.setInputType(InputType.TYPE_NULL);
        term.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //invoice_date.setText(CurrentDate);
                String strDate = CurrentDate;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = dateFormat.parse(strDate);
                    //dueDate = dateFormat.format(date);
                    //System.out.println(dueDate);
                    myCalendar.setTime(date);
                    if (!term.getText().toString().equalsIgnoreCase("")) {
                        myCalendar.add(Calendar.DATE, Integer.valueOf(term.getText().toString()));
                    }// number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                    dueDate = sdf1.format(myCalendar.getTime());
                    due_date.setText(dueDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

               /* if (!stTerm.equalsIgnoreCase("")) {
                    myCalendar.add(Calendar.DATE, Integer.valueOf(term.getText().toString()));
                }// number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                dueDate = sdf1.format(myCalendar.getTime());
                due_date.setText(dueDate);*/

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       /* term.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                stTerm = s.toString();
                if(!stTerm.equalsIgnoreCase("")) {
                    myCalendar.add(Calendar.DATE, Integer.valueOf(term.getText().toString()));
                }// number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                dueDate = sdf1.format(myCalendar.getTime());
                due_date.setText(dueDate);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

    }

    private void setData() {

    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            }
            else {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }


        } else if (v == invoice_date) {
            getDate();
        } else if (v == save) {
            stEmail = email.getText().toString();
            stInvoiceNo = invoiceNumber.getText().toString();
            stTerm = term.getText().toString();
            CurrentDate = invoice_date.getText().toString();
            dueDate = due_date.getText().toString();
            stNote = note.getText().toString();
            if (Validation.isEmptyEdittext(client)) {
                client.setError("Enter Client Name");
            } else if (Validation.isEmptyEdittext(email)) {
                email.setError("Enter Email Address");
            } else if (Validation.isEmptyEdittext(invoiceNumber)) {
                invoiceNumber.setError("Enter Invoice Number");
            } else if (Validation.isEmptyEdittext(term)) {
                term.setError("Enter Term");
            } else if (Validation.isEmptyEdittext(invoice_date)) {
                invoice_date.setError("Enter Invoice Date");
            } else if (Validation.isEmptyEdittext(due_date)) {
                due_date.setError("Enter Due Date");
            } else if (Validation.isEmptyEdittext(note)) {
                note.setError("Enter Notes");
            } else if (!Validation.isValidEmail(stEmail)) {
                email.setError("Enter Valid Email Address");
            } else {
                UpdateInvoice();
            }
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // myCalendar.set(Calendar.YEAR, year);
        // myCalendar.set(Calendar.MONTH, monthOfYear);
        // myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // final String strDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        if (dpd.isShowing()) {
            dpd.cancel();
        }


        myCalendar.set(year, monthOfYear, dayOfMonth);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CurrentDate = sdf.format(myCalendar.getTime());
        invoice_date.setText(CurrentDate);
        stTerm = term.getText().toString();
        if (!stTerm.equalsIgnoreCase("")) {
            myCalendar.add(Calendar.DATE, Integer.valueOf(term.getText().toString()));
        }// number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        dueDate = sdf1.format(myCalendar.getTime());
        due_date.setText(dueDate);

        // setDate();
    }

    public void getDate() {

        dpd = new DatePickerDialog(this,android.R.style.Theme_DeviceDefault_Light, this, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        try {
            dpd.getDatePicker().setMinDate(new Date().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        dpd.show();
        Log.v("TAG", "I AM ON");
    }


    public void clientDetail() {
        mView = new CatLoadingView();
        mView.show((this).getSupportFragmentManager(), "load");
        String url = Apis.ClientDetails + "id=" + getIntent().getIntExtra("clientID", 0);
        Log.d(CommonVariables.TAG, "Url: " + url);
        aq.ajax(url, String.class, this, "jsonCallback");

    }

    private void InvoiceDetail() {
        mView = new CatLoadingView();
        mView.show((this).getSupportFragmentManager(), "load");
        String url = Apis.InvoiceDetail + "id=" + getIntent().getIntExtra("id", 0);
        Log.d(CommonVariables.TAG, "Url: " + url);
        aq.ajax(url, String.class, this, "jsonCallback");
    }

    private void UpdateInvoice() {
        mView = new CatLoadingView();
        mView.show((this).getSupportFragmentManager(), "load");
        String url = Apis.UpdateInvoice + "ClientId=" + getIntent().getIntExtra("clientID", 0) + "&" + "MobileNo=" + stMobile + "&" + "CompanyAddress=" + stAddress + "&" + "City=" + stCity
                + "&" + "State=" + stState + "&" + "Country=" + stCountry + "&" + "Pincode=" + stPincode + "&" + "InvoiceDate=" + URLEncoder.encode(CurrentDate) + "&" + "DueDate=" + URLEncoder.encode(dueDate) + "&" + "Note=" + stNote + "&" + "invoiceno=" + stInvoiceNo + "&" + "AdminId=" + Integer.parseInt(session.getUserId()) + "&" + "id=" + getIntent().getIntExtra("id", 0);

        Log.d(CommonVariables.TAG, "url: " + url);
        //Make Asynchronous call using AJAX method
        aq.ajax(url, String.class, this, "jsonCallback");

    }

    public void jsonCallback(String url, String json, AjaxStatus status) {
        if(mView != null)
            mView.dismiss();

        if (json != null) {
            //successful ajax call
            Log.d(CommonVariables.TAG, json.toString());
            try {
                JSONObject object = new JSONObject(json);

                if (object.optString("api").equalsIgnoreCase("InvoiceDetail")) {
                    if (object.optInt("resid") > 0) {
                        JSONArray jsonArray = object.optJSONArray("resData");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.optJSONObject(i);
                            CurrentDate = c.optString("InvoiceDate");
                            invoice_date.setText(CurrentDate);
                            invoiceNumber.setText(c.optString("InvoiceNo"));
                            dueDate = c.optString("DueDate");
                            due_date.setText(dueDate);
                            term.setText(get_count_of_days(CurrentDate, dueDate));
                            client.setText(c.optString("ClientName"));
                            note.setText(c.optString("Note"));

                        }
                    }
                }
                if (object.optString("api").equalsIgnoreCase("ClientDetails")) {
                    if (object.optInt("resid") > 0) {
                        JSONArray jsonArray = object.optJSONArray("resData");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.optJSONObject(i);
                            email.setText(c.optString("Email"));
                            stMobile = c.optString("Mobile");
                            stCompany = c.optString("Company");
                            stAddress = c.optString("Address");
                            stPincode = c.optString("Pincode");
                            stCity = c.optString("City");
                            stState = c.optString("State");
                            stCountry = c.optString("Country");
                            InvoiceDetail();


                        }
                    }
                }


                if (object.optString("api").equalsIgnoreCase("UpdateInvoice")) {
                    if (object.optInt("resid") > 0) {
                        Intent i = new Intent(this, InvoiceItemActivity.class);
                        i.putExtra("InvoiceId", getIntent().getIntExtra("id", 0));
                        //finish();
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    } else {
                        Toast.makeText(this, "Something Worng.", Toast.LENGTH_SHORT).show();
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

    public String get_count_of_days(String Created_date_String, String Expire_date_String) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date Created_convertedDate = null, Expire_CovertedDate = null, todayWithZeroTime = null;
        try {
            Created_convertedDate = dateFormat.parse(Created_date_String);
            Expire_CovertedDate = dateFormat.parse(Expire_date_String);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int c_year = 0, c_month = 0, c_day = 0;
        Calendar c_cal = Calendar.getInstance();
        c_cal.setTime(Created_convertedDate);
        c_year = c_cal.get(Calendar.YEAR);
        c_month = c_cal.get(Calendar.MONTH);
        c_day = c_cal.get(Calendar.DAY_OF_MONTH);
      /*  if (Created_convertedDate.after(todayWithZeroTime)) {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(Created_convertedDate);
            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(todayWithZeroTime);
            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);
        }*/


    /*Calendar today_cal = Calendar.getInstance();
    int today_year = today_cal.get(Calendar.YEAR);
    int today = today_cal.get(Calendar.MONTH);
    int today_day = today_cal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar e_cal = Calendar.getInstance();
        e_cal.setTime(Expire_CovertedDate);

        int e_year = e_cal.get(Calendar.YEAR);
        int e_month = e_cal.get(Calendar.MONTH);
        int e_day = e_cal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(c_year, c_month, c_day);
        date2.clear();
        date2.set(e_year, e_month, e_day);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) (diff / (24 * 60 * 60 * 1000));

        return ("" + (int) dayCount);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
}
