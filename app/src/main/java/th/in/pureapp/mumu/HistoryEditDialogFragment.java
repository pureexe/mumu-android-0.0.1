package th.in.pureapp.mumu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Vector;

/**
 * Created by Pakkapon on 7/3/2558.
 */
@SuppressLint("ValidFragment")
public class HistoryEditDialogFragment extends DialogFragment {
    HistoryEdit callback;
    String pre;
    String post;
    Integer position;
    public HistoryEditDialogFragment(){
    }
    public HistoryEditDialogFragment(String pr,String po,Integer pos){
        pre =pr;
        post = po;
        position = pos;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("pre", pre);
        outState.putString("post", post);
        outState.putInt("position",position);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            callback = (HistoryEdit) getTargetFragment();
            if(callback == null){
                Toast.makeText(getActivity(),"Nulled",Toast.LENGTH_LONG).show();
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement HistoryEdit");
        }
        if(savedInstanceState!=null){
            pre = savedInstanceState.getString("pre");
            post = savedInstanceState.getString("post");
            position = savedInstanceState.getInt("position");
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_teach, null);
        builder.setView(view);
        final SharePrefManager spm = new SharePrefManager(getActivity());
        if(spm.getString("userID")!=null){
            TextView userName = (TextView)view.findViewById(R.id.textviewUserName);
            userName.setText(spm.getString("userFirstName"));
            ImageView userImg = (ImageView)view.findViewById(R.id.iconUser);
            Picasso.with(getActivity()).load("https://graph.facebook.com/" + spm.getString("userID") + "/picture?width=192&height=192").into(userImg);
        }
        final EditText input = (EditText) view.findViewById(R.id.TeachInputeditText);
        final EditText reply = (EditText) view.findViewById(R.id.TeachReplyeditText);
        input.setText(pre);
        input.setEnabled(false);
        reply.setText(post);
        builder.setTitle("แก้ไขศัพท์")
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(!reply.getText().toString().matches("")) {
                            ContentValues values = new ContentValues();
                            values.put("REPLY", reply.getText().toString());
                            new PrepareUploadDatabaseHelper(getActivity()).getWritableDatabase().update("CONVERSATION", values, "INPUT LIKE ?", new String[]{input.getText().toString()});
                            if(NetworkUtil.isOnline(getActivity())) {
                                new Edit().execute("input=" + input.getText().toString() + "&reply=" + reply.getText().toString() + "&old=" + post + "&access_token=" + spm.getString("userToken"));
                            }
                            callback.onEditedUpdateLable(post,reply.getText().toString(),position);
                            //historyAdapter.getItem(position).setMessage(reply.getText().toString());
                            //historyAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getActivity(),"ไม่สามารถใช้ช่องว่างได้",Toast.LENGTH_LONG).show();
                        }
                    }
                });



        return builder.create();
    }
    public interface HistoryEdit  {
        public void onEditedUpdateLable(String before,String after,int position);
    }

    class Edit extends AsyncTask<String,Void,String> {
             @Override
             protected String doInBackground(String... urls) {
                 return RestCall.edit(urls[0]);
             }

             @Override
             protected void onPostExecute(String restdata) {
                 Log.i("MUMU-DEBUG",restdata);
             }
         }
}
