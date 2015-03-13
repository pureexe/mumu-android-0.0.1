package th.in.pureapp.mumu;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import java.util.Arrays;

/**
 * Created by Pakkapon on 5/3/2558.
 */
public class LoginFragment extends Fragment {
    public LoginFragment() {
    }
    private UiLifecycleHelper uiHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((ActionBarActivity)(this.getActivity())).getSupportActionBar().hide();
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        Session session = Session.getActiveSession();
        if (session != null && !session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this)
                    .setPermissions(Arrays.asList("public_profile"))
                    .setCallback(callback));
        } else {
            Session.openActiveSession(getActivity(), this, true, callback);
        }
        if(session==null){
            Toast.makeText(getActivity(),"This is null",Toast.LENGTH_LONG).show();
        }
        return rootView;
    }
    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {

            Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {

                    SharePrefManager spm = new SharePrefManager(getActivity());
                    spm.setString("userToken",session.getAccessToken());
                    spm.setString("userName",user.getName());
                    spm.setString("userFirstName",user.getFirstName());
                    spm.setString("userID", user.getId());


                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ChatFragment()).commit();
                }
            });
            request.executeAsync();

        } else if (state.isClosed()) {
            Toast.makeText(getActivity(),getActivity().getString(R.string.signinfailed),Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ChatFragment()).commit();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
          //  onSessionStateChange(session, state, exception);
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    private void receiveRestData(String data){

    }
}