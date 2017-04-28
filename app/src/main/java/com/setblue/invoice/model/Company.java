package com.setblue.invoice.model;

/**
 * Created by praful on 13-Apr-17.
 */

public class Company {
    String Comapany_Name;
    int Company_ID;
    String Address;

    public Company(String comapany_Name, int company_ID) {
        Comapany_Name = comapany_Name;
        Company_ID = company_ID;
    }

    public Company(String comapany_Name, int company_ID, String address) {
        Comapany_Name = comapany_Name;
        Company_ID = company_ID;
        Address = address;
    }

    public String getComapany_Name() {
        return Comapany_Name;
    }

    public void setComapany_Name(String comapany_Name) {
        Comapany_Name = comapany_Name;
    }

    public int getCompany_ID() {
        return Company_ID;
    }

    public void setCompany_ID(int company_ID) {
        Company_ID = company_ID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
