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
import liang.junxuan.bodymonitoring.adapter.UARecyclerViewAdapter;
import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;
import liang.junxuan.bodymonitoring.util.DBManager;

public class ViewUATableFragment extends Fragment {
    private BodyMonitordbHelper dbHelper;

    private View rootView;

    private DBManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new BodyMonitordbHelper(getActivity(), "BodyMonitoring.db", null, 1);
        rootView = loadRootView(inflater, container);

        manager = new DBManager(dbHelper);

        RecyclerView recyclerView = rootView.findViewById(R.id.uric_acid_recycler_view);
        UARecyclerViewAdapter adapter = new UARecyclerViewAdapter(manager.findAllUA());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        RecyclerView recyclerView = rootView.findViewById(R.id.uric_acid_recycler_view);
        UARecyclerViewAdapter adapter = new UARecyclerViewAdapter(manager.findAllUA());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        super.onResume();
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container){
        return inflater.inflate(R.layout.fragment_view_uric_acid, container, false);
    }



}
