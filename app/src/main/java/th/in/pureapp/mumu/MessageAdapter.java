package th.in.pureapp.mumu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Pakkapon on 6/3/2558.
 */
public class MessageAdapter  extends ArrayAdapter<MessageStructure> {
    private Context context = null;
    private Vector<MessageStructure> msgVector = null;
    public MessageAdapter(Context context, Vector<MessageStructure> msg) {
        super(context, R.layout.row_left,msg);
        this.context = context;
        this.msgVector = msg;
        try {
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            File httpCacheDir = new File(context.getCacheDir(), "https");
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception httpResponseCacheNotAvailable) {
            Log.d("MUMU", "HTTP response cache is unavailable.");
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        if(msgVector.get(position).getUser()=="มูมู่") {
            rowView = inflater.inflate(R.layout.row_left, parent, false);
        }else{
            rowView = inflater.inflate(R.layout.row_right, parent, false);
        }
        TextView firstline = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondline = (TextView) rowView.findViewById(R.id.secondLine);
        if(msgVector.get(position).getId()!=null){
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            if(NetworkUtil.isOnline(context)) {
                Picasso.with(context).load("https://graph.facebook.com/" + msgVector.get(position).getId() + "/picture?width=192&height=192").into(imageView);
            }
        }
        firstline.setText(msgVector.get(position).getUser());
        secondline.setText(msgVector.get(position).getMessage());
        return rowView;
    }


    /*
    public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {

        ImageView imageView = null;

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return download_Image((String)imageView.getTag());
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        private Bitmap download_Image(String url) {

            Bitmap bmp =null;
            try{
                Log.w("mumu-picload","NEW PICLOAD");
                URL ulrn = new URL(url);
                HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                if (null != bmp)
                    return bmp;

            }catch(Exception e){}
            return bmp;
        }
    }*/
}
