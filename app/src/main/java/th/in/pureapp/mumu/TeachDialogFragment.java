package th.in.pureapp.mumu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by Pakkapon on 7/3/2558.
 */
public class TeachDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_teach, null);
        builder.setView(view);
        SharePrefManager spm = new SharePrefManager(getActivity());
        if(spm.getString("userID")!=null){
            TextView userName = (TextView)view.findViewById(R.id.textviewUserName);
            userName.setText(spm.getString("userFirstName"));
            if(NetworkUtil.isOnline(getActivity())) {
                ImageView userImg = (ImageView) view.findViewById(R.id.iconUser);
                Picasso.with(getActivity()).load("https://graph.facebook.com/" + spm.getString("userID") + "/picture?width=192&height=192").into(userImg);
            }
        }
        builder.setTitle(getActivity().getString(R.string.teachnewword))
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText input = (EditText) view.findViewById(R.id.TeachInputeditText);
                        EditText reply = (EditText) view.findViewById(R.id.TeachReplyeditText);
                        if(!input.getText().toString().matches("")&&!reply.getText().toString().matches("")) {
                            ContentValues values = new ContentValues();
                            values.put("INPUT", String.valueOf(input.getText()));
                            values.put("REPLY", String.valueOf(reply.getText()));
                            new MumuPreload(getActivity()).getWritableDatabase().insert("CONVERSATION", null, values);
                            SharePrefManager spm = new SharePrefManager(getActivity());
                            if (!NetworkUtil.isOnline(getActivity()) || spm.getString("userFirstName") == null) {
                                new PrepareUploadDatabaseHelper(getActivity()).getWritableDatabase().insert("CONVERSATION", null, values);
                            } else {
                                new RestCall.Teach().execute("input=" + input.getText().toString() + "&reply=" + reply.getText().toString() + "&access_token=" + spm.getString("userToken"));
                            }
                            Toast.makeText(getActivity(),"เพิ่มศัพท์ใหม่แล้ว",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(),"คุณต้องกรอกทั้ง 2 ช่อง",Toast.LENGTH_LONG).show();
                        }
                    }
                });



        return builder.create();
    }
}
