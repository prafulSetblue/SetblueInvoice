package com.setblue.invoice;


import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.setblue.invoice.Fragments.CustomerFragment;
import com.setblue.invoice.Fragments.InvoiceFragment;
import com.setblue.invoice.components.NavDrawerView;
import com.setblue.invoice.utils.CommonMethods;
import com.setblue.invoice.utils.ExceptionHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    public DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private LinearLayout newCustomer;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private LinearLayout newInvoice;
    String from = "null";
    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_main);

            init();
            setUpActionBar();
            setDrawer();
            from = getIntent().getStringExtra("from");
            if(from.equalsIgnoreCase("clientlist")){
                fragment = new CustomerFragment();
                replaceFragment(fragment);
            }
            if (from.equalsIgnoreCase("invoicelist")){
                newInvoice.performClick();
            }
            if (from.equalsIgnoreCase("ClientDetail")){
                newInvoice.performClick();
            }

        }
        catch (Exception e){

        }
    }


    private void init() {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        newCustomer = (LinearLayout) findViewById(R.id.ll_customer);
        newCustomer.setOnClickListener(this);
        newInvoice = (LinearLayout) findViewById(R.id.ll_invoice);
        newInvoice.setOnClickListener(this);


    }

    private void setUpActionBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setHomeButtonEnabled(false);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                tb,
                R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();


    }

    private void setDrawer() {
        NavDrawerView drawerView = new NavDrawerView(this);
        navigationView.addView(drawerView);
        drawerView.setMenuData();
    }
    @Override
    public void onClick(View v) {
        if(v == newCustomer){
            fragment = new CustomerFragment();
            replaceFragment(fragment);
        }
        else if(v == newInvoice){
            fragment = new InvoiceFragment(getIntent().getIntExtra("id",0),from);
            replaceFragment(fragment);
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
    public void onBackPressed() {
        if (exit) {
            //System.exit(0);
            finish();
            moveTaskToBack(true);
        } else {
            Toast.makeText(this, "Press once again to exit app.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

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
