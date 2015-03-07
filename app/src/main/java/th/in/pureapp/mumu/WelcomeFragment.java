package th.in.pureapp.mumu;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.Arrays;

import th.in.pureapp.mumu.LoginFragment;

/**
 * Created by Pakkapon on 5/3/2558.
 */
public class WelcomeFragment extends Fragment {
    SharePrefManager spm;
    public WelcomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(((ActionBarActivity)(this.getActivity())).getSupportActionBar().isShowing()) {
            ((ActionBarActivity) (this.getActivity())).getSupportActionBar().hide();
        }
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        Button btnsignin = (Button)rootView.findViewById(R.id.buttonSignIn);
        Button btnskip = (Button)rootView.findViewById(R.id.buttonSkip);
        spm = new SharePrefManager(getActivity());
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spm.setBool("FirstTime",true);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).commit();
            }
        });
        btnskip.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                spm.setBool("FirstTime",true);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ChatFragment()).commit();
             }
        });
        return rootView;
    }
}