package th.in.pureapp.mumu;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Created by Pakkapon on 6/3/2558.
 */
public class RestCallProvider {
    public static Bitmap getBitmap(String urls){
        URL url = null;
        try {
            url = new URL(urls);
            URLConnection connection = url.openConnection();
            connection.setUseCaches(true);
            Object response = connection.getContent();
            if (response instanceof Bitmap) {
                return (Bitmap)response;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
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
