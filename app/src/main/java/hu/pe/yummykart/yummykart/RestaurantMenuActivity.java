package hu.pe.yummykart.yummykart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;

import hu.pe.yummykart.yummykart.activity.MainActivity;
import hu.pe.yummykart.yummykart.helper.PrefManager;

import static hu.pe.yummykart.yummykart.R.id.imageView;

public class RestaurantMenuActivity extends AppCompatActivity
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
    int i;
    Handler handler;
    private PrefManager pref;
    ImageView toolbarImage;
    FloatingActionButton fab;
    MenuLoadingCondition menuLoadingCondition = new MenuLoadingCondition();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
        ArrayList<MenuInitialisation> ArrayList=CartInitialisation.getCartInitialisationArrayList(); //= new CartInitialisation();
        ArrayList.clear();

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

        this.setTitle(REST_NAME);

        pref = new PrefManager(getApplicationContext());
        handler = new Handler();

        toolbarImage = (ImageView)findViewById(R.id.toolbar_image);
        Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_animation);
        toolbarImage.startAnimation(zoomOutAnimation);

        fab = (FloatingActionButton) findViewById(R.id.fab_cart);

        if(ReloadingMenu.getReloagingMenuFlag()==0)
        {
            BackgroundFindRestaurantMenu backgroundFindRestaurantMenu = new BackgroundFindRestaurantMenu(RestaurantMenuActivity.this, REST_PHONE);
            backgroundFindRestaurantMenu.execute();
        }

        this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {

                if(menuLoadingCondition.get_menu_loading_flag()==0)
                {
                    handler.postDelayed(this,2000);
                }
                else if(menuLoadingCondition.get_menu_loading_flag()==1)
                {
                    fun();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(CartInitialisation.cartInitialisationArrayList.size()==0)
                {
                    Toast.makeText(getApplicationContext(),"Your cart is empty!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    MenuReloadingFlag.setMenu_reloading_flag(1);
                    Intent intent = new Intent(RestaurantMenuActivity.this, OrderCartActivity.class);
                    intent.putExtra("REST_NAME", REST_NAME);
                    intent.putExtra("IMG_URL",IMG_URL);
                    intent.putExtra("REST_LOC", REST_LOC);
                    intent.putExtra("REST_PHONE", REST_PHONE);
                    intent.putExtra("MIN_ORDER", MIN_ORDER);
                    intent.putExtra("OPEN_T", OPEN_T);
                    intent.putExtra("CLOSE_T", CLOSE_T);
                    intent.putExtra("DISCOUNT", DISCOUNT);
                    intent.putExtra("GST", GST);
                    intent.putExtra("PHONE", PHONE);
                    intent.putExtra("PIN", PIN);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });


    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        fun();
        if(MenuReloadingFlag.getMenu_reloading_flag()==0)
        {
            BackgroundFindRestaurantMenu backgroundFindRestaurantMenu = new BackgroundFindRestaurantMenu(RestaurantMenuActivity.this, REST_PHONE);
            backgroundFindRestaurantMenu.execute();
        }
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        CartInitialisation cartInitialisation = new CartInitialisation();
        cartInitialisation.delete_cartInitialisationArrayList();
        MenuInitialisationArrayList menuInitialisationArrayList = new MenuInitialisationArrayList();
        menuInitialisationArrayList.delete_menuInitialisationArrayList();
        MenuLoadingCondition menuLoadingCondition = new MenuLoadingCondition();
        menuLoadingCondition.set_menu_loading_flag(0);
        ReloadingMenu reloadingMenu = new ReloadingMenu();
        reloadingMenu.setReloagingMenuFlag(0);
        MenuReloadingFlag.setMenu_reloading_flag(0);
    }

    public void fun()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarImage.setImageResource(R.drawable.recommended);
        Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_animation);
        toolbarImage.startAnimation(zoomOutAnimation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
                                                 @Override
                                                 public void onClick(View v)
                                                 {
                                                     MenuReloadingFlag.setMenu_reloading_flag(0);
                                                     finish();
                                                 }
                                             });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), RestaurantMenuActivity.this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++)
        {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                if(position==0)
                {
                    fab.show();
                    toolbarImage.setImageResource(R.drawable.recommended);
                    Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_animation);
                    toolbarImage.startAnimation(zoomOutAnimation);
                }
                else if(position == 1)
                {
                    fab.show();
                    toolbarImage.setImageResource(R.drawable.chinese);
                    Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_animation);
                    toolbarImage.startAnimation(zoomOutAnimation);
                }
                else if(position == 2)
                {
                    fab.show();
                    toolbarImage.setImageResource(R.drawable.curry);
                    Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_animation);
                    toolbarImage.startAnimation(zoomOutAnimation);
                }
                else if(position == 3)
                {
                    fab.show();
                    toolbarImage.setImageResource(R.drawable.rolls);
                    Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_animation);
                    toolbarImage.startAnimation(zoomOutAnimation);
                }
                else if(position == 4)
                {
                    fab.show();
                    toolbarImage.setImageResource(R.drawable.dry);
                    Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_animation);
                    toolbarImage.startAnimation(zoomOutAnimation);
                }
                else if(position == 5)
                {
                    fab.show();
                    toolbarImage.setImageResource(R.drawable.bread_image);
                    Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_animation);
                    toolbarImage.startAnimation(zoomOutAnimation);
                }
                else if(position == 6)
                {
                    fab.show();
                    toolbarImage.setImageResource(R.drawable.biryani_rice);
                    Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_animation);
                    toolbarImage.startAnimation(zoomOutAnimation);
                }
                else if(position == 7)
                {
                    fab.show();
                    toolbarImage.setImageResource(R.drawable.combos);
                    Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_animation);
                    toolbarImage.startAnimation(zoomOutAnimation);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    class Parsers extends AsyncTask<Void,Integer,Integer>
    {
        Context c;
        String data;
        private ArrayList<MenuInitialisation> menuInitialisationArrayList = new ArrayList<>();
        ProgressDialog pd;
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

                for (int i=0;i<ja.length();i++)
                {
                    jo=ja.getJSONObject(i);

                    String item=jo.getString("ITEM");
                    int price=jo.getInt("PRICE");
                    String category=jo.getString("CATEGORY");
                    String type=jo.getString("TYPE");
                    int detail=jo.getInt("DETAIL");
                    MenuInitialisation menuInitialisation = new MenuInitialisation();
                    menuInitialisation.setItem(item);
                    menuInitialisation.setPrice(price);
                    menuInitialisation.setCategory(category);
                    menuInitialisation.setQuantity(0);
                    menuInitialisation.setAmount(0);
                    menuInitialisation.setType(type);
                    menuInitialisation.setDetail(detail);

                    menuInitialisationArrayList.add(menuInitialisation);
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
            ReloadingMenu.setReloagingMenuFlag(1);
            if(integer==1)
            {
                MenuInitialisationArrayList menuInitialisationArray = new MenuInitialisationArrayList();
                menuInitialisationArray.setMenuInitialisationArrayList(menuInitialisationArrayList);
                MenuLoadingCondition menuLoadingCondition = new MenuLoadingCondition();
                menuLoadingCondition.set_menu_loading_flag(1);
            }
            else if(integer==0)
            {
                Toast.makeText(c,"sorry! no menu", Toast.LENGTH_SHORT).show();
                ((Activity)c).finish();
            }
            pd.dismiss();
        }
    }

    class PagerAdapter extends FragmentPagerAdapter
    {
        String tabTitles[] = new String[]{"Recommended","Chinese","Curry","Rolls","Dry","Bread","Biryani & Rice","Combos"};
        Context context;

        public PagerAdapter(FragmentManager fm, Context context)
        {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount()
        {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    return new RecommendedFragment(fab);
                case 1:
                    return new ChineseFragment(fab);
                case 2:
                    return new CurryFragment(fab);
                case 3:
                    return new RollsFragment(fab);
                case 4:
                    return new DryFragment(fab);
                case 5:
                    return new BreadFragment(fab);
                case 6:
                    return new BiryaniRiceFragment(fab);
                case 7:
                    return new CombosFragment(fab);
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return tabTitles[position];
        }

        public View getTabView(int position)
        {
            View tab = LayoutInflater.from(RestaurantMenuActivity.this).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(tabTitles[position]);
            return tab;
        }
    }
}