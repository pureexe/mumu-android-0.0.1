package th.in.pureapp.mumu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pakkapon on 7/3/2558.
 */
public class PrepareUploadDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "prepare.db";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE CONVERSATION (ID INTEGER PRIMARY KEY NOT NULL,INPUT CHAR(256),REPLY CHAR(256))";
    private static final String SQL_DELETE_ENTRIES ="DROP TABLE IF EXISTS CONVERSATION";
    public PrepareUploadDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
