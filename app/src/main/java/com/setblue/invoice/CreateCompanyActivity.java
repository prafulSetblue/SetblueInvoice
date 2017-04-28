package com.setblue.invoice;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.setblue.invoice.components.CatLoadingView;
import com.setblue.invoice.components.RoundedImageView;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonMethods;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.ExceptionHandler;
import com.setblue.invoice.utils.MySessionManager;
import com.setblue.invoice.utils.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


public class CreateCompanyActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView back;
    private AQuery aq;
    private MySessionManager session;
    private ImageView edit_company_logo;
    ImageView company_logo;

    private int REQUEST_WRITE_PERMISSION = 786;
    private byte[] byteArray;
    private MaterialEditText edt_company_name;
    private MaterialEditText edt_mobile;
    private MaterialEditText edt_email;
    private MaterialEditText edt_address1;
    private MaterialEditText edt_address2;
    private MaterialEditText edt_city;
    private MaterialEditText edt_state;
    private MaterialEditText edt_country;
    private MaterialEditText edt_zip;
    private Button btn_save;
    private String stCompanyName;
    private String stMobile;
    private String stEmail;
    private String stAddress1;
    private String stAddress2;
    private String stCity;
    private String stState;
    private String stCountry;
    private String stZip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_create_company);
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
        edt_company_name = (MaterialEditText) findViewById(R.id.edt_company);
        edt_mobile = (MaterialEditText) findViewById(R.id.edt_mobile);
        edt_email = (MaterialEditText) findViewById(R.id.edt_email);
        edt_address1 = (MaterialEditText) findViewById(R.id.edt_address1);
        edt_address2 = (MaterialEditText) findViewById(R.id.edt_address2);
        edt_city = (MaterialEditText) findViewById(R.id.edt_city);
        edt_state = (MaterialEditText) findViewById(R.id.edt_state);
        edt_country = (MaterialEditText) findViewById(R.id.edt_country);
        edt_zip = (MaterialEditText) findViewById(R.id.edt_zip);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);


        edit_company_logo = (ImageView) findViewById(R.id.iv_company_image);
        company_logo = (ImageView) findViewById(R.id.company_logo);
        edit_company_logo.setOnClickListener(this);


    }

    private void setData() {

    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else if (v == edit_company_logo) {
            requestPermission();
        } else if (v == btn_save) {
            stCompanyName  = edt_company_name.getText().toString();
            stMobile  = edt_mobile.getText().toString();
            stEmail  = edt_email.getText().toString();
            stAddress1  = edt_address1.getText().toString();
            stAddress2  = edt_address2.getText().toString();
            stCity  = edt_city.getText().toString();
            stState  = edt_state.getText().toString();
            stCountry  = edt_country.getText().toString();
            stZip  = edt_zip.getText().toString();
            if (Validation.isEmptyEdittext(edt_company_name) && Validation.isEmptyEdittext(edt_mobile) && Validation.isEmptyEdittext(edt_email)
                    && Validation.isEmptyEdittext(edt_address1) && Validation.isEmptyEdittext(edt_address2) && Validation.isEmptyEdittext(edt_city) && Validation.isEmptyEdittext(edt_state)
                    && Validation.isEmptyEdittext(edt_country) && Validation.isEmptyEdittext(edt_zip)) {
                edt_company_name.setError("Enter Company Name");
                edt_mobile.setError("Enter Mobile Number");
                edt_email.setError("Enter Email Address");
                edt_address1.setError("Enter Address");
                edt_address2.setError("Enter Address");
                edt_city.setError("Enter City");
                edt_state.setError("Enter State");
                edt_country.setError("Enter Country");
                edt_zip.setError("Enter Pincode");
            } else if (Validation.isEmptyEdittext(edt_company_name)) {
                edt_company_name.setError("Enter Company Name");
            } else if (Validation.isEmptyEdittext(edt_mobile)) {
                edt_mobile.setError("Enter Mobile Number");
            } else if (Validation.isEmptyEdittext(edt_mobile)) {
                edt_email.setError("Enter Email Address");
            } else if (Validation.isEmptyEdittext(edt_address1)) {
                edt_address1.setError("Enter Address");
            }  else if (Validation.isEmptyEdittext(edt_address2)) {
                edt_address2.setError("Enter Address");
            } else if (Validation.isEmptyEdittext(edt_city)) {
                edt_city.setError("Enter City");
            } else if (Validation.isEmptyEdittext(edt_state)) {
                edt_state.setError("Enter State");
            } else if (Validation.isEmptyEdittext(edt_country)) {
                edt_country.setError("Enter Country");
            } else if (Validation.isEmptyEdittext(edt_zip)) {
                edt_zip.setError("Enter Pincode");
            } else if(stMobile.length() < 6) {
                edt_mobile.setError("Enter Valid Mobile Number");
            } else if(!Validation.isValidEmail(stEmail)) {
                edt_email.setError("Enter Valid Email Address");
            } else {
                Toast.makeText(this,"Created Company",Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            //textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                Bitmap resized = Bitmap.createScaledBitmap(decoded, 120, 120, true);
                int origWidth = resized.getWidth();
                int origHeight = resized.getHeight();
                Log.d(CommonVariables.TAG,"Height: "+origHeight);
                Log.d(CommonVariables.TAG,"Width: "+origWidth);
                byteArray = out.toByteArray();
                company_logo.setImageBitmap(resized);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void aync_multipart() {

        String url = "https://graph.facebook.com/me/photos";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("message", "Message");

//Simply put a byte[] to the params, AQuery will detect it and treat it as a multi-part post

        params.put("source", byteArray);

//Alternatively, put a File or InputStream instead of byte[]
//File file = getImageFile();
//params.put("source", file);


        aq.ajax(url, params, JSONObject.class, this, "jsonCallback");
    }

    public void jsonCallback(String url, String json, AjaxStatus status) {
        if (json != null) {

            Log.d(CommonVariables.TAG, json.toString());
            try {
                JSONObject object = new JSONObject(json);
                if (object.optInt("resid") > 0) {


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }else {
            //ajax error
            Log.d(CommonVariables.TAG, "" + status.getCode());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!CommonMethods.knowInternetOn(this)) {
            CommonMethods.showInternetAlert(this);
        }
    }
}
