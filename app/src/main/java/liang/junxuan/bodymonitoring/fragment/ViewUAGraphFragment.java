package liang.junxuan.bodymonitoring.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Group;
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
import java.util.Objects;

import liang.junxuan.bodymonitoring.R;
import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;
import liang.junxuan.bodymonitoring.util.BodyMonitorDataPoint;
import liang.junxuan.bodymonitoring.util.DBManager;
import liang.junxuan.bodymonitoring.item.UricAcid;

public class ViewUAGraphFragment extends Fragment {
    private ArrayList<UricAcid> ua_list;
    private DBManager.Time_Interval time_interval;
    private GraphView graphView;
    private TextView info_window_text;
    private ImageButton cancel_button;

    public ViewUAGraphFragment(ArrayList<UricAcid> list, DBManager.Time_Interval time_interval){
        ua_list = list;
        this.time_interval = time_interval;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root_view = loadRootView(inflater, container);
        graphView = root_view.findViewById(R.id.view_ua_graph_view);

        info_window_text = requireActivity().findViewById(R.id.info_window_text);
        cancel_button = requireActivity().findViewById(R.id.cancel_info_window);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_button.setVisibility(View.GONE);
                info_window_text.setVisibility(View.GONE);
            }
        });


        if (ua_list.size() == 0){
            return root_view;
        }

        try {
            drawAllUA(graphView, ua_list);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return root_view;
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container){
        return inflater.inflate(R.layout.fragment_view_ua_graph, container, false);
    }

    private void drawAllUA(GraphView graphView, ArrayList<UricAcid> list) throws ParseException {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        //Dummy variable to avoid untagable first point bug
        BodyMonitorDataPoint dummy_dp = new BodyMonitorDataPoint(0,list.get(0).getUricAcid());
        series.appendData(dummy_dp, true, list.size()+1);

        int i = 0;
        for (UricAcid item : list){
            BodyMonitorDataPoint dp = new BodyMonitorDataPoint(i,item.getUricAcid());
            dp.setDateTime(item.getDateTimeInDate());
            series.appendData(dp,true,list.size()+1);
            i++;
        }

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                BodyMonitorDataPoint dp = (BodyMonitorDataPoint) dataPoint;
                SimpleDateFormat out_sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINA);
                Date date = dp.getDateTime();
                assert date != null;
                String out_date = out_sdf.format(date);
                info_window_text.setText(" 尿酸为："+dp.getY()+ getResources().getString(R.string.uric_acid_unit)+ "\n" +
                        " 记录时间：" +out_date);
                info_window_text.setVisibility(View.VISIBLE);
                cancel_button.setVisibility(View.VISIBLE);
            }

        });

        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(list.size()-1);
        graphView.getViewport().setXAxisBoundsManual(true);

        graphView.addSeries(series);
        graphView.setTitle("尿酸历史记录检测图");
        graphView.setTitleColor(R.color.colorPrimary);
        graphView.setTitleTextSize(18);
    }

    @Override
    public void onResume() {
        graphView.removeAllSeries();

        BodyMonitordbHelper dbHelper = new BodyMonitordbHelper(getActivity(), "BodyMonitoring.db", null, 1);
        DBManager manager = new DBManager(dbHelper);
        ArrayList<UricAcid> list;
        if (time_interval == DBManager.Time_Interval.ALL){
            list = manager.findAllUA();
        }else {
            list = manager.findUAbyTime(time_interval);
        }

        if (list.size() == 0){
            super.onResume();
            return;
        }

        try {
            drawAllUA(graphView, list);
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
