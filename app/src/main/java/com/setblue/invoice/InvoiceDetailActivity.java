package com.setblue.invoice;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.Fragments.CustomerFragment;
import com.setblue.invoice.adapter.InvoiceDetailItemListAdapter;
import com.setblue.invoice.adapter.InvoiceItemListAdapter;
import com.setblue.invoice.mail.Mail;
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
import java.util.Properties;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class InvoiceDetailActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView back;
    private ImageView editClient;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private ImageView mail;
    private TextView clientName;
    private TextView clientCompany;
    private TextView invoiceNumber;
    private EditText invoiceDate;
    private EditText dueDate;
    private TextView billingAddress;
    private TextView subTotal;
    private TextView Tax;
    private TextView Credit;
    private TextView Total;
    private EditText notes;
    AQuery aq;
    private TextView tvTotal;
    private RecyclerView listviewItems;
    private ArrayList<InvoiceItem> clientsArrayList;
    private InvoiceDetailItemListAdapter itemListAdapter;
    private int invoiceID;
    private int clientId;
    private ImageView preview;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                //your codes here

            }
            setContentView(R.layout.activity_invoive_detail);
            aq = new AQuery(this);
            builder = new android.support.v7.app.AlertDialog.Builder(this);



            setUpActionBar();
            init();
            setData();
            ItemList();
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
        mail = (ImageView) tb.findViewById(R.id.iv_mail);
        mail.setOnClickListener(this);
        preview = (ImageView) tb.findViewById(R.id.iv_preview);
        preview.setOnClickListener(this);
        editClient = (ImageView) tb.findViewById(R.id.iv_edit);
        editClient.setOnClickListener(this);

    }
    private void init(){

        clientName = (TextView)findViewById(R.id.tv_client_name);
        clientCompany = (TextView)findViewById(R.id.tv_client_company);
        invoiceNumber = (TextView)findViewById(R.id.tv_invoice_number);
        invoiceDate = (EditText)findViewById(R.id.edt_invoice_date);
        dueDate = (EditText)findViewById(R.id.edt_due_date);
        billingAddress = (TextView)findViewById(R.id.edt_billing_address);
        subTotal = (TextView)findViewById(R.id.tv_subtotal);
        Tax = (TextView)findViewById(R.id.tv_tax);
        Credit = (TextView)findViewById(R.id.tv_credit);
        Total = (TextView)findViewById(R.id.tv_total);
        tvTotal = (TextView)findViewById(R.id.tv_total1);
        notes = (EditText)findViewById(R.id.edt_notes);
        listviewItems = (RecyclerView)findViewById(R.id.lv_items);

    }
    private void setData(){
        String url = Apis.InvoiceDetail+"id="+ getIntent().getIntExtra("invoiceID",0);
        Log.d(CommonVariables.TAG,"Url: "+url);
        aq.progress(new ProgressDialog(this,R.style.CustomProgressDialog)).ajax(url, String.class, this,"jsonCallback");
    }


    private void ItemList() {
        String url = Apis.InvoiceItemList+"InvoiceId="+getIntent().getIntExtra("invoiceID",0);
        //Make Asynchronous call using AJAX method
        Log.d(CommonVariables.TAG,"Url: "+url);
        aq.progress(new ProgressDialog(this,R.style.CustomProgressDialog)).ajax(url, String.class, this,"jsonCallback");

    }

    @Override
    public void onClick(View v) {
        if(v == back){
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        }

        else  if(v == editClient){
            Intent i = new Intent(this,EditInvoiceActivity.class);
            i.putExtra("id",getIntent().getIntExtra("invoiceID",0));
            i.putExtra("clientID",clientId);
            startActivity(i);

           /* fragment = new CustomerFragment();
            replaceFragment(fragment);*/

        }
        else  if(v == preview){
            Intent i = new Intent(this,InvoicePreviewActivity.class);
            i.putExtra("id",getIntent().getIntExtra("invoiceID",0));
            startActivity(i);

        }
        else  if(v == mail){




            builder.setTitle(CommonVariables.TAG);
            builder.setMessage("Are You Sure Send Mail This Invoice?");
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {



                }
            });
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Mail m = new Mail("pusptl776@gmail.com","pflptl1010");
                    String[] toArr = {"pflptl776@gmail.com"};
                    try {
                        m.addAttachment("/storage/emulated/0/pdfdemo/invoice.pdf");

                        if(m.send()) {
                            Toast.makeText(InvoiceDetailActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(InvoiceDetailActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                        }
                    } catch(Exception e) {
                        //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                        Log.e("MailApp", "Could not send email", e);
                    }

                    /*String url = Apis.SendEmail+"InvoiceId="+getIntent().getIntExtra("invoiceID",0);
                    Log.d(CommonVariables.TAG,url);
                    aq.progress(new ProgressDialog(InvoiceDetailActivity.this,R.style.CustomProgressDialog)).ajax(url, String.class, this,"jsonCallback");
*/

                }
            });
            builder.show();


           /* fragment = new CustomerFragment();
            replaceFragment(fragment);*/

        }
    }

    public void jsonCallback(String url, String json, AjaxStatus status){

        if(json != null){
            //successful ajax call
            Log.d(CommonVariables.TAG,json.toString());
            try {
                JSONObject object = new JSONObject(json);
                if(object.optInt("resid")>0) {
                    if(object.optString("api").equalsIgnoreCase("InvoiceDetail")) {
                        JSONArray jsonArray = object.optJSONArray("resData");
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject c = jsonArray.optJSONObject(i);
                            invoiceID = c.optInt("InvoiceId");
                            clientId = c.optInt("ClientId");
                            clientName.setText(c.optString("ClientName"));
                            clientCompany.setText(c.optString("Company"));
                            invoiceNumber.setText(c.optString("InvoiceNo"));
                            invoiceDate.setText(c.optString("InvoiceDate"));
                            dueDate.setText(c.optString("DueDate"));
                            billingAddress.setText(c.optString("CompanyAddress"));
                            subTotal.setText("\u20b9 "+c.optInt("Subtotal"));
                            Tax.setText("\u20b9 "+c.optInt("ServiceTax"));
                            Total.setText("\u20b9 "+c.optInt("TotalAmount"));
                            tvTotal.setText("\u20b9 "+c.optInt("TotalAmount"));
                            notes.setText(c.optString("Note"));

                        }
                    }
                    if(object.optString("api").equalsIgnoreCase("InvoiceList")) {
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
                                itemListAdapter = new InvoiceDetailItemListAdapter(this, clientsArrayList);
                                listviewItems.setAdapter(itemListAdapter);
                                listviewItems.addItemDecoration(new DividerItemDecoration(this));
                            } else {
                                //Toast.makeText(this,object.optString("res"),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    if(object.optString("api").equalsIgnoreCase("SendEmail")) {
                        if(object.optInt("resid")>0) {
                            Toast.makeText(this,"Mail sent successfully.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(this,"Something Wrong.",Toast.LENGTH_SHORT).show();
                        }
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
