package com.setblue.invoice.model;

/**
 * Created by praful on 22-Mar-17.
 */

public class InvoiceItem {
    int InvocieItemId;
    int InvoiceId;
    String ItemName;
    String Term;
    int Qty;
    int Rate;
    boolean IsDeleted;

    public InvoiceItem(int invocieItemId, int invoiceId, String itemName, String term, int qty, int rate, boolean isDeleted) {
        InvocieItemId = invocieItemId;
        InvoiceId = invoiceId;
        ItemName = itemName;
        Term = term;
        Qty = qty;
        Rate = rate;
        IsDeleted = isDeleted;
    }

    public int getInvocieItemId() {
        return InvocieItemId;
    }

    public void setInvocieItemId(int invocieItemId) {
        InvocieItemId = invocieItemId;
    }

    public int getInvoiceId() {
        return InvoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        InvoiceId = invoiceId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getTerm() {
        return Term;
    }

    public void setTerm(String term) {
        Term = term;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int rate) {
        Rate = rate;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }
}
