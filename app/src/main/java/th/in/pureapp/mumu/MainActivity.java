package th.in.pureapp.mumu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;


public class MainActivity extends ActionBarActivity {

    ActionBarActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            SharePrefManager spm = new SharePrefManager(this);
            if(!spm.getBool("FirstTime")){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new WelcomeFragment())
                        .commit();
            }else{
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new ChatFragment())
                        .commit();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(new SharePrefManager(this).getString("userFirstName")==null){
            menu.findItem(R.id.action_loginwithfacebook).setVisible(true);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.teach){
            SharePrefManager spm = new SharePrefManager(this);
            if(spm.getString("userFirstName")==null && spm.getBool("LoginTeachWarning") == false){
                spm.setBool("LoginTeachWarning",true);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("ลงชื่อเข้าใช้")
                        .setMessage("คุณควรลงชื่อเข้าใช้มูมู่ด้วยเฟสบุ๊ค นี่เป็นมาตราการเพื่อความปลอดภับของมูมู่ ไม่ต้องกังวลเราจะไม่แสดงชื่อของคุณให้ผู้อื่นทราบ หากคุณไม่ลงชื่อเข้าใช้คุณยังสามารถสอนมูมู่ได้ต่อไป แต่จะไม่อัปโหลดคำศัพท์ของคุณเข้าสู่ระบบส่วนกลาง");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new LoginFragment())
                                .commit();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogFragment newFragment = new TeachDialogFragment();
                        newFragment.show(activity.getFragmentManager(),"TEACH");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                DialogFragment newFragment = new TeachDialogFragment();
                newFragment.show(this.getFragmentManager(),"TEACH");

            }
            return true;
        }
        if(id == R.id.action_loginwithfacebook){
            item.setVisible(false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new LoginFragment())
                    .commit();
            return true;
        }
        if (id == R.id.action_history) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new HistoryFragment())
                    .commit();
            return true;
        }
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

}
