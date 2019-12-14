package hu.pe.yummykart.yummykart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
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

public class BackgroundFindOrderHistory extends AsyncTask<Void,Void,String>
{
    Context context;
    String CUST_PHONE;
    ProgressDialog pd;
    RecyclerView recyclerView;
    public BackgroundFindOrderHistory(Context context, String cust_phone,RecyclerView recyclerView)
    {
        this.context = context;
        this.CUST_PHONE = cust_phone;
        this.recyclerView = recyclerView;
    }

    @Override
    protected String doInBackground(Void... voids)
    {
        String address="http://yummykart.pe.hu/findorderhistory.php";
        InputStream is=null;
        try
        {
            URL url=new URL(address);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String post_data = URLEncoder.encode("CUST_PHONE","UTF-8")+"="+URLEncoder.encode(CUST_PHONE,"UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

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
        pd.setTitle("Fetch Data");
        pd.setMessage("Fetching data...Please wait");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCanceledOnTouchOutside(false);
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
            OrderHistory ob=new OrderHistory();
            OrderHistory.ParseOrderHistory innerObject = ob.new ParseOrderHistory(context,s,recyclerView);
            innerObject.execute();
        }
        else
        {
            Toast.makeText(context,"check your internet connection!", Toast.LENGTH_SHORT).show();
            ((Activity)context).finish();
        }
    }
}
