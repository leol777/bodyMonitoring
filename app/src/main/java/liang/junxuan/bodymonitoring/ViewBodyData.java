package liang.junxuan.bodymonitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.UriCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import liang.junxuan.bodymonitoring.item.*;
import liang.junxuan.bodymonitoring.dataBase.bodyMonitordbHelper;
import liang.junxuan.bodymonitoring.adapter.*;

public class ViewBodyData extends AppCompatActivity {
    private bodyMonitordbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_body_data);

        dbHelper = new bodyMonitordbHelper(ViewBodyData.this,"BodyMonitoring.db",null,1);

        RecyclerView recyclerView = findViewById(R.id.blood_pressure_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        uaRecyclerViewAdapter adapter = new uaRecyclerViewAdapter(findAllUA());
        recyclerView.setAdapter(adapter);

    }

    private ArrayList<bloodPressure> findAllBP(){
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

    private ArrayList<uricAcid> findAllUA(){
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