package hu.pe.yummykart.yummykart;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DataAdapterForOrderHistory extends RecyclerView.Adapter<DataAdapterForOrderHistory.CurrentOrdersViewHolder>
{
    Context context;
    public ArrayList<CurrentOrdersDataModel> currentOrdersDataModelArrayList;
    public DataAdapterForOrderHistory(Context c, ArrayList<CurrentOrdersDataModel> currentOrdersDataModelArrayList)
    {
        this.context=c;
        this.currentOrdersDataModelArrayList=currentOrdersDataModelArrayList;
    }

    @Override
    public DataAdapterForOrderHistory.CurrentOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_orders_card, parent, false);
        return new CurrentOrdersViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final DataAdapterForOrderHistory.CurrentOrdersViewHolder holder, final int position)
    {
        holder.l1g.setVisibility(View.INVISIBLE);
        holder.l2g.setVisibility(View.VISIBLE);
        holder.l3g.setVisibility(View.INVISIBLE);
        holder.l4g.setVisibility(View.INVISIBLE);
        holder.l5g.setVisibility(View.INVISIBLE);

        holder.tv_status.setText("Delivered");
        holder.tv_status.setTextColor(Color.parseColor("#CB202D"));
        holder.set_tvorderno.setText(String.valueOf(currentOrdersDataModelArrayList.get(position).getOrder_no()));
        holder.tv_rest_name.setText("Restaurant : "+currentOrdersDataModelArrayList.get(position).getRestaurant_Name());
        holder.set_tvamount.setText("₹"+currentOrdersDataModelArrayList.get(position).getOrder_Amount()+"/-");
        holder.tv_set_date_time.setText("Placed on "+currentOrdersDataModelArrayList.get(position).getDate()+" at "+currentOrdersDataModelArrayList.get(position).getTime());
        holder.order_details_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (view.getId() == R.id.order_details_btn)
                {
                    Intent intent = new Intent(context,OrderDetailsActivity.class);
                    intent.putExtra("Order_No",currentOrdersDataModelArrayList.get(position).getOrder_no());
                    context.startActivity(intent);
                }
            }
        });
        holder.ivphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.ivphone)
                {
                    String number = currentOrdersDataModelArrayList.get(position).getRestaurant_Mobile();
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                    context.startActivity(i);
                }
            }
        });
        if(currentOrdersDataModelArrayList.get(position).getFlag()==1)
        {
            holder.l2g.setVisibility(View.VISIBLE);
            holder.l3g.setVisibility(View.VISIBLE);
        }
        if(currentOrdersDataModelArrayList.get(position).getFlagD()==3)
        {
            holder.l1g.setVisibility(View.VISIBLE);
            holder.tv_status.setText("Cancelled");
            holder.tv_status.setTextColor(Color.parseColor("#CB202D"));
        }
        if(currentOrdersDataModelArrayList.get(position).getFlagD()==1)
        {
            holder.l2g.setVisibility(View.VISIBLE);
            holder.l3g.setVisibility(View.VISIBLE);
            holder.l4g.setVisibility(View.VISIBLE);
        }
        if(currentOrdersDataModelArrayList.get(position).getFlagD()==2)
        {
            holder.l2g.setVisibility(View.VISIBLE);
            holder.l3g.setVisibility(View.VISIBLE);
            holder.l4g.setVisibility(View.VISIBLE);
            holder.l5g.setVisibility(View.VISIBLE);
        }
    }

    public int getItemCount()
    {
        return currentOrdersDataModelArrayList.size();
    }

    public class CurrentOrdersViewHolder extends RecyclerView.ViewHolder
    {
        private TextView set_tvorderno,set_tvamount,tv_set_date_time,tv_status,tv_rest_name;
        private Button order_details_btn;
        private ImageView l1g,l2g,l3g,l4g,l5g,ivphone;
        public CurrentOrdersViewHolder(View view)
        {
            super(view);
            set_tvorderno =(TextView)view.findViewById(R.id.set_tvorderno);
            set_tvamount =(TextView)view.findViewById(R.id.set_tvamount);
            tv_set_date_time =(TextView)view.findViewById(R.id.tv_set_date_time);
            tv_status = (TextView)view.findViewById(R.id.tv_status);
            tv_rest_name=(TextView)view.findViewById(R.id.tv_rest_name);

            order_details_btn=(Button)view.findViewById(R.id.order_details_btn);
            l1g=(ImageView)view.findViewById(R.id.l1g);
            l2g=(ImageView)view.findViewById(R.id.l2g);
            l3g=(ImageView)view.findViewById(R.id.l3g);
            l4g=(ImageView)view.findViewById(R.id.l4g);
            l5g=(ImageView)view.findViewById(R.id.l5g);
            ivphone=(ImageView)view.findViewById(R.id.ivphone);
        }
    }
}