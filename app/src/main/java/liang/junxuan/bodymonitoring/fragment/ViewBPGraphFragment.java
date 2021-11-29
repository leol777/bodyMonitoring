package liang.junxuan.bodymonitoring.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import liang.junxuan.bodymonitoring.R;
import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;
import liang.junxuan.bodymonitoring.item.BloodPressure;
import liang.junxuan.bodymonitoring.item.UricAcid;
import liang.junxuan.bodymonitoring.util.BodyMonitorDataPoint;
import liang.junxuan.bodymonitoring.util.DBManager;
import liang.junxuan.bodymonitoring.util.DateTimeStringConverter;

public class ViewBPGraphFragment extends Fragment {
    private ArrayList<BloodPressure> bp_list;
    private DBManager.Time_Interval time_interval;
    private GraphView graphView;
    private ImageButton cancel_button;
    private TextView info_window_text;

    public ViewBPGraphFragment(ArrayList<BloodPressure> list, DBManager.Time_Interval time_interval){
        bp_list = list;
        this.time_interval = time_interval;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root_view = loadRootView(inflater, container);

        graphView = root_view.findViewById(R.id.view_bp_graph_view);

        info_window_text = requireActivity().findViewById(R.id.info_window_text);
        cancel_button = requireActivity().findViewById(R.id.cancel_info_window);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_button.setVisibility(View.GONE);
                info_window_text.setVisibility(View.GONE);
            }
        });


        if (bp_list.size() == 0){
            return root_view;
        }

        try {
            drawAllBP(graphView, bp_list);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return root_view;
    }

    private View loadRootView(LayoutInflater inflater, ViewGroup container){
        return inflater.inflate(R.layout.fragment_view_bp_graph, container, false);
    }

    private void drawAllBP(GraphView graphView, ArrayList<BloodPressure> list) throws ParseException {
        LineGraphSeries<DataPoint> lower_series = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> upper_series = new LineGraphSeries<>();

        //Dummy variable to avoid untagable first point bug
        BodyMonitorDataPoint dummy_dp_upper = new BodyMonitorDataPoint(0,list.get(0).getUpperPressure());
        BodyMonitorDataPoint dummy_dp_lower = new BodyMonitorDataPoint(0, list.get(0).getLowerPressure());
        lower_series.appendData(dummy_dp_lower, false, list.size()+1);
        upper_series.appendData(dummy_dp_upper, false, list.size()+1);

        int i = 0;
        for (BloodPressure item : list){
            BodyMonitorDataPoint low_dp = new BodyMonitorDataPoint(i, item.getLowerPressure());
            low_dp.setDateTime(item.getDateTimeInDate());
            low_dp.setExtra(item.getUpperPressure());

            BodyMonitorDataPoint up_dp = new BodyMonitorDataPoint(i, item.getUpperPressure());
            up_dp.setDateTime(item.getDateTimeInDate());
            up_dp.setExtra(item.getLowerPressure());

            lower_series.appendData(low_dp,false, list.size()+1);
            upper_series.appendData(up_dp,false, list.size()+1);
            i++;
        }
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(list.size()-1);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setScalable(true);

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
                info_window_text.setText("高压为："+((BodyMonitorDataPoint) dataPoint).getExtra()+ getResources().getString(R.string.blood_pressure_unit)+ "\n" +
                        "低压为：" + dataPoint.getY() + getResources().getString(R.string.blood_pressure_unit)+ "\n" +
                        " 记录时间：" +out_date);
                info_window_text.setVisibility(View.VISIBLE);
                cancel_button.setVisibility(View.VISIBLE);
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
                info_window_text.setText("高压为："+dataPoint.getY()+ getResources().getString(R.string.blood_pressure_unit)+ "\n" +
                        "低压为：" + ((BodyMonitorDataPoint) dataPoint).getExtra() + getResources().getString(R.string.blood_pressure_unit)+ "\n" +
                        " 记录时间：" +out_date);
                info_window_text.setVisibility(View.VISIBLE);
                cancel_button.setVisibility(View.VISIBLE);
            }
        });


        graphView.addSeries(lower_series);
        graphView.addSeries(upper_series);
        graphView.setTitle("血压历史记录检测图");
        graphView.setTitleColor(R.color.colorPrimary);
        graphView.setTitleTextSize(18);
    }

    @Override
    public void onResume() {
        BodyMonitordbHelper dbHelper = new BodyMonitordbHelper(getActivity(), "BodyMonitoring.db", null, 1);
        DBManager manager = new DBManager(dbHelper);
        graphView.removeAllSeries();

        ArrayList<BloodPressure> list;
        if (time_interval == DBManager.Time_Interval.ALL){
            list = manager.findAllBP();
        }else {
            list = manager.findBPbyTime(time_interval);
        }

        if (list.size() == 0){
            super.onResume();
            return;
        }

        try {
            drawAllBP(graphView, list);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        hide_infoWindow();
        super.onPause();
    }




    private void hide_infoWindow(){
        cancel_button.setVisibility(View.GONE);
        info_window_text.setVisibility(View.GONE);
    }
}
