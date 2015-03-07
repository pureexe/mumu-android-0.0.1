package th.in.pureapp.mumu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Pakkapon on 7/3/2558.
 */
public class TeachFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teach, container, false);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("สอนศัพท์ใหม่");

        return rootView;
    }
}
