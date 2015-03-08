package th.in.pureapp.mumu;

import android.app.DialogFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Vector;

/**
 * Created by Pakkapon on 7/3/2558.
 */
public class HistoryFragment extends Fragment {
    HistoryAdapter historyAdapter;
    boolean notHaveHistory = false;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ListView listView;
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        Vector<MessageStructure> vex = new Vector <MessageStructure>();
        listView = (ListView) rootView.findViewById(R.id.listView);
        if(savedInstanceState != null&&savedInstanceState.getParcelable("Chatlog")!=null){
            historyAdapter = new HistoryAdapter(getActivity(),((ParcelableMessageStructureVector)savedInstanceState.getParcelable("Chatlog")).get());
            rootView.findViewById(R.id.historySpinner).setVisibility(View.GONE);
        }else {
            historyAdapter = new HistoryAdapter(getActivity(), vex);

            PrepareUploadDatabaseHelper mDbHelper = new PrepareUploadDatabaseHelper(getActivity());
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM CONVERSATION WHERE 1 ORDER BY ID DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    historyAdapter.add(new MessageStructure(cursor.getString(cursor.getColumnIndex("INPUT")), cursor.getString(cursor.getColumnIndex("REPLY"))));
                } while (cursor.moveToNext());
            } else {
                notHaveHistory = true;
            }
            SharePrefManager spm = new SharePrefManager(getActivity());
            if (spm.getString("userToken") != null) {
                if (NetworkUtil.isOnline(getActivity())) {
                    new GetHistory().execute("https://mumu.irin.in.th/history?access_token=" + spm.getString("userToken") + "&limit=0,1000");
                } else {
                    ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.historySpinner);
                    progressBar.setVisibility(View.GONE);
                    if (notHaveHistory) {
                        TextView log = (TextView) rootView.findViewById(R.id.historyNoneMsg);
                        log.setVisibility(View.VISIBLE);
                        log.setText("กรุณาเชื่อมต่ออินเตอร์เน็ต\nเพื่อโหลดข้อมูลเพิ่มเติม");
                    } else {
                        Toast.makeText(getActivity(), "กรุณาเชื่อมต่ออินเตอร์เน็ตเพื่อโหลดข้อมูลส่วนที่เหลือ", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.historySpinner);
                progressBar.setVisibility(View.GONE);
                if (notHaveHistory == true) {
                    rootView.findViewById(R.id.historyNoneMsg).setVisibility(View.VISIBLE);
                }
            }
        }
        listView.setAdapter(historyAdapter);
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogFragment newFragment = new HistoryEditDialogFragment(historyAdapter.getItem(position).getUser(), historyAdapter.getItem(position).getMessage(), historyAdapter, position);
                newFragment.show(getActivity().getFragmentManager(), "HISTORYEDIT");
            }
        });
        return rootView;
    }
    public void receiveHistory(String restdata){
            try {
                JSONArray jsa = new JSONArray(restdata);
                Integer i = 0;
                if (jsa.length() > 0 && historyAdapter!=null) {
                    for (i = 0; i < jsa.length(); i++) {
                            historyAdapter.add(new MessageStructure(jsa.getJSONObject(i).getString("INPUT"), jsa.getJSONObject(i).getString("REPLY")));
                    }
                } else {
                    if(getView()!=null) {
                        getView().findViewById(R.id.historyNoneMsg).setVisibility(View.VISIBLE);
                    }
                }
                if(getView()!=null) {
                    getView().findViewById(R.id.historySpinner).setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(historyAdapter.getCount()>0) {
            Integer i = 0;
            Vector<MessageStructure> temp = new Vector<MessageStructure>();
            for (i = 0; i < historyAdapter.getCount(); i++) {
                temp.add(historyAdapter.getItem(i));
            }
            outState.putParcelable("Chatlog", new ParcelableMessageStructureVector(temp));
        }
    }
    class GetHistory extends AsyncTask<String,Void,String> {

             @Override
             protected String doInBackground(String... urls) {
                 return RestCall.doGet(urls[0]);
             }

             @Override
             protected void onPostExecute(String restdata) {
                 receiveHistory(restdata);
             }
         }

}
