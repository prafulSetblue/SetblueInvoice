package com.setblue.invoice.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.setblue.invoice.ClientDetailActivity;
import com.setblue.invoice.CompanyDetailActivity;
import com.setblue.invoice.R;
import com.setblue.invoice.model.Clients;
import com.setblue.invoice.model.Company;

import java.util.ArrayList;
import java.util.Locale;

public class Company1ListAdapter extends RecyclerView.Adapter<Company1ListAdapter.ClientHolder> {

	private final ArrayList<Company> listCompany;
	private ArrayList<Company> searchlistComapny;
	private AppCompatActivity context;
	private AdapterView.OnItemClickListener  onItemClickListener;
	String from;

	public Company1ListAdapter(AppCompatActivity context, ArrayList<Company> listCompany, String from) {
		this.listCompany = listCompany;
		this.context = context;
		this.from = from;
		this.searchlistComapny = new ArrayList<Company>();
		this.searchlistComapny.addAll(listCompany);
	}

	@Override
	public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		//View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, null);
		LayoutInflater inflater = LayoutInflater.from(context);
		final View view = inflater.inflate(R.layout.adapter_company_item, parent, false);
		ClientHolder rcv = new ClientHolder(view,this);
		return rcv;
	}
	@Override
	public void onBindViewHolder(ClientHolder holder, int position) {
		Company item = listCompany.get(position);
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
		return this.listCompany.size();
	}

	public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void onItemHolderClick(ClientHolder holder) {
		if (onItemClickListener != null)
			onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
	}
	public class ClientHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


		private final TextView address;
		public TextView title;
		private Intent i;

		public ClientHolder(View itemView, Company1ListAdapter mAdapter) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.tv_company_name);
			address = (TextView) itemView.findViewById(R.id.tv_company_address);

			itemView.setOnClickListener(this);
		}


		@Override
		public void onClick(View v) {
            Intent i = new Intent(context, CompanyDetailActivity.class);
            i.putExtra("id",listCompany.get(getAdapterPosition()).getCompany_ID());
            context.startActivity(i);
            context.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

		}

		public void setDateToView(Company item, int position) throws Exception {

			title.setText(item.getComapany_Name());
			address.setText(item.getAddress());

		}


	}
	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		listCompany.clear();
		if (charText.length() == 0) {
			listCompany.addAll(searchlistComapny);
		}
		else
		{
			for (Company wp : searchlistComapny)
			{
				if (wp.getComapany_Name().toLowerCase(Locale.getDefault()).contains(charText))
				{
					listCompany.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
}