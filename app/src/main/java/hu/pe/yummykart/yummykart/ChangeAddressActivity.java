package hu.pe.yummykart.yummykart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import hu.pe.yummykart.yummykart.activity.MainActivity;

import static hu.pe.yummykart.yummykart.CartInitialisation.cartInitialisationArrayList;

public class ChangeAddressActivity extends AppCompatActivity
{
    Button change_pin_btn,change_details_btn;
    static EditText et_input_pin,et_input_name,et_input_address_1,et_input_address_2,et_input_landmark;
    static TextView tv_phone, tv_place_order;
    static Button btn_place_order;

    CoordinatorLayout coordinatorLayout;

    String REST_NAME;
    String REST_PHONE;
    int MIN_ORDER;
    Time OPEN_T;
    Time CLOSE_T;
    int DISCOUNT;
    int GST;
    String PHONE;
    String PIN;

    int total=0;
    int to_pay=0;
    int discount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        et_input_pin = (EditText)findViewById(R.id.input_pin);
        et_input_name = (EditText)findViewById(R.id.input_name);
        et_input_address_1 = (EditText)findViewById(R.id.input_address_1);
        et_input_address_2 = (EditText)findViewById(R.id.input_address_2);
        et_input_landmark = (EditText)findViewById(R.id.input_landmark);
        tv_phone = (TextView)findViewById(R.id.tv_phone);
        btn_place_order = (Button) findViewById(R.id.btn_place_order);
        tv_place_order = (TextView)findViewById(R.id.tv_place_order);
        change_pin_btn = (Button)findViewById(R.id.change_pin_btn);
        change_details_btn = (Button)findViewById(R.id.change_details_btn);
        et_input_name.requestFocus();

