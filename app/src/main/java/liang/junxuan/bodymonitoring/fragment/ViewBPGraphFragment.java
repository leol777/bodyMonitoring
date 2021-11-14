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
import liang.junxuan.bodymonitoring.item.bloodPressure;
import liang.junxuan.bodymonitoring.util.dbFinder;

public class ViewBPGraphFragment extends Fragment {
    private bodyMonitordbHelper dbHelper;
    private dbFinder finder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new bodyMonitordbHelper(getActivity(), "BodyMonitoring.db", null, 1);
        finder = new dbFinder(dbHelper);
        View root_view = loadRootView(inflater, container);

        GraphView graphView = root_view.findViewById(R.id.view_bp_graph_view);

        try {
            drawAllBP(graphView);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return root_view;
    }

    private View loadRootView(LayoutInflater inflater, ViewGroup container){
        return inflater.inflate(R.layout.fragment_view_bp_graph, container, false);
    }

    private void drawAllBP(GraphView graphView) throws ParseException {
        ArrayList<bloodPressure> list = finder.findAllBP();
        LineGraphSeries<DataPoint> lower_series = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> upper_series = new LineGraphSeries<>();
        for (bloodPressure item : list){
            lower_series.appendData(new DataPoint(item.getDateTimeInDate(), item.getLowerPressure()),false, list.size());
            upper_series.appendData(new DataPoint(item.getDateTimeInDate(), item.getUpperPressure()),false, list.size());
        }
        graphView.addSeries(lower_series);
        graphView.addSeries(upper_series);
        graphView.setTitle("血压历史记录检测图");
        graphView.setTitleColor(R.color.colorPrimary);
        graphView.setTitleTextSize(18);
    }
}
