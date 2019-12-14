package hu.pe.yummykart.yummykart;

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.graphics.Color;
        import android.os.AsyncTask;
        import android.support.design.widget.Snackbar;
        import android.view.View;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

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

public class BackgroundLogin extends AsyncTask<String, Void, String>
{
    ProgressDialog pd;
    Context context;
    RelativeLayout relativeLayout;
    BackgroundLogin(Context ctx, RelativeLayout relativeLayout)
    {
        context=ctx;
        this.relativeLayout=relativeLayout;
    }

    @Override
    protected String doInBackground(String... params)
    {
        String type=params[0];
        InputStream inputStream=null;
        String login_url="http://yummykart.pe.hu/login.php";
        if(type.equals("login"))
        {
            try {
                String phone = params[1];
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8");
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
            }
            catch (ProtocolException e)
            {
                e.printStackTrace();
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
                if (inputStream!=null)
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
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
        pd.setTitle("Signing in");
        pd.setMessage("Please wait...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
    }

    @Override
    protected void onPostExecute(String result)
    {
        pd.dismiss();
        if(result!=null)
        {
            ((LoginActivity) context).fun(result);
        }
        else
        {
            pd.dismiss();
           // Toast.makeText(context,"check internet connection!", Toast.LENGTH_SHORT).show();
            Snackbar snackbar =  Snackbar.make(relativeLayout, "Check internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("Action", null);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values)
    {
        super.onProgressUpdate(values);
    }

}


