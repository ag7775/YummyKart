package hu.pe.yummykart.yummykart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundFindRestaurantMenu extends AsyncTask<Void,Void,String>
{
    Context context;
    ProgressDialog pd;
    String rest_phone;
    public BackgroundFindRestaurantMenu(Context c, String rest_phone)
    {
        Log.d("Testing","Line 0");
        this.context=c;
        this.rest_phone=rest_phone;
    }

    @Override
    protected String doInBackground(Void... params)
    {
        Log.d("Testing","Line 1");
        String address="http://yummykart.pe.hu/findrestaurantmenu.php";
        InputStream is=null;

        try
        {
            URL url=new URL(address);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream=httpURLConnection.getOutputStream();
            Log.d("Testing","Line 2");
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String post_data = URLEncoder.encode("rest_phone","UTF-8")+"="+URLEncoder.encode(rest_phone,"UTF-8");
            bufferedWriter.write(post_data);
            Log.d("Testing","Line 3");
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            Log.d("Testing","Line 4");
            String line=null;

            is=httpURLConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
            StringBuffer sb = new StringBuffer();

            if(br!=null)
            {
                while ((line=br.readLine())!=null)
                {
                    sb.append(line+"\n");
                }
            }
            else
            {
                return null;
            }
            String data= sb.toString();
            return data;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (is!=null)
            {
                try
                {
                    is.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        pd=new ProgressDialog(context,R.style.customProgressDialog);
        pd.setTitle("Loading Menu");
        pd.setMessage("Fetching data...Please wait");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCanceledOnTouchOutside(false);
        this.pd.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
               // ((Activity)context).finish();
            }
        });

        pd.show();
    }

    @Override
    protected void onProgressUpdate(Void... values)
    {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);

        pd.dismiss();
        if(s!=null)
        {
            RestaurantMenuActivity ob=new RestaurantMenuActivity();
            RestaurantMenuActivity.Parsers innerObject = ob.new Parsers(context,s);
            innerObject.execute();
        }
        else
        {
            Toast.makeText(context,"check internet connection!", Toast.LENGTH_SHORT).show();
            MenuLoadingCondition menuLoadingCondition = new MenuLoadingCondition();
            menuLoadingCondition.set_menu_loading_flag(1);
            ((Activity)context).finish();
        }
    }
}
