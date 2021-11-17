package liang.junxuan.bodymonitoring.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class BodyMonitordbHelper extends SQLiteOpenHelper {
    private static final String TAG = "bodyMonitorDataBse";

    public static final String CREATE_URIC_ACID = "create table UricAcid(" +
            "id integer primary key autoincrement," +
            "dateTime text," +
            "uricAcid integer," +
            "bloodSugar integer)";

    public static final String CREATE_BLOOD_PRESSURE = "create table BloodPressure(" +
            "id integer primary key autoincrement," +
            "dateTime text," +
            "upperBP integer," +
            "lowerBP integer," +
            "heartBeat integer)";

    private Context mContext;

    public BodyMonitordbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BLOOD_PRESSURE);
        db.execSQL(CREATE_URIC_ACID);
        Log.i(TAG,"Data base created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
