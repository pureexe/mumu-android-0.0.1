package th.in.pureapp.mumu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by Pakkapon on 7/3/2558.
 */
public class AboutDialogFragment extends DialogFragment {

    public AboutDialogFragment(){
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_about, null);
        builder.setView(view);
        TextView privacy = (TextView)view.findViewById(R.id.privacyLink);
        privacy.setText(Html.fromHtml("<a href='https://irin.in.th/privacy'>ความเป็นส่วนตัว</a> <a href='https://irin.in.th/term'>เงื่อนไขในการใช้งาน</a>"));
        privacy.setMovementMethod(LinkMovementMethod.getInstance());

        TextView creditLink = (TextView)view.findViewById(R.id.creditLink);
        creditLink.setText(Html.fromHtml("พัฒนาโดย <a href='http://ไอริน.ไทย'>ไอริน.ไทย</a>"));
        creditLink.setMovementMethod(LinkMovementMethod.getInstance());
        final SharePrefManager spm = new SharePrefManager(getActivity());
        builder.setTitle("เกี่ยวกับ")
                .setPositiveButton(getActivity().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });



        return builder.create();
    }

 }
