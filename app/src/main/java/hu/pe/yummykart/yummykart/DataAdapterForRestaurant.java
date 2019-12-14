package hu.pe.yummykart.yummykart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static hu.pe.yummykart.yummykart.R.id.discount;
import static hu.pe.yummykart.yummykart.R.id.imageView;
import static hu.pe.yummykart.yummykart.R.id.packed;
import static hu.pe.yummykart.yummykart.R.id.time_status;
import static hu.pe.yummykart.yummykart.R.id.tv_discount;

public class DataAdapterForRestaurant extends RecyclerView.Adapter<DataAdapterForRestaurant.ViewHolder>
{

    public ArrayList<String> rest_name;
    public ArrayList<String> rest_phone;
    public ArrayList<String> rest_loc;
    public ArrayList<String> rest_rating;
    public ArrayList<Integer> min_order;
    public ArrayList<String> img_Url;
    public ArrayList<Time> open_t;
    public ArrayList<Time> close_t;
    public ArrayList<Integer> discount;
    public ArrayList<Integer> gst;
    public String phone;
    public String pin;


    Context c;

    public DataAdapterForRestaurant(Context c, ArrayList<String> rest_name, ArrayList<String> rest_phone, ArrayList<String> rest_loc, ArrayList<String> rest_rating, ArrayList<Integer> min_order, ArrayList<String> img_Url, ArrayList<Time> open_t, ArrayList<Time> close_t, ArrayList<Integer> discount, ArrayList<Integer> gst, String phone,String pin)
    {
        this.rest_name = rest_name;
        this.rest_phone = rest_phone;
        this.rest_loc = rest_loc;
        this.rest_rating = rest_rating;
        this.min_order = min_order;
        this.img_Url = img_Url;
        this.open_t = open_t;
        this.close_t = close_t;
        this.discount = discount;
        this.gst = gst;
        this.c = c;
        this.phone = phone;
        this.pin = pin;

    }

    @Override
    public DataAdapterForRestaurant.ViewHolder onCreateViewHolder(ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataAdapterForRestaurant.ViewHolder holder, final int position)
    {
        holder.tv_discount.setVisibility(View.INVISIBLE);
        holder.ll_discount.setVisibility(View.GONE);
        holder.tv_rest_name.setText(rest_name.get(position));
        if(min_order.get(position)==0)
        {
            holder.tv_min_order.setText("No min. order");
        }
        else if(min_order.get(position)!=0)
        {
            holder.tv_min_order.setText("Min Order ₹ " + String.valueOf(min_order.get(position)));
        }
        holder.tv_rest_loc.setText(rest_loc.get(position));
        holder.tv_rating.setText(rest_rating.get(position));

        Picasso.with(c).load(R.drawable.restaurant_default).resize(150,150).into(holder.image_view);
        Picasso.with(c).load(img_Url.get(position)).error(R.drawable.restaurant_default).resize(150,150).into(holder.image_view);


        Time OPEN_T = open_t.get(position);
        Time CLOSE_T = close_t.get(position);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = timeformat.format(calendar.getTime());
        Date current = null;
        try
        {
        current = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
        }
        catch (ParseException e)
        {
        e.printStackTrace();
        }

        if(!OPEN_T.equals(CLOSE_T))
        {
            if (current.after(OPEN_T))
             {
                if (current.before(CLOSE_T))
                    {
                        holder.time_status.setText("OPEN   ");
                        holder.time_status.setTextColor(Color.parseColor("#6ba80a"));
                    }
             }
            if (current.before(OPEN_T) || current.after(CLOSE_T))
            {
                holder.time_status.setText("CLOSED");
            }
        }
        else if(OPEN_T.equals(CLOSE_T))
        {
            holder.time_status.setTextColor(Color.parseColor("#6ba80a"));
            holder.time_status.setText("OPEN   ");
        }

        if (open_t.get(position).equals(close_t.get(position)))
        {
            holder.tv_time.setText("\uD83D\uDD50 Always open");
        }
        else
        {
            String strDateFormat = "hh:mm a";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            holder.tv_time.setText("\uD83D\uDD50 "+sdf.format(open_t.get(position)) + " - " + sdf.format(close_t.get(position)));
        }

        int d = discount.get(position);
        if(d!=0)
        {
            holder.tv_discount.setVisibility(View.VISIBLE);
            holder.ll_discount.setVisibility(View.VISIBLE);
            holder.tv_discount.setText(d+"%"+" OFF - auto applies at checkout");
        }
        else
        {
            holder.ll_discount.setVisibility(View.GONE);
            holder.tv_discount.setVisibility(View.INVISIBLE);
        }
    }



