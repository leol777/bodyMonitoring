package liang.junxuan.bodymonitoring.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import liang.junxuan.bodymonitoring.R;
import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;
import liang.junxuan.bodymonitoring.item.BloodPressure;
import liang.junxuan.bodymonitoring.util.BodyMonitorDataPoint;
import liang.junxuan.bodymonitoring.util.DBManager;

public class ViewBPGraphFragment extends Fragment {
    private BodyMonitordbHelper dbHelper;
    private DBManager finder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new BodyMonitordbHelper(getActivity(), "BodyMonitoring.db", null, 1);
        finder = new DBManager(dbHelper);
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
        ArrayList<BloodPressure> list = finder.findAllBP();
        LineGraphSeries<DataPoint> lower_series = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> upper_series = new LineGraphSeries<>();

        //Dummy variable to avoid untagable first point bug
        BodyMonitorDataPoint dummy_dp = new BodyMonitorDataPoint(0,0);
        lower_series.appendData(dummy_dp, false, list.size()+1);
        upper_series.appendData(dummy_dp, false, list.size()+1);

        int i = 1;
        for (BloodPressure item : list){
            BodyMonitorDataPoint low_dp = new BodyMonitorDataPoint(i, item.getLowerPressure());
            low_dp.setDateTime(item.getDateTimeInDate());
            BodyMonitorDataPoint up_dp = new BodyMonitorDataPoint(i, item.getUpperPressure());
            up_dp.setDateTime(item.getDateTimeInDate());

            lower_series.appendData(low_dp,false, list.size()+1);
            upper_series.appendData(up_dp,false, list.size()+1);
            i++;
        }
        lower_series.setDrawDataPoints(true);
        upper_series.setDrawDataPoints(true);

        lower_series.setDataPointsRadius(10);
        upper_series.setDataPointsRadius(10);

        upper_series.setColor(Color.RED);

        lower_series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                BodyMonitorDataPoint dp = (BodyMonitorDataPoint) dataPoint;
                SimpleDateFormat out_sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINA);
                Date date = dp.getDateTime();
                assert date != null;
                String out_date = out_sdf.format(date);
                Toast.makeText(getActivity(), "低压为："+dataPoint.getY()+ getResources().getString(R.string.blood_pressure_unit)+ " 记录时间："
                        +out_date, Toast.LENGTH_SHORT).show();
            }
        });

        upper_series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                BodyMonitorDataPoint dp = (BodyMonitorDataPoint) dataPoint;
                SimpleDateFormat out_sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINA);
                Date date = dp.getDateTime();
                assert date != null;
                String out_date = out_sdf.format(date);
                Toast.makeText(getActivity(), "高压为："+dataPoint.getY()+ getResources().getString(R.string.blood_pressure_unit)+ " 记录时间："
                        +out_date, Toast.LENGTH_SHORT).show();
            }
        });

        graphView.getViewport().setMinX(1);
        graphView.getViewport().setMaxX(list.size());
        graphView.getViewport().setXAxisBoundsManual(true);

        graphView.addSeries(lower_series);
        graphView.addSeries(upper_series);
        graphView.setTitle("血压历史记录检测图");
        graphView.setTitleColor(R.color.colorPrimary);
        graphView.setTitleTextSize(18);
    }
}
