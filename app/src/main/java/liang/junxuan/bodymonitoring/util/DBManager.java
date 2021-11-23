package liang.junxuan.bodymonitoring.util;

import android.animation.TimeInterpolator;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;
import liang.junxuan.bodymonitoring.item.BloodPressure;
import liang.junxuan.bodymonitoring.item.UricAcid;

public class DBManager {
    final private String TAG = "DataBaseManager";
    private BodyMonitordbHelper dbHelper;

    public enum Time_Interval{
        WEEK, MONTH, YEAR
    }

    public DBManager(BodyMonitordbHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    public ArrayList<BloodPressure> findAllBP(){
        ArrayList<BloodPressure> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("BloodPressure", null, null, null, null, null, null);

        if (cursor.moveToNext()){
            do {int up = cursor.getInt(cursor.getColumnIndex("upperBP"));
                int low = cursor.getInt(cursor.getColumnIndex("lowerBP"));
                int hb = cursor.getInt(cursor.getColumnIndex("heartBeat"));
                String dateTime = cursor.getString(cursor.getColumnIndex("dateTime"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));


                BloodPressure item = new BloodPressure(dateTime, up, low);
                item.setHeartBeat(hb);
                item.setId(id);

                list.add(item);
            }while (cursor.moveToNext());
        }
        cursor.close();
        Collections.sort(list);
        return list;
    }

    public ArrayList<UricAcid> findAllUA(){
        ArrayList<UricAcid> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("UricAcid", null, null, null, null, null, null);
        if (!cursor.moveToFirst()){
            Log.i("ViewBodyData", "Empty list");
        }

        if (cursor.moveToNext()){
            do {int ua = cursor.getInt(cursor.getColumnIndex("uricAcid"));
                int bs = cursor.getInt(cursor.getColumnIndex("bloodSugar"));
                String dateTime = cursor.getString(cursor.getColumnIndex("dateTime"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));

                UricAcid item = new UricAcid(dateTime, ua);
                item.setBloodSugar(bs);
                item.setId(id);

                list.add(item);
            }while (cursor.moveToNext());
        }
        Log.d("ViewBodyData",list.toString());
        cursor.close();
        Collections.sort(list);
        return list;
    }

    public ArrayList<BloodPressure> findBPbyTime(Time_Interval time_interval){
        ArrayList<BloodPressure> allBP= this.findAllBP();
        Calendar calendar = Calendar.getInstance();

        switch (time_interval){
            case MONTH:
                calendar.add(Calendar.MONTH, -1);
            case WEEK:
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
            case YEAR:
                calendar.add(Calendar.YEAR, -1);
        }

        Date date = calendar.getTime();
        long month_ago = date.getTime();

        for (int i = 0; i<allBP.size(); i++){
            BloodPressure item = allBP.get(i);
            if (item.getDateTimeInDate().getTime() < month_ago){
                allBP.remove(i);
            }else {
                break;
            }
        }

        return allBP;
    }

    public ArrayList<UricAcid> findUAbyTime(Time_Interval time_interval){
        ArrayList<UricAcid> allUA = this.findAllUA();
        Calendar calendar = Calendar.getInstance();

        switch (time_interval){
            case MONTH:
                calendar.add(Calendar.MONTH, -1);
            case WEEK:
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
            case YEAR:
                calendar.add(Calendar.YEAR, -1);
        }

        Date date = calendar.getTime();
        long month_ago = date.getTime();

        for (int i = 0; i<allUA.size(); i++){
            UricAcid item = allUA.get(i);
            if (item.getDateTimeInDate().getTime() < month_ago){
                allUA.remove(i);
            }else {
                break;
            }
        }

        return allUA;
    }

    public BloodPressure findBPbyId(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from BloodPressure where id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});

        cursor.moveToFirst();

        int up = cursor.getInt(cursor.getColumnIndex("upperBP"));
        int low = cursor.getInt(cursor.getColumnIndex("lowerBP"));
        int hb = cursor.getInt(cursor.getColumnIndex("heartBeat"));
        String dateTime = cursor.getString(cursor.getColumnIndex("dateTime"));


        BloodPressure item = new BloodPressure(dateTime, up, low);
        item.setHeartBeat(hb);
        item.setId(id);

        cursor.close();

        return item;
    }

    public UricAcid findUAbyId(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from UricAcid where id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});

        cursor.moveToFirst();

        int ua = cursor.getInt(cursor.getColumnIndex("uricAcid"));
        int bs = cursor.getInt(cursor.getColumnIndex("bloodSugar"));
        String dateTime = cursor.getString(cursor.getColumnIndex("dateTime"));

        UricAcid item = new UricAcid(dateTime, ua);
        item.setBloodSugar(bs);
        item.setId(id);

        cursor.close();

        return item;
    }

    public void addUA(UricAcid uricAcid) throws Exception{
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.insert("UricAcid", null, uricAcid.toContentValues());

        if (result == -1){
            throw new Exception("Uric acid item insertion denied");
        }else {
            Log.i(TAG, uricAcid.toContentValues().toString() + "--recorded");
        }
        db.close();
    }

    public void addBP(BloodPressure bloodPressure) throws Exception{
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.i(TAG, bloodPressure.toContentValues().toString() + "--recorded");
        long result = db.insert("BloodPressure",null,bloodPressure.toContentValues());

        if (result == -1){
            throw new Exception("Blood pressure item insertion denied");
        }else {
            Log.i(TAG, bloodPressure.toContentValues().toString() + "--recorded");
        }
        db.close();
    }

    public void deleteSingleUA(UricAcid uricAcid) throws Exception{
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long result = database.delete("UricAcid", "id=?", new String[]{String.valueOf(uricAcid.getId())});

        if (result == -1){
            throw new Exception("Uric acid item deletion denied");
        }else {
            Log.i(TAG, uricAcid.toContentValues().toString() + "--recorded");
        }
        database.close();
    }

    public void deleteSingleBP(BloodPressure bloodPressure) throws Exception{
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long result = database.delete("BloodPressure", "id=?", new String[]{String.valueOf(bloodPressure.getId())});

        if (result == -1){
            throw new Exception("Blood pressure item deletion denied");
        }else {
            Log.i(TAG, bloodPressure.toContentValues().toString() + "--deleted");
        }

        database.close();
    }

    public void modifyUA(int uricAcidId, UricAcid updated) throws Exception{
       SQLiteDatabase db = dbHelper.getWritableDatabase();
       long result = db.update("UricAcid", updated.toContentValues(), "id=?", new String[]{String.valueOf(uricAcidId)});

        if (result == -1){
            throw new Exception("Uric acid item modification denied");
        }else {
            Log.i(TAG, updated.toContentValues().toString() + "--updated");
        }

        db.close();
    }

    public void modifyBP(int bloodPressureId, BloodPressure updated) throws Exception {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.update("BloodPressure", updated.toContentValues(), "id=?", new String[]{String.valueOf(bloodPressureId)});

        if (result == -1){
            throw new Exception("Blood pressure item modification denied");
        }else {
            Log.i(TAG, updated.toContentValues().toString() + "--updated");
        }

        db.close();
    }


}