        Toolbar toolbar = (Toolbar) findViewById(R.id.change_address_app_bar);
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
         coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);

        Intent intent = getIntent();
        REST_NAME = intent.getStringExtra("REST_NAME");
        REST_PHONE = intent.getStringExtra("REST_PHONE");
        MIN_ORDER = intent.getIntExtra("MIN_ORDER",0);
        OPEN_T = (Time) intent.getCharSequenceExtra("OPEN_T");
        CLOSE_T = (Time) intent.getCharSequenceExtra("CLOSE_T");
        DISCOUNT = intent.getIntExtra("DISCOUNT",0);
        GST = intent.getIntExtra("GST",0);
        PHONE = intent.getStringExtra("PHONE");
        PIN = intent.getStringExtra("PIN");
        tv_phone.setText("Your contact no: "+PHONE);
        if(CartInitialisation.cartInitialisationArrayList.size() == 0)
        {
            btn_place_order.setVisibility(View.GONE);
            tv_place_order.setVisibility(View.INVISIBLE);
        }
        else
        {
            btn_place_order.setVisibility(View.VISIBLE);
            tv_place_order.setVisibility(View.VISIBLE);
           /* int i;
            for (i=0;i<CartInitialisation.cartInitialisationArrayList.size();i++)
            {
                total = total + CartInitialisation.cartInitialisationArrayList.get(i).getAmount();
            }
            if(DISCOUNT==0)
            {
                discount = total;
                btn_place_order.setText("To pay  ₹"+String.valueOf(discount)+"/-");
                btn_place_order.setTextColor(Color.parseColor("#FFFFFF"));
            }
            if(DISCOUNT!=0)
            {
                discount = total-((total*DISCOUNT)/100);
                btn_place_order.setText("To pay  ₹"+String.valueOf(discount)+"/-");
                btn_place_order.setTextColor(Color.parseColor("#FFFFFF"));
            }*/

            int i,qty=0,sum=0,discount_amt=0,gst_amount=0,amount=0;
            for(i=0;i<cartInitialisationArrayList.size();i++)
            {
                sum = sum+cartInitialisationArrayList.get(i).getAmount();
            }

            gst_amount = (sum*GST)/100;
            amount = sum+gst_amount;

            discount_amt=(amount*DISCOUNT)/100;

            to_pay=amount-discount_amt;

            btn_place_order.setText("To pay  ₹"+String.valueOf(to_pay)+"/-");
            btn_place_order.setTextColor(Color.parseColor("#FFFFFF"));
        }
        BackgroundFindCustomerDetails backgroundFindCustomerDetails = new BackgroundFindCustomerDetails(ChangeAddressActivity.this, PHONE);
        backgroundFindCustomerDetails.execute();

        change_details_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(et_input_name.getText().toString().equals("") || et_input_address_1.getText().toString().equals("") || et_input_landmark.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"enter proper details",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    BackgroundChangeCustomerDetails backgroundChangeCustomerDetails = new BackgroundChangeCustomerDetails(ChangeAddressActivity.this, PHONE, et_input_name.getText().toString(), et_input_address_1.getText().toString(), et_input_address_2.getText().toString(),et_input_landmark.getText().toString());
                    backgroundChangeCustomerDetails.execute();
                }
            }
        });
        change_pin_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(et_input_pin.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"enter pin code",Toast.LENGTH_SHORT).show();
                }
                else if(et_input_pin.getText().toString().length()!=6)
                {
                    Toast.makeText(getApplicationContext(),"enter correct pin",Toast.LENGTH_SHORT).show();
                }
                else if(et_input_pin.getText().toString().equals(PIN))
                {
                    Toast.makeText(getApplicationContext(),"enter new pin",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    BackgroundChangeCustomerPin backgroundChangeCustomerPin = new BackgroundChangeCustomerPin(ChangeAddressActivity.this, PHONE, et_input_pin.getText().toString());
                    backgroundChangeCustomerPin.execute();
                }
            }
        });


        btn_place_order.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(to_pay>=MIN_ORDER)
                {
                    if(et_input_pin.getText().toString().equals("")||et_input_name.getText().toString().equals("")|| et_input_address_1.getText().toString().equals("")||et_input_landmark.getText().toString().equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"enter proper details",Toast.LENGTH_SHORT).show();
                    }
                    else if(et_input_pin.getText().toString().length()!=6)
                    {
                        Toast.makeText(getApplicationContext(),"enter correct pin",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm aa");
                        String time = timeformat.format(calendar.getTime());
                        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
                        String date = dateformat.format(calendar.getTime());
                        //give non filled entries condition
                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(CartInitialisation.cartInitialisationArrayList);
                        BackgroundSendData backgroundSendData = new BackgroundSendData(ChangeAddressActivity.this,REST_NAME, REST_PHONE, PHONE, et_input_pin.getText().toString(), et_input_name.getText().toString(), et_input_address_1.getText().toString(), et_input_address_2.getText().toString(), et_input_landmark.getText().toString(), String.valueOf(to_pay), jsonArray, date, time, DISCOUNT, GST);
                        backgroundSendData.execute();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Min. order is ₹ "+String.valueOf(MIN_ORDER), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void fun_change_details(String s)
    {
        if(Integer.parseInt(s.trim())==1)
        {
            Toast.makeText(getApplicationContext(),"changed successfully", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"check internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void fun_change_pin(String s)
    {
        if(Integer.parseInt(s.trim())==1)
        {
           /* Toast.makeText(getApplicationContext(),"changed successfully", Toast.LENGTH_SHORT).show();
            Logout_Condition logout_condition = new Logout_Condition();
            logout_condition.set_flag(0);
            LoginCondition loginCondition = new LoginCondition();
            loginCondition.setLoginflag(0);
            Intent intent = new Intent(ChangeAddressActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();*/
            Toast.makeText(getApplicationContext(),"changed successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangeAddressActivity.this, MainActivity.class);
            intent.putExtra("PHONE",PHONE);
            intent.putExtra("PIN",et_input_pin.getText().toString());
            intent.putExtra("INTENT_CODE","1");
            startActivity(intent);
            finish();
        }
        else if(Integer.parseInt(s.trim())==2)
        {
            Snackbar snackbar =  Snackbar.make(coordinatorLayout,"no restaurant found at this pin!", Snackbar.LENGTH_LONG).setAction("Action", null);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            et_input_pin.setText(PIN);
        }
        else
        {
            finish();
        }
    }

    public void fun_send_data(String s)
    {
        if(Integer.parseInt(s.trim())==1)
        {
            Toast.makeText(getApplicationContext(),"Order Placed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangeAddressActivity.this, CurrentOrdersActivity.class);
            intent.putExtra("CUST_PHONE",PHONE);
            intent.putExtra("CUST_PIN",PIN);
            startActivity(intent);
            finish();
        }
        else if(Integer.parseInt(s.trim())==2)
        {
            Snackbar snackbar =  Snackbar.make(coordinatorLayout,"this restaurant doesn't deliver at this pin ", Snackbar.LENGTH_LONG).setAction("Action", null);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Check internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    class ParseCustomerDetails extends AsyncTask<Void,Integer,Integer>
    {
        Context c;
        String data;
        String name,pin,address_1,address_2,landmark;
        public ParseCustomerDetails(Context c,String data)
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

                for (int i=0;i<ja.length();i++)
                {
                    jo=ja.getJSONObject(i);

                    name=jo.getString("name");
                    pin=jo.getString("pin");
                    address_1=jo.getString("address_1");
                    address_2=jo.getString("address_2");
                    landmark=jo.getString("landmark");
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
               et_input_pin.setText(pin);

               if(name.trim().equals("null"))
                   et_input_name.setText("");
               else
                   et_input_name.setText(name);

               if(address_1.trim().equals("null"))
                    et_input_address_1.setText("");
               else
                    et_input_address_1.setText(address_1);

               if(address_2.trim().equals("null"))
                   et_input_address_2.setText("");
               else
                   et_input_address_2.setText(address_2);

               if(landmark.trim().equals("null"))
                   et_input_landmark.setText("");
               else
                   et_input_landmark.setText(landmark);
           }
           else
           {
               Toast.makeText(c,"check your internet connection!", Toast.LENGTH_SHORT).show();
               ((Activity)c).finish();
           }
        }
    }
}
