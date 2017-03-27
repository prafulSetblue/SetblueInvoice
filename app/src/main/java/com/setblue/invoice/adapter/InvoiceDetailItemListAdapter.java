package com.setblue.invoice.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.setblue.invoice.R;
import com.setblue.invoice.model.InvoiceItem;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonVariables;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InvoiceDetailItemListAdapter extends RecyclerView.Adapter<InvoiceDetailItemListAdapter.InvoiceHolder> {

	private ArrayList<InvoiceItem> listInvoice;
	private Context context;
	AppCompatActivity activity = (AppCompatActivity) context;
	private AdapterView.OnItemClickListener  onItemClickListener;
	int pos;
	AQuery aq;
	private AlertDialog.Builder builder;


	public InvoiceDetailItemListAdapter(Context context, ArrayList<InvoiceItem> listInvoice) {
		this.listInvoice = listInvoice;
		this.context = context;
	}

	@Override
	public InvoiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		//View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, null);
		LayoutInflater inflater = LayoutInflater.from(context);
		final View view = inflater.inflate(R.layout.list_invoice_item, parent, false);
		InvoiceHolder rcv = new InvoiceHolder(view,this);
		builder = new AlertDialog.Builder(context);
		aq = new AQuery(context);
		return rcv;
	}
	@Override
	public void onBindViewHolder(InvoiceHolder holder, int position) {
		InvoiceItem item = listInvoice.get(position);
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


		private final ImageView delete;
		private final TextView term;
		private InvoiceDetailItemListAdapter mAdapter;
		public TextView title;
		public TextView total;
		public TextView qty;
		public TextView rate;
		private Intent i;

		public InvoiceHolder(View itemView, InvoiceDetailItemListAdapter mAdapter) {
			super(itemView);
			this.mAdapter = mAdapter;
			title = (TextView) itemView.findViewById(R.id.tv_item_name);
			total = (TextView) itemView.findViewById(R.id.tv_total);
			qty = (TextView) itemView.findViewById(R.id.tv_invoice_qty);
			rate = (TextView) itemView.findViewById(R.id.tv_invoice_rate);
			term = (TextView) itemView.findViewById(R.id.tv_item_term);
			delete = (ImageView)itemView.findViewById(R.id.iv_delete);
		}


		@Override
		public void onClick(View v) {


		}

		public void setDateToView(InvoiceItem item, int position) throws Exception {
			delete.setVisibility(View.GONE);
			Double duration = Double.parseDouble(item.getTerm())/12;
			Double Total = item.getQty()*item.getRate()*duration;
			title.setText(item.getItemName());
			total.setText("\u20b9 "+new DecimalFormat("##.##").format(Total));
			qty.setText(""+item.getQty());
			rate.setText(""+item.getRate());
			term.setText(item.getTerm());



		}

	}



}