package liang.junxuan.bodymonitoring.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;
import liang.junxuan.bodymonitoring.item.BloodPressure;
import liang.junxuan.bodymonitoring.item.UricAcid;

public class DBManager {
    final private String TAG = "DataBaseManager";
    private BodyMonitordbHelper dbHelper;

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
        return list;
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
