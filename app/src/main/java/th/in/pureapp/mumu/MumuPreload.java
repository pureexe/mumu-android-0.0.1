package th.in.pureapp.mumu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Pakkapon on 6/3/2558.
 */
public class MumuPreload extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "mumu.db";
    private static final int DATABASE_VERSION = 1;

    public MumuPreload(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
