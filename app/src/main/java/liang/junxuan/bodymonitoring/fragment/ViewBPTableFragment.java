package liang.junxuan.bodymonitoring.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import liang.junxuan.bodymonitoring.R;
import liang.junxuan.bodymonitoring.ViewBodyData;
import liang.junxuan.bodymonitoring.adapter.bpRecyclerViewAdapter;
import liang.junxuan.bodymonitoring.dataBase.bodyMonitordbHelper;
import liang.junxuan.bodymonitoring.item.bloodPressure;

public class ViewBPTableFragment extends Fragment {
    private bodyMonitordbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new bodyMonitordbHelper(getActivity(), "BodyMonitoring.db", null, 1);
        View rootView = loadRootView(inflater, container);

        RecyclerView recyclerView = rootView.findViewById(R.id.blood_pressure_recycler_view);
        bpRecyclerViewAdapter adapter = new bpRecyclerViewAdapter(findAllBP());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container){
        return inflater.inflate(R.layout.fragment_view_blood_pressure, container, false);
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



}
