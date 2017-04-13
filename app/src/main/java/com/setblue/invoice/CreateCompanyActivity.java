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
    RoundedImageView company_logo;

    private int REQUEST_WRITE_PERMISSION = 786;
    private byte[] byteArray;

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

        edit_company_logo = (ImageView)findViewById(R.id.iv_company_image);
        company_logo = (RoundedImageView)findViewById(R.id.company_logo);
        edit_company_logo.setOnClickListener(this);


    }
    private void setData(){

    }
    @Override
    public void onClick(View v) {
        if(v == back){
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }
        else if(v == edit_company_logo){

                requestPermission();

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

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            //textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                byteArray = out.toByteArray();
                company_logo.setImageBitmap(decoded);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void aync_multipart(){

        String url = "https://graph.facebook.com/me/photos";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("message", "Message");

//Simply put a byte[] to the params, AQuery will detect it and treat it as a multi-part post

        params.put("source", byteArray);

//Alternatively, put a File or InputStream instead of byte[]
//File file = getImageFile();
//params.put("source", file);

        AQuery aq = new AQuery(getApplicationContext());
        aq.ajax(url, params, JSONObject.class, this, "photoCb");
    }
    public void jsonCallback(String url, String json, AjaxStatus status){

           }
    @Override
    protected void onResume() {
        super.onResume();
        if(!CommonMethods.knowInternetOn(this)){
            CommonMethods.showInternetAlert(this);
        }

    }
}
