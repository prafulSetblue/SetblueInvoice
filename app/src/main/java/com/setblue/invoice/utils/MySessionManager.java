package com.setblue.invoice.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

public class MySessionManager
{
    public Context context;

    public MySessionManager(Context context)
    {
	this.context = context;
    }

    private final String PREF_USER_INFO = "InvoicePreference";
    private final String PREF_INVOICE_INFO = "InvoiceNumberPreference";
    private final String LOGIN = CommonVariables.KEY_IS_LOGGED_IN;
    private final String USER_ID =CommonVariables.KEY_CUSTOMER_ID;
    private final String USER_INVOICE =CommonVariables.KEY_CUSTOMER_INVOICE;


    public void setUserLogIn(boolean isLogin)
    {
	if (context != null)
	{
	    SharedPreferences LoginPref = context.getSharedPreferences(PREF_USER_INFO, context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = LoginPref.edit();
	    editor.putBoolean(LOGIN, isLogin);
	    editor.commit();
	}
    }

    public boolean isUserLogIn()
    {
	if (context != null)
	{
	    SharedPreferences LoginPref = context.getSharedPreferences(PREF_USER_INFO, context.MODE_PRIVATE);
	    return LoginPref.getBoolean(LOGIN, false);
	}
	else
	{
	    return false;
	}
    }

    public void setUserId(String userId)
    {
	if (context != null)
	{
	    SharedPreferences LoginPref = context.getSharedPreferences(PREF_USER_INFO, context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = LoginPref.edit();
	    editor.putString(USER_ID, userId);
	    editor.commit();
	}
    }

    public String getUserId()
    {
	if (context != null)
	{
	    SharedPreferences LoginPref = context.getSharedPreferences(PREF_USER_INFO, context.MODE_PRIVATE);
	    return LoginPref.getString(USER_ID, "");
	}
	else
	{
	    return "";
	}
    }

    public void setNumber(int number)
    {
	if (context != null)
	{
	    SharedPreferences LoginPref = context.getSharedPreferences(PREF_INVOICE_INFO, context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = LoginPref.edit();
	    editor.putInt(USER_INVOICE, number);
	    editor.commit();
	}
    }

    public int getNumber()
    {
	if (context != null)
	{
	    SharedPreferences LoginPref = context.getSharedPreferences(PREF_INVOICE_INFO, context.MODE_PRIVATE);
	    return LoginPref.getInt(USER_INVOICE, 1);
	}
	else
	{
	    return 0;
	}
    }

	public void Logout()
	{
		SharedPreferences LoginPref = context.getSharedPreferences(PREF_USER_INFO, context.MODE_PRIVATE);
		SharedPreferences.Editor editor = LoginPref.edit();
		editor.clear();
		editor.commit();
	}


}
