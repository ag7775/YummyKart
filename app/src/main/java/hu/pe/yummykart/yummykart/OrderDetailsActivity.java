package hu.pe.yummykart.yummykart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity
{
    int Order_No;
    public static ListView listView;
    ArrayList<OrderDetailsDataModel> orderDetailsDataModelArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.order_details_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView=(ListView)findViewById(R.id.listView);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        Intent intent = getIntent();
        Order_No = intent.getIntExtra("Order_No",0);

        BackgroundFindOrderDetails backgroundFindOrderDetails = new BackgroundFindOrderDetails(OrderDetailsActivity.this,Order_No);
        backgroundFindOrderDetails.execute();
    }

    private void initViews(ArrayList<OrderDetailsDataModel> orderDetailsDataModelArrayList,Context c)
    {
        OrderDetails_ListAdapter adapter =  new OrderDetails_ListAdapter(c,R.layout.list_adapter_view, orderDetailsDataModelArrayList);
        listView.setAdapter(adapter);
    }


    class Parsers extends AsyncTask<Void,Integer,Integer>
    {
        Context c;
        String data;
        String name,pin,address_1,address_2,landmark;
        public Parsers(Context c,String data)
        {
            this.c=c;
            this.data=data;
        }


        @Override
        protected Integer doInBackground(Void... params)
        {
            try {
                JSONArray ja=new JSONArray(data);
                JSONObject jo=null;
                String s = null;
                for (int i=0;i<ja.length();i++)
                {
                    jo=ja.getJSONObject(i);
                    s = jo.getString("Order");
                }


                JSONArray ja1=new JSONArray(s);
                JSONObject jo1=null;
                for (int i=0;i<ja1.length();i++)
                {
                    jo1=ja1.getJSONObject(i);
                    OrderDetailsDataModel orderDetailsDataModel = new OrderDetailsDataModel();
                    orderDetailsDataModel.setItem(jo1.getString("item"));
                    orderDetailsDataModel.setQuantity(jo1.getString("quantity"));
                    orderDetailsDataModel.setAmount(jo1.getString("amount"));
                    orderDetailsDataModelArrayList.add(orderDetailsDataModel);
                }
                return 1;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPreExecute()

        {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer s)
        {
            super.onPostExecute(s);
            if(s!=0)
            {
                OrderDetailsActivity.this.initViews(orderDetailsDataModelArrayList,c);
            }
            else
            {
                 Toast.makeText(c,"check your internet connection!", Toast.LENGTH_SHORT).show();
                ((Activity)c).finish();
            }
        }
    }
}
