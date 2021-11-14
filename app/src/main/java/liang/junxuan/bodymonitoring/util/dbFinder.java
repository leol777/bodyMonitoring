package liang.junxuan.bodymonitoring.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import liang.junxuan.bodymonitoring.dataBase.bodyMonitordbHelper;
import liang.junxuan.bodymonitoring.item.bloodPressure;
import liang.junxuan.bodymonitoring.item.uricAcid;

public class dbFinder {
    private bodyMonitordbHelper dbHelper;

    public dbFinder(bodyMonitordbHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    public ArrayList<bloodPressure> findAllBP(){
        ArrayList<bloodPressure> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("BloodPressure", null, null, null, null, null, null);

        if (cursor.moveToNext()){
            do {int up = cursor.getInt(cursor.getColumnIndex("upperBP"));
                int low = cursor.getInt(cursor.getColumnIndex("lowerBP"));
                int hb = cursor.getInt(cursor.getColumnIndex("heartBeat"));
                String dateTime = cursor.getString(cursor.getColumnIndex("dateTime"));

                bloodPressure item = new bloodPressure(dateTime, up, low);
                item.setHeartBeat(hb);

                list.add(item);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<uricAcid> findAllUA(){
        ArrayList<uricAcid> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("UricAcid", null, null, null, null, null, null);
        if (!cursor.moveToFirst()){
            Log.i("ViewBodyData", "Empty list");
        }

        if (cursor.moveToNext()){
            do {int ua = cursor.getInt(cursor.getColumnIndex("uricAcid"));
                int bs = cursor.getInt(cursor.getColumnIndex("bloodSugar"));
                String dateTime = cursor.getString(cursor.getColumnIndex("dateTime"));

                uricAcid item = new uricAcid(dateTime, ua);
                item.setBloodSugar(bs);

                list.add(item);
            }while (cursor.moveToNext());
        }
        Log.d("ViewBodyData",list.toString());
        cursor.close();
        return list;
    }


}
