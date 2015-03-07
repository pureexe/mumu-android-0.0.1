package th.in.pureapp.mumu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Vector;

/**
 * Created by Pakkapon on 6/3/2558.
 */
public class HistoryAdapter extends ArrayAdapter<MessageStructure> {
    private Context context = null;
    private Vector<MessageStructure> msgVector = null;
    public HistoryAdapter(Context context, Vector<MessageStructure> msg) {
        super(context, R.layout.row_history,msg);
        this.context = context;
        this.msgVector = msg;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_history, parent, false);
        TextView firstline = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondline = (TextView) rowView.findViewById(R.id.secondLine);
        firstline.setText(msgVector.get(position).getUser());
        secondline.setText(msgVector.get(position).getMessage());
        return rowView;
    }
}
