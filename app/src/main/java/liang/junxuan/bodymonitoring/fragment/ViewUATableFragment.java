package liang.junxuan.bodymonitoring.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import liang.junxuan.bodymonitoring.R;
import liang.junxuan.bodymonitoring.adapter.BPRecyclerViewAdapter;
import liang.junxuan.bodymonitoring.adapter.UARecyclerViewAdapter;
import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;
import liang.junxuan.bodymonitoring.item.UricAcid;
import liang.junxuan.bodymonitoring.util.DBManager;

public class ViewUATableFragment extends Fragment {
    private ArrayList<UricAcid> ua_list;
    private View rootView;
    private DBManager.Time_Interval time_interval;

    public ViewUATableFragment(ArrayList<UricAcid> list, DBManager.Time_Interval time_interval){
        ua_list = list;
        this.time_interval = time_interval;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = loadRootView(inflater, container);

        RecyclerView recyclerView = rootView.findViewById(R.id.uric_acid_recycler_view);
        UARecyclerViewAdapter adapter = new UARecyclerViewAdapter(ua_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        BodyMonitordbHelper dbHelper = new BodyMonitordbHelper(getActivity(), "BodyMonitoring.db", null, 1);
        DBManager manager = new DBManager(dbHelper);

        RecyclerView recyclerView = rootView.findViewById(R.id.uric_acid_recycler_view);
        UARecyclerViewAdapter adapter;

        if (time_interval == DBManager.Time_Interval.ALL){
            adapter = new UARecyclerViewAdapter(manager.findAllUA());
        }else {
            adapter = new UARecyclerViewAdapter(manager.findUAbyTime(time_interval));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        super.onResume();
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container){
        return inflater.inflate(R.layout.fragment_view_uric_acid, container, false);
    }



}