    @Override
    public int getItemCount()
    {
        return rest_phone.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_rest_name;
        private TextView tv_min_order;
        private TextView tv_rest_loc;
        private TextView tv_rating;
        private LinearLayout ll_discount;
        private ImageView image_view;
        private TextView tv_time;
        private TextView tv_discount;
        private TextView time_status;
        public ViewHolder(View view)
        {
            super(view);
            tv_rest_name = (TextView) view.findViewById(R.id.tv_rest_name);
            tv_rest_loc = (TextView) view.findViewById(R.id.tv_rest_loc);
            tv_rating = (TextView) view.findViewById(R.id.tv_rating);
            ll_discount = (LinearLayout) view.findViewById(R.id.ll_discount);
            tv_min_order = (TextView) view.findViewById(R.id.tv_min_order);
            image_view = (ImageView) view.findViewById(R.id.imageVieww);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                image_view.setClipToOutline(true);
            }
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_discount = (TextView) view.findViewById(R.id.tv_discount);
            time_status = (TextView) view.findViewById(R.id.time_status);

            view.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View view)
                                        {
                                            String REST_NAME = (String) tv_rest_name.getText();
                                            String REST_LOC = (String) tv_rest_loc.getText();
                                            int position = rest_name.indexOf(REST_NAME);
                                            String REST_PHONE = rest_phone.get(position);
                                            String IMG_URL = img_Url.get(position);
                                            int MIN_ORDER = min_order.get(position);
                                            Time OPEN_T = open_t.get(position);
                                            Time CLOSE_T = close_t.get(position);
                                            int DISCOUNT = discount.get(position);
                                            int GST = gst.get(position);

                                            Calendar calendar = Calendar.getInstance();
                                            SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
                                            String currentTime = timeformat.format(calendar.getTime());
                                            Date current = null;
                                            try
                                            {
                                                current = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
                                            }
                                            catch (ParseException e)
                                            {
                                                e.printStackTrace();
                                            }

                                            if(!OPEN_T.equals(CLOSE_T))
                                            {
                                                if (current.after(OPEN_T))
                                                {
                                                    if (current.before(CLOSE_T))
                                                    {
                                                        Intent intent = new Intent(c, RestaurantMenuActivity.class);
                                                        intent.putExtra("REST_NAME", REST_NAME);
                                                        intent.putExtra("IMG_URL",IMG_URL);
                                                        intent.putExtra("REST_LOC",REST_LOC);
                                                        intent.putExtra("REST_PHONE", REST_PHONE);
                                                        intent.putExtra("MIN_ORDER", MIN_ORDER);
                                                        intent.putExtra("OPEN_T", OPEN_T);
                                                        intent.putExtra("CLOSE_T", CLOSE_T);
                                                        intent.putExtra("DISCOUNT", DISCOUNT);
                                                        intent.putExtra("GST", GST);
                                                        intent.putExtra("PHONE", phone);
                                                        intent.putExtra("PIN", pin);
                                                        c.startActivity(intent);
                                                    }
                                                }
                                                if (current.before(OPEN_T) || current.after(CLOSE_T))
                                                {
                                                    Toast.makeText(c, "closed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else if(OPEN_T.equals(CLOSE_T))
                                            {
                                                Intent intent = new Intent(c,RestaurantMenuActivity.class);
                                                intent.putExtra("REST_NAME",REST_NAME);
                                                intent.putExtra("IMG_URL",IMG_URL);
                                                intent.putExtra("REST_LOC",REST_LOC);
                                                intent.putExtra("REST_PHONE",REST_PHONE);
                                                intent.putExtra("MIN_ORDER",MIN_ORDER);
                                                intent.putExtra("OPEN_T",OPEN_T);
                                                intent.putExtra("CLOSE_T",CLOSE_T);
                                                intent.putExtra("DISCOUNT",DISCOUNT);
                                                intent.putExtra("GST", GST);
                                                intent.putExtra("PHONE",phone);
                                                intent.putExtra("PIN",pin);
                                                c.startActivity(intent);
                                            }


                                        }
                                    }
            );
        }
    }
}