package com.setblue.invoice.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.setblue.invoice.ClientDetailActivity;
import com.setblue.invoice.R;
import com.setblue.invoice.model.Clients;
import com.setblue.invoice.model.Invoice;

import java.util.ArrayList;
import java.util.Locale;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ClientHolder> {

	private final ArrayList<Clients> listClient;
	private ArrayList<Clients> searchlistInvoice;
	private AppCompatActivity context;
	private AdapterView.OnItemClickListener  onItemClickListener;
	String from;

	public ClientListAdapter(AppCompatActivity context, ArrayList<Clients> listClient,String from) {
		this.listClient = listClient;
		this.context = context;
		this.from = from;
		this.searchlistInvoice = new ArrayList<Clients>();
		this.searchlistInvoice.addAll(listClient);
	}

	@Override
	public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		//View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, null);
		LayoutInflater inflater = LayoutInflater.from(context);
		final View view = inflater.inflate(R.layout.adapter_client_item, parent, false);
		ClientHolder rcv = new ClientHolder(view,this);
		return rcv;
	}
	@Override
	public void onBindViewHolder(ClientHolder holder, int position) {
		Clients item = listClient.get(position);
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
		return this.listClient.size();
	}

	public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void onItemHolderClick(ClientHolder holder) {
		if (onItemClickListener != null)
			onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
	}
	public class ClientHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private final TextView email;
		private final TextView comapny;
		private ClientListAdapter mAdapter;
		public TextView title;
		private Intent i;

		public ClientHolder(View itemView, ClientListAdapter mAdapter) {
			super(itemView);
			this.mAdapter = mAdapter;
			title = (TextView) itemView.findViewById(R.id.tv_client_name);
			comapny = (TextView) itemView.findViewById(R.id.tv_company);
			email = (TextView) itemView.findViewById(R.id.tv_client_email);

			itemView.setOnClickListener(this);
		}


		@Override
		public void onClick(View v) {
			if(from.equalsIgnoreCase("invoice")) {
				Intent output = new Intent();
				output.putExtra("id",listClient.get(getPosition()).getId());
				output.putExtra("name",listClient.get(getPosition()).getClient_name());
				output.putExtra("email",listClient.get(getPosition()).getEmail());
				output.putExtra("mobile",listClient.get(getPosition()).getMobile());
				output.putExtra("company",listClient.get(getPosition()).getCompany());
				output.putExtra("address",listClient.get(getPosition()).getAddress());
				output.putExtra("pincode",listClient.get(getPosition()).getPincode());
				output.putExtra("city",listClient.get(getPosition()).getCity());
				output.putExtra("state",listClient.get(getPosition()).getState());
				output.putExtra("country",listClient.get(getPosition()).getCountry());
				context.setResult(Activity.RESULT_OK, output);
				context.finish();
				context.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			}
			else if(from.equalsIgnoreCase("home")){
				i = new Intent(context, ClientDetailActivity.class);
				i.putExtra("id",listClient.get(getPosition()).getId());
				context.startActivity(i);
			}
		}

		public void setDateToView(Clients item, int position) throws Exception {
			if(item.getCompany().equalsIgnoreCase("") || item.getCompany().equalsIgnoreCase("null")){

				comapny.setVisibility(View.GONE);

			}else {
				comapny.setVisibility(View.VISIBLE);
				comapny.setText(item.getCompany());

			}
			title.setText(item.getClient_name());
			email.setText(item.getEmail());

		}


	}
	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		listClient.clear();
		if (charText.length() == 0) {
			listClient.addAll(searchlistInvoice);
		}
		else
		{
			for (Clients wp : searchlistInvoice)
			{
				if (wp.getClient_name().toLowerCase(Locale.getDefault()).contains(charText))
				{
					listClient.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}



}