package com.setblue.invoice.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.setblue.invoice.ClientListActivity;
import com.setblue.invoice.Fragments.AboutFragment;
import com.setblue.invoice.Fragments.ContactFragment;
import com.setblue.invoice.Fragments.CustomerFragment;
import com.setblue.invoice.InvoiceListActivity;
import com.setblue.invoice.LoginActivity;
import com.setblue.invoice.MainActivity;
import com.setblue.invoice.R;
import com.setblue.invoice.utils.CommonVariables;
import com.setblue.invoice.utils.MySessionManager;

import java.util.ArrayList;
import java.util.List;


public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private List<Item> data;
    MainActivity activity;
    MySessionManager session;
    private FragmentManager fragmentManager;
    private AlertDialog.Builder builder;

    public ExpandableListAdapter(List<Item> data, MainActivity context) {
        this.data = data;
        activity = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        session = new MySessionManager(activity);
        builder = new android.support.v7.app.AlertDialog.Builder(activity);
        switch (type) {
            case HEADER:
                view = inflater.inflate(R.layout.list_header, parent, false);
                return new ListHeaderViewHolder(view);
            case CHILD:
                view = inflater.inflate(R.layout.list_drawer_child, parent, false);
                return new ListChidHeader(view);
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                itemController.header_title.setText(item.text);
                /*if (item.invisibleChildren == null) {
                    itemController.btn_expand_toggle.setVisibility(View.INVISIBLE);//.setImageResource(R.drawable.plus);
                } else {
                    itemController.btn_expand_toggle.setVisibility(View.VISIBLE);
                    itemController.btn_expand_toggle.setImageResource(R.drawable.ic_chevron_right_black_24dp);
                }*/
                itemController.mLl_whole.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.text.equalsIgnoreCase("HOME")) {
                            activity.drawerLayout.closeDrawer(Gravity.LEFT);
                            activity.finish();
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        } else if (item.text.equalsIgnoreCase("CLIENTS")) {
                            activity.drawerLayout.closeDrawer(Gravity.LEFT);
                            activity.startActivity(new Intent(activity, ClientListActivity.class).putExtra("from", "home"));
                        } else if (item.text.equalsIgnoreCase("INVOICE")) {
                            activity.drawerLayout.closeDrawer(Gravity.LEFT);
                            activity.startActivity(new Intent(activity, InvoiceListActivity.class));
                        } else if (item.text.equalsIgnoreCase("ABOUT US")) {
                            activity.drawerLayout.closeDrawer(Gravity.LEFT);
                            AboutFragment fragment = new AboutFragment();
                            replaceFragment(fragment);
                        } else if (item.text.equalsIgnoreCase("CONTACT US")) {
                            activity.drawerLayout.closeDrawer(Gravity.LEFT);
                            ContactFragment fragment = new ContactFragment();
                            replaceFragment(fragment);
                        } else if (item.text.equalsIgnoreCase("LOGOUT")) {
                            activity.drawerLayout.closeDrawer(Gravity.LEFT);
                            builder.setTitle(CommonVariables.TAG);
                            builder.setMessage("Are You Sure to Logout?");
                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    session.Logout();
                                    activity.startActivity(new Intent(activity, LoginActivity.class));
                                    activity.finish();

                                }
                            });
                            builder.show();


                        } else if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.ic_chevron_right_black_24dp);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.ic_chevron_right_black_24dp);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case CHILD:
                final ListChidHeader child = (ListChidHeader) holder;
                child.header_title.setText(item.text);

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private static class ListChidHeader extends RecyclerView.ViewHolder {
        public TextView header_title;

        public ListChidHeader(View itemView) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
        }
    }


    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView header_title;
        public ImageView btn_expand_toggle;
        LinearLayout mLl_whole;
        public Item refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            mLl_whole = (LinearLayout) itemView.findViewById(R.id.ll_whole);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
        }
    }

    public static class Item {
        public int type, projectId;
        public String text;
        public List<Item> invisibleChildren;

        public Item(int type, int projectid, String text) {
            this.projectId = projectid;
            this.type = type;
            this.text = text;
        }
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack("My Fragment");
            ft.commit();
            // fragmentManager.beginTransaction()
            //         .replace(R.id.content_frame, fragment).setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).addToBackStack(null).commit();

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
}