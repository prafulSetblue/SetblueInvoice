package com.setblue.invoice.model;

/**
 * Created by praful on 02-Feb-17.
 */

public class Invoice {
    private String id;
    private String client_name;
    private String total;
    private String invoice_number;
    private String date;
    private String company;


    public Invoice(String id, String client_name, String total, String invoice_number, String date, String company) {
        this.id = id;
        this.client_name = client_name;
        this.total = total;
        this.invoice_number = invoice_number;
        this.date = date;
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
