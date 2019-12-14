package hu.pe.yummykart.yummykart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundSendData extends AsyncTask<Void, Void, String>
{
    Context context;
    String REST_NAME;
    String REST_PHONE;
    String CUST_PHONE;
    String CUST_PIN;
    String CUST_NAME;
    String ADD_L1;
    String ADD_L2;
    String LANDMARK;
    String jsonArray;
    String AMOUNT;
    String DATE;
    String TIME;
    int DISCOUNT;
    int GST;
    ProgressDialog pd;

    public BackgroundSendData(Context context,String REST_NAME, String REST_PHONE, String CUST_PHONE, String CUST_PIN, String CUST_NAME, String ADD_L1, String ADD_L2, String LANDMARK, String AMOUNT, String jsonArray, String DATE, String TIME,int DISCOUNT,int GST)
    {
        this.context = context;
        this.jsonArray = jsonArray;
        this.REST_NAME = REST_NAME;
        this.CUST_NAME = CUST_NAME;
        this.REST_PHONE = REST_PHONE;
        this.CUST_PIN = CUST_PIN;
        this.CUST_PHONE = CUST_PHONE;
        this.ADD_L1 = ADD_L1;
        this.ADD_L2 = ADD_L2;
        this.LANDMARK = LANDMARK;
        this.AMOUNT = AMOUNT;
        this.DATE = DATE;
        this.TIME = TIME;
        this.DISCOUNT = DISCOUNT;
        this.GST = GST;
    }

    @Override
    protected String doInBackground(Void... voids)
    {
        String login_url="http://yummykart.pe.hu/placeorder.php";
        InputStream inputStream=null;
        try
        {
            URL url=new URL(login_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String post_data = URLEncoder.encode("JSONARRAY","UTF-8")+"="+URLEncoder.encode(jsonArray,"UTF-8")+"&"+
                    URLEncoder.encode("CUST_NAME","UTF-8")+"="+URLEncoder.encode(CUST_NAME,"UTF-8")+"&"+
                    URLEncoder.encode("REST_NAME","UTF-8")+"="+URLEncoder.encode(REST_NAME,"UTF-8")+"&"+
                    URLEncoder.encode("REST_PHONE","UTF-8")+"="+URLEncoder.encode(REST_PHONE,"UTF-8")+"&"+
                    URLEncoder.encode("CUST_PIN","UTF-8")+"="+URLEncoder.encode(CUST_PIN,"UTF-8")+"&"+
                    URLEncoder.encode("CUST_PHONE","UTF-8")+"="+URLEncoder.encode(CUST_PHONE,"UTF-8")+"&"+
                    URLEncoder.encode("ADD_L1","UTF-8")+"="+URLEncoder.encode(ADD_L1,"UTF-8")+"&"+
                    URLEncoder.encode("ADD_L2","UTF-8")+"="+URLEncoder.encode(ADD_L2,"UTF-8")+"&"+
                    URLEncoder.encode("LANDMARK","UTF-8")+"="+URLEncoder.encode(LANDMARK,"UTF-8")+"&"+
                    URLEncoder.encode("AMOUNT","UTF-8")+"="+URLEncoder.encode(AMOUNT,"UTF-8")+"&"+
                    URLEncoder.encode("DATE","UTF-8")+"="+URLEncoder.encode(DATE,"UTF-8")+"&"+
                    URLEncoder.encode("TIME","UTF-8")+"="+URLEncoder.encode(TIME,"UTF-8")+"&"+
                    URLEncoder.encode("DISCOUNT","UTF-8")+"="+URLEncoder.encode(String.valueOf(DISCOUNT),"UTF-8")+"&"+
                    URLEncoder.encode("GST","UTF-8")+"="+URLEncoder.encode(String.valueOf(GST),"UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String result="";
            String line="";
            while ((line=bufferedReader.readLine())!=null)
            {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            return result;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            if (inputStream!=null)
            {
                try
                {
                    inputStream.close();
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
        pd.setTitle("Placing Order");
        pd.setMessage("Please wait...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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
            ((ChangeAddressActivity) context).fun_send_data(s);
        }
        else
        {
            pd.dismiss();
            Toast.makeText(context,"check your internet connection!", Toast.LENGTH_SHORT).show();
            ((Activity)context).finish();
        }
    }


}
