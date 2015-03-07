package th.in.pureapp.mumu;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Vector;

/**
 * Created by Pakkapon on 7/3/2558.
 */
public class HistoryFragment extends Fragment {
    HistoryAdapter historyAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ListView listView;
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        Vector<MessageStructure> vex = new Vector <MessageStructure>();
        listView = (ListView) rootView.findViewById(R.id.listView);
        historyAdapter = new HistoryAdapter(getActivity(),vex);
        listView.setAdapter(historyAdapter);
        PrepareUploadDatabaseHelper mDbHelper = new PrepareUploadDatabaseHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor =db.rawQuery("SELECT * FROM CONVERSATION WHERE 1 ORDER BY ID DESC",null);
        if (cursor.moveToFirst()){
            do {
                historyAdapter.add(new MessageStructure(cursor.getString(cursor.getColumnIndex("INPUT")), cursor.getString(cursor.getColumnIndex("REPLY"))));
            }while (cursor.moveToNext());
        }else{
            //empty db
        }
        SharePrefManager spm = new SharePrefManager(getActivity());
        new GetHistory().execute("https://mumu.irin.in.th/history?access_token=" +spm.getString("userToken")+"&limit=0,1000");
        return rootView;
    }
    public void receiveHistory(String restdata){
        try {
            JSONArray jsa = new JSONArray(restdata);

            Integer i =0;
            for(i=0;i<jsa.length();i++){
                historyAdapter.add(new MessageStructure(jsa.getJSONObject(i).getString("INPUT"),jsa.getJSONObject(i).getString("REPLY")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    class GetHistory extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            return RestCallProvider.doGet(urls[0]);
        }

        @Override
        protected void onPostExecute(String restdata) {
            receiveHistory(restdata);
        }
    }

}
