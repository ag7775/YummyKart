package hu.pe.yummykart.yummykart.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hu.pe.yummykart.yummykart.AboutActivity;
import hu.pe.yummykart.yummykart.AboutAppActivity;
import hu.pe.yummykart.yummykart.BackgroundFindRestaurant;
import hu.pe.yummykart.yummykart.CartInitialisation;
import hu.pe.yummykart.yummykart.ChangeAddressActivity;
import hu.pe.yummykart.yummykart.CurrentOrdersActivity;
import hu.pe.yummykart.yummykart.DataAdapterForRestaurant;
import hu.pe.yummykart.yummykart.LoginActivity;
import hu.pe.yummykart.yummykart.LoginCondition;
import hu.pe.yummykart.yummykart.Logout_Condition;
import hu.pe.yummykart.yummykart.MenuInitialisation;
import hu.pe.yummykart.yummykart.OrderHistory;
import hu.pe.yummykart.yummykart.R;
import hu.pe.yummykart.yummykart.SliderUtils;
import hu.pe.yummykart.yummykart.ViewPagerAdapter;
import hu.pe.yummykart.yummykart.helper.PrefManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    public static TextView tv_no_restaurant_found;
    private  static  ImageView iv_restaurant_not_found;

    private PrefManager pref;
    public static String pin;
    public static String phone;
    TextView tv_drawer_phone;

    RecyclerView recyclerView;
    boolean doubleBackToExitPressedOnce = false;
    private boolean flag = false;

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    RequestQueue rq;
    List<SliderUtils> sliderImg;
    ViewPagerAdapter viewPagerAdapter;

    String request_url = "http://yummykart.pe.hu/find_slider.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<MenuInitialisation> ArrayList= CartInitialisation.getCartInitialisationArrayList(); //= new CartInitialisation();
        ArrayList.clear();

        tv_no_restaurant_found=(TextView)findViewById(R.id.tv_no_restaurant_found);
        iv_restaurant_not_found=(ImageView)findViewById(R.id.iv_restaurant_not_found);
        tv_no_restaurant_found.setVisibility(View.INVISIBLE);
        iv_restaurant_not_found.setVisibility(View.INVISIBLE);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> profile = pref.getUserDetails();
        phone = profile.get("mobile");
        pin = profile.get("pin");

        LoginCondition loginCondition = new LoginCondition();
        if(loginCondition.getLoginflag()==1)
        {
            Intent intent = getIntent();
            if (intent.getStringExtra("INTENT_CODE").equals("1"))
            {
                phone = intent.getStringExtra("PHONE");
                pin = intent.getStringExtra("PIN");
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        recyclerView = (RecyclerView)findViewById(R.id.restaurant_recycler_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        tv_drawer_phone = (TextView) header.findViewById(R.id.tv_drawer_phone);
        tv_drawer_phone.setText(phone);

        BackgroundFindRestaurant backgroundFindRestaurant=new BackgroundFindRestaurant(MainActivity.this,recyclerView,pin);
        backgroundFindRestaurant.execute();

        rq = Volley.newRequestQueue(this);
        sliderImg = new ArrayList<>();
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        sendRequest();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                for(int i=0; i<dotscount; i++)
                {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 5000);

    }

    public void sendRequest()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(request_url ,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                for(int i=0; i<response.length(); i++)
                {
                    SliderUtils sliderUtils = new SliderUtils();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        sliderUtils.setSliderImageUrl(jsonObject.getString("image_url"));
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    sliderImg.add(sliderUtils);
                }

                viewPagerAdapter = new ViewPagerAdapter(sliderImg ,MainActivity.this);
                viewPager.setAdapter(viewPagerAdapter);

                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];

                for(int i=0;i<dotscount;i++)
                {
                    dots[i] = new ImageView(MainActivity.this);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactive_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderDotspanel.addView(dots[i], params);
                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                    }
                });

        rq.add(jsonArrayRequest);
    }

    private void logout()
    {
        pref.clearSession();
        Logout_Condition logout_condition = new Logout_Condition();
        logout_condition.set_flag(0);
        LoginCondition loginCondition = new LoginCondition();
        loginCondition.setLoginflag(0);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_logout)
        {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);

        }
        else
        {
            if (exit)
            {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            } else
            {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_address)
        {
            Intent i = new Intent(this, ChangeAddressActivity.class);
            i.putExtra("PHONE",phone);
            i.putExtra("PIN",pin);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (id == R.id.nav_current_order)
        {
            Intent i = new Intent(this, CurrentOrdersActivity.class);
            i.putExtra("CUST_PHONE",phone);
            i.putExtra("CUST_PIN",pin);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (id == R.id.nav_pre_order)
        {
            Intent i = new Intent(this, OrderHistory.class);
            i.putExtra("CUST_PHONE",phone);
            i.putExtra("CUST_PIN",pin);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if(id == R.id.nav_about)
        {
            Intent i = new Intent(this,AboutActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (id == R.id.nav_share)
        {
            Intent i=new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT,"YummyKart");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=hu.pe.yummykart.yummykart");
            startActivity(Intent.createChooser(i,"Share via"));
        }
        else if (id == R.id.nav_rate)
        {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=hu.pe.yummykart.yummykart" + getApplicationContext().getPackageName())));
            }
        }
        else if (id == R.id.about_app)
        {
            Intent i = new Intent(this, AboutAppActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void initViews(ArrayList<String> rest_name, ArrayList<String> rest_phone, ArrayList<String> rest_loc, ArrayList<String> rest_rating, ArrayList<Integer> min_order,ArrayList<String> img_Url,ArrayList<Time> open_t, ArrayList<Time> close_t, ArrayList<Integer> discount, ArrayList<Integer> gst, RecyclerView rv, Context c)
    {
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(c);
        rv.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new DataAdapterForRestaurant(c,rest_name,rest_phone,rest_loc,rest_rating,min_order,img_Url,open_t,close_t,discount,gst,phone,pin);
        rv.setAdapter(adapter);
    }


    public class Parsers extends AsyncTask<Void,Integer,Integer>
    {
        Context c;
        String data;
        RecyclerView rv;
        private ArrayList<String> rest_name = new ArrayList<>();
        private ArrayList<String> rest_phone = new ArrayList<>();
        private ArrayList<String> rest_loc = new ArrayList<>();
        private ArrayList<String> rest_rating = new ArrayList<>();
        private ArrayList<Integer> min_order = new ArrayList<>();
        private ArrayList<String> img_Url = new ArrayList<>();
        private ArrayList<Time> open_t = new ArrayList<>();
        private ArrayList<Time> close_t = new ArrayList<>();
        private ArrayList<Integer> discount = new ArrayList<>();
        private ArrayList<Integer> gst = new ArrayList<>();
        ProgressDialog pd;

        public Parsers(Context c,String data,RecyclerView rv)
        {
            this.c=c;
            this.data=data;
            this.rv=rv;
        }


        @Override
        protected Integer doInBackground(Void... params)
        {
            try {
                JSONArray ja=new JSONArray(data);
                JSONObject jo=null;

                rest_name.clear();
                rest_phone.clear();
                rest_loc.clear();
                rest_rating.clear();
                min_order.clear();
                img_Url.clear();
                open_t.clear();
                close_t.clear();
                discount.clear();
                gst.clear();
                for (int i=0;i<ja.length();i++)
                {
                    jo=ja.getJSONObject(i);

                    String restname=jo.getString("REST_NAME");
                    String restphone=jo.getString("PHONE");
                    String restloc=jo.getString("REST_LOC");
                    String restrating=jo.getString("REST_RATING");
                    int minorder=jo.getInt("MIN_ORDER");
                    String imgurl=jo.getString("IMG_URL");
                    Time opent= Time.valueOf(jo.getString("OPEN_TIME"));
                    Time closet= Time.valueOf(jo.getString("CLOSE_TIME"));
                    int discounts=jo.getInt("DISCOUNT");
                    int gst_tax=jo.getInt("GST");
                    rest_name.add(restname);
                    rest_phone.add(restphone);
                    rest_loc.add(restloc);
                    rest_rating.add(restrating);
                    min_order.add(minorder);
                    img_Url.add(imgurl);
                    open_t.add(opent);
                    close_t.add(closet);
                    discount.add(discounts);
                    gst.add(gst_tax);
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
            pd=new ProgressDialog(c);
            pd.setTitle("parser");
            pd.setMessage("parsing...please wait");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
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
                MainActivity.this.initViews(rest_name,rest_phone,rest_loc,rest_rating,min_order,img_Url,open_t,close_t,discount,gst,rv,c);
            }
            else if(integer==0)
            {
                // Toast.makeText(c,"sorry! no restaurant found", Toast.LENGTH_LONG).show();
                tv_no_restaurant_found.setVisibility(View.VISIBLE);
                iv_restaurant_not_found.setVisibility(View.VISIBLE);
            }
            pd.dismiss();
        }
    }

    public class MyTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            MainActivity.this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    int i = viewPager.getCurrentItem();
                    if(i<dotscount-1)
                    {

                            viewPager.setCurrentItem(i+1);
                    }
                    else
                    {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

}
