package hu.pe.yummykart.yummykart;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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

public class BackgroundChangeCustomerDetails extends AsyncTask<Void, Void, String>
{
    Context context;
    String PHONE,NAME,ADDRESS_1,ADDRESS_2,LANDMARK;
    ProgressDialog pd;
    public BackgroundChangeCustomerDetails(Context context, String phone, String name, String address_1, String address_2, String landmark)
    {
        this.context = context;
        this.PHONE = phone;
        this.NAME = name;
        this.ADDRESS_1 = address_1;
        this.ADDRESS_2 = address_2;
        this.LANDMARK = landmark;
    }

    @Override
    protected String doInBackground(Void... voids)
    {
        String login_url="http://yummykart.pe.hu/changecustomerdetails.php";
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
                String post_data = URLEncoder.encode("PHONE","UTF-8")+"="+URLEncoder.encode(PHONE,"UTF-8")+"&"
                        +URLEncoder.encode("NAME","UTF-8")+"="+URLEncoder.encode(NAME,"UTF-8")+"&"
                        +URLEncoder.encode("ADDRESS_1","UTF-8")+"="+ URLEncoder.encode(ADDRESS_1,"UTF-8")+"&"
                        +URLEncoder.encode("ADDRESS_2","UTF-8")+"="+ URLEncoder.encode(ADDRESS_2,"UTF-8")+"&"
                        +URLEncoder.encode("LANDMARK","UTF-8")+"="+ URLEncoder.encode(LANDMARK,"UTF-8");
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
        pd.setTitle("Changing Details");
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
            ((ChangeAddressActivity) context).fun_change_details(s);
        }
        else
        {
            pd.dismiss();
            Toast.makeText(context,"check your internet connection!", Toast.LENGTH_SHORT).show();
            ((Activity)context).finish();
        }
    }

}
