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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity
{

    String CUST_PHONE,CUST_PIN;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.order_history_app_bar);
        recyclerView = (RecyclerView)findViewById(R.id.order_history_recycler_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        Intent intent = getIntent();
        CUST_PHONE = intent.getStringExtra("CUST_PHONE");
        CUST_PIN = intent.getStringExtra("CUST_PIN");
        BackgroundFindOrderHistory backgroundFindOrderHistory = new BackgroundFindOrderHistory(OrderHistory.this, CUST_PHONE,recyclerView);
        backgroundFindOrderHistory.execute();
    }

    private void initViews(ArrayList<CurrentOrdersDataModel> currentOrdersDataModelArrayList, RecyclerView rv, Context c)
    {
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(c);
        rv.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new DataAdapterForOrderHistory(c,currentOrdersDataModelArrayList);
        rv.setAdapter(adapter);
    }

    class ParseOrderHistory extends AsyncTask<Void,Integer,Integer>
    {
        Context c;
        String data;
        RecyclerView rv;
        ArrayList<CurrentOrdersDataModel> currentOrdersDataModelArrayList=new ArrayList<>();
        public ParseOrderHistory(Context c,String data,RecyclerView recyclerView)
        {
            this.c=c;
            this.data=data;
            this.rv=recyclerView;
        }

        @Override
        protected Integer doInBackground(Void... params)
        {
            try {
                JSONArray ja=new JSONArray(data);
                JSONObject jo=null;

                for (int i=0;i<ja.length();i++)
                {
                    CurrentOrdersDataModel currentOrdersDataModel = new CurrentOrdersDataModel();
                    jo=ja.getJSONObject(i);
                        currentOrdersDataModel.setOrder_no(jo.getInt("Order_No"));
                    currentOrdersDataModel.setRestaurant_Name(jo.getString("Restaurant_Name"));
                        currentOrdersDataModel.setRestaurant_Mobile(jo.getString("Restaurant_Mobile"));
                        currentOrdersDataModel.setOrder_Amount(jo.getString("Order_Amount"));
                        currentOrdersDataModel.setDate(jo.getString("Date"));
                        currentOrdersDataModel.setTime(jo.getString("Time"));
                        currentOrdersDataModel.setFlag(jo.getInt("Flag"));
                        currentOrdersDataModel.setFlagD(jo.getInt("FlagD"));
                        currentOrdersDataModelArrayList.add(currentOrdersDataModel);
                }
                if(currentOrdersDataModelArrayList.size()==0)
                {
                    return 0;
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
        protected void onPostExecute(Integer integer)
        {
            super.onPostExecute(integer);
            if(integer==1)
            {

                OrderHistory.this.initViews(currentOrdersDataModelArrayList,rv,c);
            }
            else
            {
                Toast.makeText(c,"no order history!", Toast.LENGTH_SHORT).show();
                ((Activity)c).finish();
            }
        }
    }
}
