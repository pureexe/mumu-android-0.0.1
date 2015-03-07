package th.in.pureapp.mumu;

import android.graphics.Bitmap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Created by Pakkapon on 6/3/2558.
 */
public class RestCallProvider {
    public static String doGet(String url) {
        String result = "Empty";
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("mumu.irin.in.th", session);
            }
        };
        try {
            URL urls = new URL(url);
            HttpsURLConnection urlConnection = (HttpsURLConnection)urls.openConnection();
            urlConnection.setHostnameVerifier(hostnameVerifier);
            InputStream in = urlConnection.getInputStream();
            result= convertStreamToString(in);
        } catch (MalformedURLException e) {
            result = e.getMessage();
            e.printStackTrace();
        }catch (IOException e) {
            result = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    public static String teach(String urls) {
        String[] spl = urls.split("|_&&|");
        String result = "Empty";
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("mumu.irin.in.th", session);
            }
        };
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("input",spl[0]));
            nameValuePairs.add(new BasicNameValuePair("reply",spl[1]));
            nameValuePairs.add(new BasicNameValuePair("reply",spl[3]));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            result = "OK";
        } catch (MalformedURLException e) {
            result = e.getMessage();
            e.printStackTrace();
        }catch (IOException e) {
            result = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }
    private static String convertStreamToString(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
