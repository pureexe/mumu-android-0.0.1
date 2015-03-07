package th.in.pureapp.mumu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Pakkapon on 6/3/2558.
 */
public class SharePrefManager {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    SharePrefManager(Activity activity)
    {
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }
    /*** Easy Define ***/
    public void setBool(String name,boolean in)
    {
        editor.putBoolean(name, in);
        editor.commit();
    }
    public boolean getBool(String name)
    {
        return sharedPref.getBoolean(name, false);
    }
    public void setInt(String name,int in)
    {
        editor.putInt(name, in);
        editor.commit();
    }
    public int getInt(String name)
    {
        return sharedPref.getInt(name,0);
    }
    public void setFloat(String name,float in)
    {
        editor.putFloat(name, in);
        editor.commit();
    }
    public float getFloat(String name)
    {
        return sharedPref.getFloat(name,0);
    }
    public void setString(String name,String in)
    {
        editor.putString(name, in);
        editor.commit();
    }
    public String getString(String name)
    {
        return sharedPref.getString(name,null);
    }
}
