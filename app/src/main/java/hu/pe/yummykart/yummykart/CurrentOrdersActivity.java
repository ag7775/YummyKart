package hu.pe.yummykart.yummykart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import hu.pe.yummykart.yummykart.activity.MainActivity;

public class CurrentOrdersActivity extends AppCompatActivity
{
    String CUST_PHONE,CUST_PIN;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.current_orders_app_bar);
        recyclerView = (RecyclerView)findViewById(R.id.current_orders_recycler_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
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
        BackgroundFindCurrentOrders backgroundFindCurrentOrders = new BackgroundFindCurrentOrders(CurrentOrdersActivity.this, CUST_PHONE,recyclerView);
        backgroundFindCurrentOrders.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.current_orders_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_refresh)
        {
            BackgroundFindCurrentOrders backgroundFindCurrentOrders = new BackgroundFindCurrentOrders(CurrentOrdersActivity.this, CUST_PHONE,recyclerView);
            backgroundFindCurrentOrders.execute();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews(ArrayList<CurrentOrdersDataModel> currentOrdersDataModelArrayList, RecyclerView rv, Context c)
    {
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(c);
        rv.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new DataAdapterForCurrentOrders(c,currentOrdersDataModelArrayList);
        rv.setAdapter(adapter);
    }

    class ParseOrderDetails extends AsyncTask<Void,Integer,Integer>
    {
        Context c;
        String data;
        RecyclerView rv;
        ArrayList<CurrentOrdersDataModel> currentOrdersDataModelArrayList=new ArrayList<>();
        public ParseOrderDetails(Context c,String data,RecyclerView recyclerView)
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
                    if(jo.getInt("FlagD")!=2 && jo.getInt("FlagD")!=3)
                    {
                        currentOrdersDataModel.setOrder_no(jo.getInt("Order_No"));
                        currentOrdersDataModel.setRestaurant_Mobile(jo.getString("Restaurant_Mobile"));
                        currentOrdersDataModel.setRestaurant_Name(jo.getString("Restaurant_Name"));
                        currentOrdersDataModel.setOrder_Amount(jo.getString("Order_Amount"));
                        currentOrdersDataModel.setDate(jo.getString("Date"));
                        currentOrdersDataModel.setTime(jo.getString("Time"));
                        currentOrdersDataModel.setFlag(jo.getInt("Flag"));
                        currentOrdersDataModel.setFlagD(jo.getInt("FlagD"));
                        currentOrdersDataModelArrayList.add(currentOrdersDataModel);
                    }
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

                CurrentOrdersActivity.this.initViews(currentOrdersDataModelArrayList,rv,c);
            }
            else
            {
                Toast.makeText(c,"no current order!", Toast.LENGTH_SHORT).show();
                ((Activity)c).finish();
            }
        }
    }
}
