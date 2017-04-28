package com.setblue.invoice.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.setblue.invoice.ClientDetailActivity;
import com.setblue.invoice.R;
import com.setblue.invoice.model.Clients;
import com.setblue.invoice.model.InvoiceSummary;

import java.util.ArrayList;
import java.util.Locale;

public class InvoiceSummaryAdapter extends RecyclerView.Adapter<InvoiceSummaryAdapter.ClientHolder> {

	private final ArrayList<InvoiceSummary> invoiceSummaries;
	private AppCompatActivity context;
	private AdapterView.OnItemClickListener  onItemClickListener;
	String from;

	public InvoiceSummaryAdapter(AppCompatActivity context, ArrayList<InvoiceSummary> invoiceSummaries) {
		this.invoiceSummaries = invoiceSummaries;
		this.context = context;
		this.from = from;
	}

	@Override
	public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		//View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, null);
		LayoutInflater inflater = LayoutInflater.from(context);
		final View view = inflater.inflate(R.layout.adapter_invoice_summary, parent, false);
		ClientHolder rcv = new ClientHolder(view,this);
		return rcv;
	}
	@Override
	public void onBindViewHolder(ClientHolder holder, int position) {
		InvoiceSummary item = invoiceSummaries.get(position);
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
		return this.invoiceSummaries.size();
	}

	public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void onItemHolderClick(ClientHolder holder) {
		if (onItemClickListener != null)
			onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
	}
	public class ClientHolder extends RecyclerView.ViewHolder {


		private final TextView amount;
		private InvoiceSummaryAdapter mAdapter;
		public TextView title;
		private Intent i;

		public ClientHolder(View itemView, InvoiceSummaryAdapter mAdapter) {
			super(itemView);
			this.mAdapter = mAdapter;
			title = (TextView) itemView.findViewById(R.id.lbl_title);
			amount = (TextView) itemView.findViewById(R.id.tv_amount);

		}



		public void setDateToView(InvoiceSummary item, int position) throws Exception {
            if(position == getItemCount() -1){
                title.setTypeface(null, Typeface.BOLD);
                amount.setTypeface(null, Typeface.BOLD);
            }
			title.setText(item.getSummaryTitle());
			amount.setText("\u20b9 "+item.getAmount());
		}


	}



}