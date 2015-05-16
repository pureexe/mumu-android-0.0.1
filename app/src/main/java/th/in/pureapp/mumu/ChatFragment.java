package th.in.pureapp.mumu;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.Vector;

/**
 * Created by Pakkapon on 6/3/2558.
 */
public class ChatFragment extends Fragment {
    public ChatFragment() {
    }
    SharePrefManager spm;
    String fbUserID;
    Bitmap userPicture = null;
    ListView chatView;
    MessageAdapter chatAdapter = null;
    EditText chatEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            chatAdapter = new MessageAdapter(getActivity(),((ParcelableMessageStructureVector)savedInstanceState.getParcelable("Chatlog")).get());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        if(!((ActionBarActivity)getActivity()).getSupportActionBar().isShowing()){
            ((ActionBarActivity)getActivity()).getSupportActionBar().show();
        }
        spm = new SharePrefManager(getActivity());
        if(NetworkUtil.isOnline(getActivity())&&spm.getString("userToken")!=null){
            SQLiteDatabase db=  new PrepareUploadDatabaseHelper(getActivity()).getWritableDatabase();
            //Cursor cursor =db.rawQuery("SELECT * FROM CONVERSATION WHERE 1",null);
            Cursor cursor = db.query("CONVERSATION",new String[]{"INPUT,REPLY"},"1",new String[]{},null,null,"INPUT ASC");
            if(cursor.moveToFirst()){
                String access_token = spm.getString("userToken");
                do{
                    new RestCall.Teach().execute("input="+cursor.getString(cursor.getColumnIndex("INPUT"))+"&reply="+cursor.getString(cursor.getColumnIndex("REPLY"))+"&access_token="+access_token);
                }while(cursor.moveToNext());
                    //db.execSQL("DELETE FROM CONVERSATION WHERE 1");
                    db.delete("CONVERSATION", "1",new String[]{});
            }
        }
        chatView = (ListView) rootView.findViewById(R.id.listViewChat);
        Vector<MessageStructure> welcomeMessage = new Vector<MessageStructure>();
        welcomeMessage.add(new MessageStructure(getActivity().getString(R.string.mumu),"วันนี้มีอะไรอยากพูดกับมูมู่ไหม"));
        if(chatAdapter==null) {
            chatAdapter = new MessageAdapter(getActivity(), welcomeMessage);
        }
        chatView.setAdapter(chatAdapter);
        chatView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        fbUserID = spm.getString("userID");
        chatEditText = (EditText) rootView.findViewById(R.id.chatInput);
        chatEditText.setImeOptions(EditorInfo.IME_ACTION_SEND);
        ImageView imageViewSend = (ImageView)rootView.findViewById(R.id.imageViewSend);
        imageViewSend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String inp = chatEditText.getText().toString();
                if(inp.length()>0) {
                    sendMsg(inp);
                }
            }
        });
        chatEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    String inp = chatEditText.getText().toString();
                    if(inp.length()>0) {
                        if(inp.substring(0,2).equals("$$")&&inp.indexOf("%%")>-1){
                            chatEditText.setText("");
                            Toast.makeText(getActivity(),getActivity().getString(R.string.plzusepenciltoteach),Toast.LENGTH_LONG).show();
                        }else {
                            sendMsg(inp);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Integer i = 0;
        Vector<MessageStructure> temp = new Vector<MessageStructure>();
        for(i=0;i<chatAdapter.getCount();i++){
            temp.add(chatAdapter.getItem(i));
        }
        outState.putParcelable("Chatlog", new ParcelableMessageStructureVector(temp));
     }
    public void sendMsg(String inp){
        if(spm.getString("userFirstName")==null) {
            chatAdapter.add(new MessageStructure(getActivity().getString(R.string.me), inp));
        }else{
            chatAdapter.add(new MessageStructure(spm.getString("userFirstName"), inp,spm.getString("userID")));
        }
        chatEditText.setText("");
        MumuPreload mDbHelper = new MumuPreload(getActivity());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //Cursor cursor =db.rawQuery("SELECT REPLY FROM CONVERSATION WHERE INPUT='"+inp+"' AND REPLY !='-' ORDER BY random() LIMIT 1",null);
        Cursor cursor = db.query("CONVERSATION",new String[]{"REPLY"},"INPUT = ?",new String[]{inp},null,null,"random()");
        if (cursor.moveToFirst()){
            chatAdapter.add(new MessageStructure(getActivity().getString(R.string.mumu), cursor.getString(cursor.getColumnIndex("REPLY"))));
            chatAdapter.notifyDataSetChanged();
        }else{
            if(NetworkUtil.isOnline(getActivity())) {
                new GetMumuMessage().execute("https://mumu.irin.in.th/talk/" + inp);
            }else {
                chatAdapter.add(new MessageStructure(getActivity().getString(R.string.mumu),"มูมู่ยังไม่รู้จักเลย สอนเพิ่มหน่อย"));
                chatAdapter.notifyDataSetChanged();
            }
        }
        cursor.close();
    }
    private void replyMumu(String rep){chatAdapter.add(new MessageStructure(getActivity().getString(R.string.mumu),rep));}
    class GetMumuMessage extends AsyncTask<String,Void,String> {
        // Use getMessage class
        @Override
        protected String doInBackground(String... urls) {
            return RestCall.doGet(urls[0]);
        }

        @Override
        protected void onPostExecute(String restdata) {
            replyMumu(restdata);
        }
    }
}