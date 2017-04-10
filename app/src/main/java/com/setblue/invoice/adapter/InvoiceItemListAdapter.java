package com.setblue.invoice.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.setblue.invoice.InvoiceDetailActivity;
import com.setblue.invoice.InvoiceItemActivity;
import com.setblue.invoice.R;
import com.setblue.invoice.components.CatLoadingView;
import com.setblue.invoice.model.Invoice;
import com.setblue.invoice.model.InvoiceItem;
import com.setblue.invoice.utils.Apis;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InvoiceItemListAdapter extends RecyclerView.Adapter<InvoiceItemListAdapter.InvoiceHolder> {

	private ArrayList<InvoiceItem> listInvoice;
	private Context context;
	AppCompatActivity activity = (AppCompatActivity) context;
	private AdapterView.OnItemClickListener  onItemClickListener;
	int pos;
	AQuery aq;
	private AlertDialog.Builder builder;
	private CatLoadingView mView;


	public InvoiceItemListAdapter(Context context, ArrayList<InvoiceItem> listInvoice) {
		this.listInvoice = listInvoice;
		this.context = context;
	}

	@Override
	public InvoiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		//View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, null);
		LayoutInflater inflater = LayoutInflater.from(context);
		final View view = inflater.inflate(R.layout.list_invoice_item, parent, false);
		InvoiceHolder rcv = new InvoiceHolder(view,this);
		builder = new android.support.v7.app.AlertDialog.Builder(context);
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


		private final ImageView detele;
		private final TextView term;
		private InvoiceItemListAdapter mAdapter;
		public TextView title;
		public TextView total;
		public TextView qty;
		public TextView rate;
		private Intent i;

		public InvoiceHolder(View itemView, InvoiceItemListAdapter mAdapter) {
			super(itemView);
			this.mAdapter = mAdapter;
			title = (TextView) itemView.findViewById(R.id.tv_item_name);
			total = (TextView) itemView.findViewById(R.id.tv_total);
			qty = (TextView) itemView.findViewById(R.id.tv_invoice_qty);
			rate = (TextView) itemView.findViewById(R.id.tv_invoice_rate);
			term = (TextView) itemView.findViewById(R.id.tv_item_term);
			detele = (ImageView) itemView.findViewById(R.id.iv_delete);

			detele.setOnClickListener(this);
		}


		@Override
		public void onClick(View v) {
			pos = getPosition();
			builder.setTitle(CommonVariables.TAG);
			builder.setMessage("Are You Sure Delete This Item?");
			builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {



				}
			});
			builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					itemRemove(getPosition());

				}
			});
			builder.show();

		}

		public void setDateToView(InvoiceItem item, int position) throws Exception {
			Double duration = Double.parseDouble(item.getTerm())/12;
			Double Total = item.getQty()*item.getRate()*duration;
			title.setText(item.getItemName());
			total.setText("\u20b9 "+new DecimalFormat("##.##").format(Total));
			qty.setText(""+item.getQty());
			rate.setText(""+item.getRate());
			term.setText(item.getTerm());


		}

	}

	private void itemRemove(int position) {
		mView = new CatLoadingView();
		mView.show(activity.getSupportFragmentManager(), "load");
		String url = Apis.RemoveInvoiceItem+"id="+listInvoice.get(position).getInvocieItemId();
		//Make Asynchronous call using AJAX method
		Log.d(CommonVariables.TAG,"Url: "+url);
		aq.ajax(url, String.class, this,"jsonCallback");
		//activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
	}

	public void jsonCallback(String url, String json, AjaxStatus status){

		if(mView != null)
			mView.dismiss();
		if(json != null){
			//successful ajax call
			Log.d(CommonVariables.TAG,json.toString());
			try {
				JSONObject object = new JSONObject(json);
				if(object.optInt("resid")>0) {
					notifyDataSetChanged();
					listInvoice.remove(pos);
					updateList(listInvoice);
					notifyDataSetChanged();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}else{
			//ajax error
			Log.d(CommonVariables.TAG,""+status.getCode());
		}
	}
	private void updateList(ArrayList<InvoiceItem> data) {
		this.listInvoice = data;
		notifyDataSetChanged();
	}
}