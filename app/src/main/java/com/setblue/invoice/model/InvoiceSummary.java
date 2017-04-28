package com.setblue.invoice.model;

/**
 * Created by praful on 27-Apr-17.
 */

public class InvoiceSummary {

    String SummaryTitle;
    String Amount;
    String Currency;

    public InvoiceSummary(String summaryTitle, String amount, String currency) {
        SummaryTitle = summaryTitle;
        Amount = amount;
        Currency = currency;
    }

    public String getSummaryTitle() {
        return SummaryTitle;
    }

    public void setSummaryTitle(String summaryTitle) {
        SummaryTitle = summaryTitle;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }
}
