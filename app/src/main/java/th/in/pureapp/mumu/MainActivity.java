package th.in.pureapp.mumu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;


import java.lang.reflect.Field;


public class MainActivity extends ActionBarActivity {

    ActionBarActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        activity = this;
        setContentView(R.layout.activity_main);

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
        SharePrefManager spm = new SharePrefManager(this);
        if (savedInstanceState == null) {
            if (!spm.getBool("FirstTime")) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new WelcomeFragment())
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ChatFragment())
                        .commit();
            }
        }
        if(NetworkUtil.isOnline(this)&&spm.getString("userToken")!=null){
            SQLiteDatabase db=  new PrepareUploadDatabaseHelper(this).getWritableDatabase();
            Cursor cursor =db.rawQuery("SELECT * FROM CONVERSATION WHERE 1",null);
            if(cursor.moveToFirst()){
                String access_token = spm.getString("userToken");
                do{
                    new RestCall.Teach().execute("input="+cursor.getString(cursor.getColumnIndex("INPUT"))+"&reply="+cursor.getString(cursor.getColumnIndex("REPLY"))+"&=access_token="+access_token);
                }while(cursor.moveToNext());
                cursor = db.rawQuery("DELETE FROM CONVERSATION WHERE 1",null);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (new SharePrefManager(this).getString("userFirstName") == null) {
            menu.findItem(R.id.action_loginwithfacebook).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (new SharePrefManager(this).getString("userFirstName") == null) {
            menu.findItem(R.id.action_loginwithfacebook).setVisible(true);
        } else {
            menu.findItem(R.id.action_loginwithfacebook).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.teach) {
            SharePrefManager spm = new SharePrefManager(this);
            if (spm.getString("userFirstName") == null && spm.getBool("LoginTeachWarning") == false) {
                spm.setBool("LoginTeachWarning", true);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(this.getString(R.string.signin))
                        .setMessage(this.getString(R.string.addword_sigin_warn));
                builder.setPositiveButton(this.getString(R.string.signin), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new LoginFragment())
                                .commit();
                    }
                });
                builder.setNegativeButton(this.getString(R.string.skip_short), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogFragment newFragment = new TeachDialogFragment();
                        newFragment.show(activity.getFragmentManager(), "TEACH");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                DialogFragment newFragment = new TeachDialogFragment();
                newFragment.show(this.getFragmentManager(), "TEACH");

            }
            return true;
        }
        if (id == R.id.action_loginwithfacebook) {
            item.setVisible(false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new LoginFragment())
                    .commit();
            return true;
        }
        if (id == R.id.action_history) {
            if(!(this.getSupportFragmentManager().findFragmentById(R.id.container) instanceof HistoryFragment)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new HistoryFragment(),"HISTORY").addToBackStack(null)
                        .commit();
            }
            return true;
        }
        if (id == R.id.action_about) {
            android.support.v4.app.DialogFragment newFragment = new AboutDialogFragment();
            newFragment.show(this.getSupportFragmentManager(), "HISTORYEDIT");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

}
