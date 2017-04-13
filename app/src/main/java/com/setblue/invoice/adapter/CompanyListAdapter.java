package com.setblue.invoice.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.setblue.invoice.R;
import com.setblue.invoice.model.Company;

import java.util.ArrayList;

public class CompanyListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Company> areaArrayList = new ArrayList<Company>();
    LayoutInflater inflter;

    public CompanyListAdapter(Context applicationContext,ArrayList<Company> areaArrayList) {
        this.context = applicationContext;

        this.areaArrayList = areaArrayList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return areaArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public int getPosition(String value){
        int index = 0;

        for (int i=0;i<areaArrayList.size();i++){
            if (areaArrayList.get(i).getComapany_Name().equalsIgnoreCase(value)){
                index = i;
                break;
            }
        }
        return index;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_company, null);
        TextView names = (TextView) view.findViewById(R.id.textView);
        names.setText(areaArrayList.get(i).getComapany_Name());
        return view;
    }
}