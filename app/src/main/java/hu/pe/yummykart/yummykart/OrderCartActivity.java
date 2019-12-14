package hu.pe.yummykart.yummykart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static hu.pe.yummykart.yummykart.CartInitialisation.cartInitialisationArrayList;

public class OrderCartActivity extends AppCompatActivity
{
    String REST_NAME;
    String IMG_URL;
    String REST_LOC;
    String REST_PHONE;
    int MIN_ORDER;
    Time OPEN_T;
    Time CLOSE_T;
    int DISCOUNT;
    int GST;
    String PHONE;
    String PIN;
    TextView tv_rest_name,tv_rest_loc,tv_cart_qty,tv_cart_amt,tv_clear_cart,tv_GST,tv_total,tv_gst_amount,tv_amount,tv_discount,tv_discount_amount,tv_to_pay;
    ImageView imageVieww;

    public Button chk_btn;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_cart);
        chk_btn = (Button) findViewById(R.id.checkout);
        recyclerView = (RecyclerView)findViewById(R.id.order_cart_recycler_view);
        imageVieww = (ImageView)findViewById(R.id.imageVieww);
        tv_rest_name = (TextView)findViewById(R.id.tv_rest_name);
        tv_rest_loc = (TextView)findViewById(R.id.tv_rest_loc);
        tv_cart_qty = (TextView)findViewById(R.id.tv_cart_qty);
        tv_cart_amt = (TextView)findViewById(R.id.tv_cart_amt);
        tv_clear_cart = (TextView)findViewById(R.id.tv_clear_cart);
        tv_total = (TextView)findViewById(R.id.tv_total);
        tv_GST = (TextView)findViewById(R.id.tv_GST);
        tv_gst_amount = (TextView)findViewById(R.id.tv_gst_amount);
        tv_amount = (TextView)findViewById(R.id.tv_amount);

        tv_discount = (TextView)findViewById(R.id.tv_discount);
        tv_discount_amount = (TextView)findViewById(R.id.tv_discount_amount);
        tv_to_pay = (TextView)findViewById(R.id.tv_to_pay);

        Intent intent = getIntent();
        REST_NAME = intent.getStringExtra("REST_NAME");
        IMG_URL = intent.getStringExtra("IMG_URL");
        REST_LOC = intent.getStringExtra("REST_LOC");
        REST_PHONE = intent.getStringExtra("REST_PHONE");
        MIN_ORDER = intent.getIntExtra("MIN_ORDER",0);
        OPEN_T = (Time) intent.getCharSequenceExtra("OPEN_T");
        CLOSE_T = (Time) intent.getCharSequenceExtra("CLOSE_T");
        DISCOUNT = intent.getIntExtra("DISCOUNT",0);
        GST = intent.getIntExtra("GST",0);
        PHONE = intent.getStringExtra("PHONE");
        PIN = intent.getStringExtra("PIN");

        if(cartInitialisationArrayList.size()==0)
        {
            Toast.makeText(this,"Your cart is empty!",Toast.LENGTH_SHORT).show();
            chk_btn.setVisibility(View.INVISIBLE);
        }

        int i,qty=0,sum=0,discount_amt=0,to_pay=0,gst_amount=0,amount=0;
        for(i=0;i<cartInitialisationArrayList.size();i++)
        {
            qty = qty+cartInitialisationArrayList.get(i).getQuantity();
            sum = sum+cartInitialisationArrayList.get(i).getAmount();
        }

        gst_amount = (sum*GST)/100;
        tv_gst_amount.setText("+ ₹"+String.valueOf(gst_amount)+".00");

        amount = sum+gst_amount;
        tv_amount.setText("₹"+String.valueOf(amount)+".00");

        discount_amt=(amount*DISCOUNT)/100;
        tv_discount_amount.setText("- ₹"+String.valueOf(discount_amt)+".00");

        to_pay=amount-discount_amt;
        tv_to_pay.setText("To Pay   ₹"+String.valueOf(to_pay)+".00");

        tv_cart_qty.setText(String.valueOf(qty));
        tv_cart_amt.setText("₹"+String.valueOf(sum)+".00");

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new DataAdapterForOrderCart(this, cartInitialisationArrayList,recyclerView,tv_cart_qty,tv_cart_amt,tv_clear_cart,tv_GST,tv_gst_amount,tv_amount,tv_discount,tv_discount_amount,tv_to_pay,DISCOUNT,GST);
        recyclerView.setAdapter(adapter);

        tv_rest_name.setText(REST_NAME);
        tv_rest_loc.setText(REST_LOC);

        Picasso.with(getApplicationContext()).load(R.drawable.restaurant_default).resize(150,150).into(imageVieww);
        Picasso.with(getApplicationContext()).load(IMG_URL).error(R.drawable.restaurant_default).resize(150,150).into(imageVieww);

        tv_discount.setText(String.valueOf(DISCOUNT)+"%");
        tv_GST.setText("GST @ "+String.valueOf(GST)+"%");

        if(GST==0)
        {
            tv_GST.setVisibility(View.GONE);
            tv_gst_amount.setVisibility(View.GONE);
            tv_amount.setVisibility(View.GONE);
            tv_total.setVisibility(View.GONE);
        }


        chk_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(),ChangeAddressActivity.class);
                intent.putExtra("REST_NAME",REST_NAME);
                intent.putExtra("REST_PHONE",REST_PHONE);
                intent.putExtra("MIN_ORDER",MIN_ORDER);
                intent.putExtra("OPEN_T",OPEN_T);
                intent.putExtra("CLOSE_T",CLOSE_T);
                intent.putExtra("DISCOUNT",DISCOUNT);
                intent.putExtra("GST",GST);
                intent.putExtra("PHONE",PHONE);
                intent.putExtra("PIN",PIN);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
}
