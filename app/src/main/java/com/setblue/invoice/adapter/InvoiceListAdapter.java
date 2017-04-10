package com.setblue.invoice.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.setblue.invoice.ClientDetailActivity;
import com.setblue.invoice.InvoiceDetailActivity;
import com.setblue.invoice.R;
import com.setblue.invoice.model.Clients;
import com.setblue.invoice.model.Invoice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class InvoiceListAdapter extends RecyclerView.Adapter<InvoiceListAdapter.InvoiceHolder> {

	private final ArrayList<Invoice> listInvoice;
	private ArrayList<Invoice> searchlistInvoice;
	private Context context;
	AppCompatActivity activity = (AppCompatActivity) context;
	private AdapterView.OnItemClickListener  onItemClickListener;

	public InvoiceListAdapter(Context context, ArrayList<Invoice> listInvoice) {
		this.listInvoice = listInvoice;
		this.context = context;
		this.searchlistInvoice = new ArrayList<Invoice>();
		this.searchlistInvoice.addAll(listInvoice);
	}

	@Override
	public InvoiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		//View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, null);
		LayoutInflater inflater = LayoutInflater.from(context);
		final View view = inflater.inflate(R.layout.adapter_invoice_item, parent, false);
		InvoiceHolder rcv = new InvoiceHolder(view,this);
		return rcv;
	}
	@Override
	public void onBindViewHolder(InvoiceHolder holder, int position) {
		Invoice item = listInvoice.get(position);
		try {
			holder.setDateToView(item, position);
		} catch (Exception e) {
			e.printStackTrace();
		}


		/*String stImage = listOrders.get(position).toString();
		if(!stImage.equals("")) {
			Glide.with(context).load(listOrders.get(position).getImage_url()).placeholder(R.mipmap.no_image).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.Photo);
		}
		else{
			holder.Photo.setBackgroundResource(R.mipmap.no_image);
		}*/

			//holder.countryPhoto.setImageResource(listOrders.get(position).getPhoto());
	}

	@Override
	public int getItemCount() {
		return this.listInvoice.size();
	}

	public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void onItemHolderClick(InvoiceHolder holder) {
		if (onItemClickListener != null)
			onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
	}
	public class InvoiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


		private final TextView company;
		private InvoiceListAdapter mAdapter;
		public TextView title;
		public TextView total;
		public TextView invoice_number;
		public TextView invoice_date;
		private Intent i;

		public InvoiceHolder(View itemView, InvoiceListAdapter mAdapter) {
			super(itemView);
			this.mAdapter = mAdapter;
			title = (TextView) itemView.findViewById(R.id.tv_client_name);
			company = (TextView) itemView.findViewById(R.id.tv_company);
			total = (TextView) itemView.findViewById(R.id.tv_total);
			invoice_number = (TextView) itemView.findViewById(R.id.tv_invoice_number);
			invoice_date = (TextView) itemView.findViewById(R.id.tv_invoice_date);

			itemView.setOnClickListener(this);
		}


		@Override
		public void onClick(View v) {

			i = new Intent(context,InvoiceDetailActivity.class);
			i.putExtra("invoiceID",Integer.parseInt(listInvoice.get(getPosition()).getId()));
			context.startActivity(i);
			//activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
		}

		public void setDateToView(Invoice item, int position) throws Exception {
			if(item.getCompany().equalsIgnoreCase("") || item.getCompany().equalsIgnoreCase("null")){
				company.setVisibility(View.GONE);
			}else {
				company.setVisibility(View.VISIBLE);
				company.setText(item.getCompany());
			}
			String invoiceNo = item.getInvoice_number();
			if (invoiceNo.equalsIgnoreCase("null")) {
				invoiceNo = "";
				invoice_number.setVisibility(View.GONE);
			}
			else {
				invoice_number.setVisibility(View.VISIBLE);
				invoice_number.setText(invoiceNo);
			}
			title.setText(item.getClient_name());
			total.setText("\u20b9 "+item.getTotal());
			invoice_date.setText(item.getDate().toString());

		}
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		listInvoice.clear();
		if (charText.length() == 0) {
			listInvoice.addAll(searchlistInvoice);
		}
		else
		{
			for (Invoice wp : searchlistInvoice)
			{
				if (wp.getClient_name().toLowerCase(Locale.getDefault()).contains(charText) || wp.getInvoice_number().toLowerCase(Locale.getDefault()).contains(charText)|| wp.getCompany().toLowerCase(Locale.getDefault()).contains(charText))
				{
					listInvoice.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}