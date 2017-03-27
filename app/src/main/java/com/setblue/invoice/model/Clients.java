package com.setblue.invoice.model;

/**
 * Created by praful on 02-Feb-17.
 */

public class Clients {
    private int id;
    private String client_name;
    private String email;
    private String mobile;
    private String company;
    private String address;
    private String pincode;
    private String city;
    private String state;
    private String country;


    public Clients(int id, String client_name, String email,String mobile, String company, String address, String pincode
            , String city, String state, String country) {
        this.id = id;
        this.client_name = client_name;
        this.email = email;
        this.mobile = mobile;
        this.company = company;
        this.address = address;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
