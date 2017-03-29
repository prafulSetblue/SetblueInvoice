package com.setblue.invoice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.AbstractAQuery;
import com.androidquery.callback.AjaxStatus;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.CFFFont;
import com.itextpdf.text.pdf.PdfDiv;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.text.pdf.codec.Base64;
import com.setblue.invoice.adapter.InvoiceDetailItemListAdapter;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.setblue.invoice.utils.CommonVariables.REQUEST_RECEIVE_MESSAGE;


public class InvoicePreviewActivity extends AppCompatActivity implements View.OnClickListener, OnPageChangeListener, OnLoadCompleteListener {


    private ImageView back;
    private ImageView editClient;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Document document;
    private File file;
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final int PERMISSION_CODE = 42042;
    private PDFView pdfView;
    private Font catFont;
    private static Font invoiceFont;
    private int invoiceID;
    private AQuery aq;
    private String stInvoiceDate;
    private String stDuedate;
    private String stInvoiceNo;
    private String stAddress;
    private String stCity;
    private String stState;
    private String stCountry;
    private String stPincode;
    private String stSubtotal;
    private String stServiceTax;
    private String stTotalAmount;
    private String stNote;
    private String stMobile;
    private String stComapany;
    private ImageView mail;
    private int stClientID = 0;
    private String stMail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                    Font.BOLD);
            invoiceFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                    Font.NORMAL);
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);


            }
            setContentView(R.layout.activity_invoive_preview);
            aq = new AQuery(this);
            invoiceID = getIntent().getIntExtra("invoiceID",0);
            setUpActionBar();
            init();
            requestPermissionSMSReceive();
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
        back.setOnClickListener(this);
        mail = (ImageView) tb.findViewById(R.id.iv_mail);
        mail.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if(v == back){
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);



        }
        else  if(v == mail){

            new SendMail().execute();


           /* fragment = new CustomerFragment();
            replaceFragment(fragment);*/

        }
    }
    private void init(){

        pdfView = (PDFView)findViewById(R.id.pdfView);


    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void loadComplete(int nbPages) {

    }
    private void setData(){
        String url = Apis.InvoiceDetail+"id="+ getIntent().getIntExtra("id",0);
        Log.d(CommonVariables.TAG,"Url: "+url);
        aq.progress(new ProgressDialog(this,R.style.CustomProgressDialog)).ajax(url, String.class, this,"jsonCallback");
    }
    private void ItemList() {
        String url = Apis.InvoiceItemList+"InvoiceId="+getIntent().getIntExtra("id",0);
        //Make Asynchronous call using AJAX method
        Log.d(CommonVariables.TAG,"Url: "+url);
        aq.progress(new ProgressDialog(this,R.style.CustomProgressDialog)).ajax(url, String.class, this,"jsonCallback");

    }
    public void  clientDetail(){
        String url = Apis.ClientDetails+"id="+stClientID;
        Log.d(CommonVariables.TAG,"Url: "+url);
        aq.progress(new ProgressDialog(this,R.style.CustomProgressDialog)).ajax(url, String.class, this,"jsonCallback");

    }

    private class SendMail extends AsyncTask<String, String, String> {
        private Mail m;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(InvoicePreviewActivity.this,
                    "Setblue Invoice",
                    "Sending Mail..");
        }

        @Override
        protected String doInBackground(String... params) {
            String[] toArr = {stMail};
           // m = new Mail("praful@setblue.com","pflptl1010",toArr);
            m = new Mail("pusptl776@gmail.com","pflptl1010",toArr);
            try {
                m.addAttachment("/storage/emulated/0/pdfdemo/invoice.pdf",stInvoiceNo+".pdf");


            } catch(Exception e) {
                //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                Log.e("MailApp", "Could not send email", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            try {
                if(m.send()) {
                    Toast.makeText(InvoicePreviewActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(InvoicePreviewActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private class AsyncTaskpdf extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        JSONArray JsonArray;
        public AsyncTaskpdf(JSONArray jsonArray) {
            JsonArray = jsonArray;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(InvoicePreviewActivity.this,
                    "Loading",
                    "Wait for  Preview");
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                createPdf(JsonArray);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            pdfView.fromFile(file)
                    .defaultPage(1)
                    //.onPageChange((OnPageChangeListener) this)
                    .enableAnnotationRendering(true)
                    //.onLoad((OnLoadCompleteListener) this)
                   // .scrollHandle(new DefaultScrollHandle(InvoicePreviewActivity.this))
                    .load();

        }
    }

    private void createPdf(JSONArray jsonArray) throws FileNotFoundException, DocumentException, IOException {

        File pdfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i(CommonVariables.TAG, pdfFolder.toString());
        }
        Image image = null;
        try {
            image = Image.getInstance(convertImageToByte());
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Create time stamp
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        file = new File(pdfFolder,"invoice" + ".pdf");
        Log.i(CommonVariables.TAG, file.toString());
        OutputStream output = new FileOutputStream(file);
        document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, output);
        //Inserting Pdf file
        PdfPTable table=new PdfPTable(3);
        PdfPCell cell = new PdfPCell (new Paragraph ("Invoice # "+stInvoiceNo+"\n" +
                "Invoice Date: "+stInvoiceDate+"\n" +
                "Due Date: "+stDuedate+""));

        cell.setColspan (3);
        cell.setHorizontalAlignment (Element.ALIGN_LEFT);
        cell.setPadding (10.0f);
        cell.setBackgroundColor(new BaseColor(247, 247, 248));

        table.addCell(cell);
        table.setWidthPercentage(100);

       // table.addCell("Invoice # V21515107\nInvoice Date: 22/03/2017\nDua Date: 21/04/2017");
       // table.setSpacingBefore(30.0f);       // Space Before table starts, like margin-top in CSS
        //table.setSpacingAfter(30.0f);        // Space After table starts, like margin-Bottom in CSS

        //Inserting List in PDF
        List list=new List(true,30);
        list.add(new ListItem("Items1"));
        list.add(new ListItem("Items2"));
        list.add(new ListItem("Items3"));

        //Text formating in PDF
        Chunk chunk=new Chunk("Please Note:");
        //chunk.setUnderline(+1f,-2f);//1st co-ordinate is for line width,2nd is space between

        //chunk1.setBackground(new BaseColor (17, 46, 193));

        document.open();
        //document.add(image);

        document.addHeader("Setblue","test");
        //document.addTitle("Setblue");

        Paragraph p = new Paragraph("V2IDEAS.COM",catFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        Paragraph address = new Paragraph("801, Empire State builing,\nNear World Trad Center,\nUdhna Darwaja,Ring Road,\nSurat-395002 (GJ) India\nPh - 0261 4015401",invoiceFont);
        address.setAlignment(Element.ALIGN_RIGHT);
        address.setSpacingAfter(10.0f);

        Paragraph serveceTax = new Paragraph("Servce Tax No: ABAPT4600JSD001\nPan No: ABAPT4600J",invoiceFont);
        serveceTax.setAlignment(Element.ALIGN_RIGHT);
        serveceTax.setSpacingAfter(10.0f);


        Paragraph p1 = new Paragraph("Invoice To",invoiceFont);
        p1.setAlignment(Element.ALIGN_LEFT);

        Paragraph clientname = new Paragraph(stComapany,catFont);
        clientname.setAlignment(Element.ALIGN_LEFT);
        Paragraph clientAddress = new Paragraph(stAddress+",\n"+stCity+",\nTel - "+stMobile+"",invoiceFont);
        clientAddress.setAlignment(Element.ALIGN_LEFT);
        clientAddress.setSpacingAfter(10.0f);
        // Second parameter is the number of the chapter
       /* Paragraph p = new Paragraph("V2IDIEAS.COM");
        Paragraph p1 = new Paragraph("demo");
        p.setAlignment(Element.ALIGN_RIGHT);*/
        document.add(p);
        document.add(address);
        document.add(serveceTax);
        // document.add(new Paragraph(Html.fromHtml(invoice).toString()));
        document.add(table);
        document.add(p1);
       // document.add(new Paragraph("Invoice To"));
        document.add(clientname);
        document.add(clientAddress);

        createTable(document,jsonArray);
        createTotalTable(document);

        document.add(Chunk.NEWLINE);
        document.add(chunk);

        document.add(Chunk.NEWLINE); //Something Like HTML
        //document.newPage();
        document.add(list);
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("For,",invoiceFont));
        document.add(image);
        document.add(new Paragraph("V2IDEAS.COM",invoiceFont));

        document.close();
        output.close();

        Log.i(CommonVariables.TAG, file.toString());





    }
    private void createTable(Document doc, JSONArray jsonArray)
            throws BadElementException {

        PdfPTable table = new PdfPTable(6);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Description",catFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding (10.0f);
        c1.setColspan(2);
        table.addCell(c1);


        c1 = new PdfPCell(new Phrase("Term(In Months)",catFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding (10.0f);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Qty",catFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding (10.0f);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Rate",catFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding (10.0f);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Total",catFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding (10.0f);
        table.addCell(c1);

        table.setHeaderRows(1);
        for(int i =0; i<jsonArray.length(); i++){
            JSONObject c= jsonArray.optJSONObject(i);

            Double duration = Double.parseDouble(c.optString("Term"))/12;
            Double Total = c.optInt("Qty")*c.optInt("Rate")*duration;
            Paragraph phrase = new Paragraph(c.optString("ItemName"),invoiceFont);
            phrase.setFont(invoiceFont);
            c1 = new PdfPCell(phrase);
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setPadding (05.0f);
            c1.setColspan(2);
            table.addCell(c1);
            c1 = new PdfPCell(new Phrase(""+c.optString("Term"),invoiceFont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPadding (05.0f);
            table.addCell(c1);
            c1 = new PdfPCell(new Phrase(""+c.optInt("Qty"),invoiceFont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPadding (05.0f);
            table.addCell(c1);
            c1 = new PdfPCell(new Phrase(""+c.optInt("Rate"),invoiceFont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPadding (05.0f);
            table.addCell(c1);
            c1 = new PdfPCell(new Phrase(""+new DecimalFormat("##.##").format(Total),invoiceFont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPadding (05.0f);
            table.addCell(c1);
        }






        try {
           // table.setWidthPercentage  (new float[]{ 150, 100,100,100 },null);
            table.setWidthPercentage(100);
            doc.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    public void createTotalTable(Document doc)
            throws BadElementException {

        PdfPTable table = new PdfPTable(6);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Sub Total",invoiceFont));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setPadding (05.0f);
        c1.setColspan(5);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(stSubtotal,invoiceFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding (05.0f);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("15% New Service Tax(Nov-2015)",invoiceFont));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setPadding (05.0f);
        c1.setColspan(5);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(stServiceTax,invoiceFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding (05.0f);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Credit",invoiceFont));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setPadding (05.0f);
        c1.setColspan(5);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("",invoiceFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding (05.0f);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Total",catFont));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setPadding (05.0f);
        c1.setColspan(5);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(stTotalAmount,catFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding (05.0f);
        table.addCell(c1);




        try {
           // table.setWidthPercentage  (new float[]{ 150, 100,100,100 },null);
           // table.setLockedWidth(true);
            table.setWidthPercentage(100);
            doc.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    public byte[] convertImageToByte(){
        byte[] data = null;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.stamp);
        //Bitmap bitmap = getBitmapFromURL("http://theprintshop.ae/wp-content/uploads/2015/06/1.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data = baos.toByteArray();
        return data;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    private void requestPermissionSMSReceive() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_RECEIVE_MESSAGE);
            }
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    READ_EXTERNAL_STORAGE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{READ_EXTERNAL_STORAGE},
                        PERMISSION_CODE
                );


            }
        }
    }
    @SuppressLint("Override")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RECEIVE_MESSAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                }
                return;
            }
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                }
                return;
            }

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
                            stClientID = c.optInt("ClientId");
                            stMobile = c.optString("MobileNo");
                            stInvoiceNo = c.optString("InvoiceNo");
                            stInvoiceDate = c.optString("InvoiceDate");
                            stDuedate = c.optString("DueDate");
                            stAddress = c.optString("CompanyAddress");
                            stCity = c.optString("City");
                            stState = c.optString("State");
                            stCountry = c.optString("Country");
                            stPincode = c.optString("Pincode");
                            stSubtotal = c.optString("Subtotal");
                            stServiceTax = c.optString("ServiceTax");
                            stTotalAmount = c.optString("TotalAmount");
                            stNote = c.optString("Note");
                            stComapany = c.optString("Company");
                            ItemList();
                            clientDetail();


                        }
                    }
                    if(object.optString("api").equalsIgnoreCase("InvoiceList")) {
                        if(object.optInt("resid")>0) {
                            if (object.optJSONArray("resData").length() != 0) {
                                JSONArray jsonArray = object.optJSONArray("resData");
                                new AsyncTaskpdf(jsonArray).execute();

                            } else {
                                //Toast.makeText(this,object.optString("res"),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    if(object.optString("api").equalsIgnoreCase("ClientDetails")) {
                        if(object.optInt("resid")>0) {
                            JSONArray jsonArray = object.optJSONArray("resData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.optJSONObject(i);
                                stMail = c.optString("Email");
                            }
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

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
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
