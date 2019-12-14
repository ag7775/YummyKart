package hu.pe.yummykart.yummykart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hu.pe.yummykart.yummykart.activity.MainActivity;
import hu.pe.yummykart.yummykart.activity.SmsActivity;

public class LoginActivity extends AppCompatActivity
{

    public SharedPreferences sp;
    Context _context;
    public static final String PREF_NAME = "Demofile";
    public int PRIVATE_MODE = 0;
    public SharedPreferences.Editor editor;

    public EditText editTextPhone;
    private TextView tvvalidate;
    private Toolbar toolbar;
    String msg;
    String pin;

    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextPhone.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_perm_identity_black_24dp, 0, 0, 0);
       // toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        tvvalidate = (TextView)findViewById(R.id.tvvalidate);

        relativeLayout= (RelativeLayout)findViewById(R.id.login_activity_relative_layout);
        setSupportActionBar(toolbar);

        Logout_Condition logout_condition = new Logout_Condition();
        int flag = logout_condition.get_flag();

        sp =  getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        msg = sp.getString("PHONE",null);
        editTextPhone.setText(msg);

        if(flag==0)
        {
            editTextPhone.setText(null);
            SharedPreferences sp = getSharedPreferences(PREF_NAME,PRIVATE_MODE);
            editor =sp.edit();
            editor.putString("PHONE",null);
            editor.commit();
        }
        String str = editTextPhone.getText().toString();
        if(!str.trim().equals(""))
        {
            String type="login";
            String phone=msg;

                BackgroundLogin backgroundLogin = new BackgroundLogin(this,relativeLayout);
                backgroundLogin.execute(type, phone);
          //  Intent i = new Intent(this, MainActivity.class);
          //  i.putExtra("PHONE",msg);
          //  i.putExtra("PIN",pin);
          //  startActivity(i);
        }

    }


    public void signup(View v)
    {
        Intent i = new Intent(this,SmsActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public  void signin(View v)
    {
        String type="login";
        String phone=editTextPhone.getText().toString();
        if(isValidPhoneNumber(phone))
        {
            BackgroundLogin backgroundLogin = new BackgroundLogin(this,relativeLayout);
            backgroundLogin.execute(type, phone);
        }
        else
        {
            Toast.makeText(this,"enter valid phone no.",Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    public void fun(String result)
    {
        String pin=null;
        try {
            JSONArray ja = new JSONArray(result);
            JSONObject jo=null;
            for (int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                pin =  jo.getString("pin");
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        if(pin!=null)
        {
            LoginCondition loginCondition = new LoginCondition();
            loginCondition.setLoginflag(1);
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("PHONE",editTextPhone.getText().toString());
            i.putExtra("PIN",pin);
            i.putExtra("INTENT_CODE","1");
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
        else
        {
            tvvalidate.setText("not registered");
            editTextPhone.setText(null);
        }
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        SharedPreferences sp = getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor =sp.edit();
        editor.putString("PHONE", editTextPhone.getText().toString());
        editor.commit();
    }


    class ParsersPin extends AsyncTask<Void,Integer,String>
    {
        Context c;
        String data;

        ProgressDialog pd;

        public ParsersPin(Context c, String data)
        {
            this.c=c;
            this.data=data;
        }

        @Override
        protected String doInBackground(Void... params)
        {
            try {
                JSONArray ja = new JSONArray(data);
                JSONObject jo=null;
                for (int i=0;i<ja.length();i++)
                {
                    jo=ja.getJSONObject(i);
                    pin =  jo.getString("pin");
                }
                return pin;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd=new ProgressDialog(c);
            pd.setTitle("parser");
            pd.setMessage("parsing...please wait");
            pd.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String string)
        {

            super.onPostExecute(string);
            pd.dismiss();
            fun(string);
        }
    }


    private Boolean exit = false;
    @Override
    public void onBackPressed()
    {
        if (exit)
        {
            editTextPhone.setText(null);
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        else
        {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable()
            {

                @Override
                public void run()
                {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

}
