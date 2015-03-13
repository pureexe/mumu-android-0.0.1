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
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        if(msgVector.get(position).getUser()==getContext().getString(R.string.mumu)) {
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
}
