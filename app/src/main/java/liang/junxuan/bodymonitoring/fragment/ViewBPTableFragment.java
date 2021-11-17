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

import liang.junxuan.bodymonitoring.R;
import liang.junxuan.bodymonitoring.adapter.BPRecyclerViewAdapter;
import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;
import liang.junxuan.bodymonitoring.util.DBManager;

public class ViewBPTableFragment extends Fragment {
    private BodyMonitordbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new BodyMonitordbHelper(getActivity(), "BodyMonitoring.db", null, 1);
        View rootView = loadRootView(inflater, container);

        DBManager db_finder = new DBManager(dbHelper);

        RecyclerView recyclerView = rootView.findViewById(R.id.blood_pressure_recycler_view);
        BPRecyclerViewAdapter adapter = new BPRecyclerViewAdapter(db_finder.findAllBP());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container){
        return inflater.inflate(R.layout.fragment_view_blood_pressure, container, false);
    }




}
