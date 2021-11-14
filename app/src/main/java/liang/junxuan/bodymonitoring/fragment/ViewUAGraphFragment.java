package liang.junxuan.bodymonitoring.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.util.ArrayList;

import liang.junxuan.bodymonitoring.R;
import liang.junxuan.bodymonitoring.dataBase.bodyMonitordbHelper;
import liang.junxuan.bodymonitoring.util.dbFinder;
import liang.junxuan.bodymonitoring.item.uricAcid;

public class ViewUAGraphFragment extends Fragment {
    private bodyMonitordbHelper dbHelper;
    private dbFinder finder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new bodyMonitordbHelper(getActivity(), "BodyMonitoring.db", null, 1);
        View root_view = loadRootView(inflater, container);
        finder = new dbFinder(dbHelper);

        GraphView graphView = root_view.findViewById(R.id.view_ua_graph_view);

        try {
            drawAllUA(graphView);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return root_view;
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container){
        return inflater.inflate(R.layout.fragment_view_ua_graph, container, false);
    }

    private void drawAllUA(GraphView graphView) throws ParseException {
        ArrayList<uricAcid> list = finder.findAllUA();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (uricAcid item : list){
            series.appendData(new DataPoint(item.getDateTimeInDate(),item.getUricAcid()),false,list.size());
        }
        graphView.addSeries(series);
        graphView.setTitle("尿酸历史记录检测图");
        graphView.setTitleColor(R.color.colorPrimary);
        graphView.setTitleTextSize(18);
    }
}
