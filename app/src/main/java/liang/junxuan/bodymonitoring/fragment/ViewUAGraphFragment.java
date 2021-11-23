package liang.junxuan.bodymonitoring.fragment;

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
import liang.junxuan.bodymonitoring.util.BodyMonitorDataPoint;
import liang.junxuan.bodymonitoring.util.DBManager;
import liang.junxuan.bodymonitoring.item.UricAcid;

public class ViewUAGraphFragment extends Fragment {
    private ArrayList<UricAcid> ua_list;

    public ViewUAGraphFragment(ArrayList<UricAcid> list){
        ua_list = list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root_view = loadRootView(inflater, container);
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
        ArrayList<UricAcid> list = ua_list;
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        //Dummy variable to avoid untagable first point bug
        BodyMonitorDataPoint dummy_dp = new BodyMonitorDataPoint(0,0);
        series.appendData(dummy_dp, true, list.size()+1);

        int i = 1;
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
                Toast.makeText(getActivity(), " 尿酸为："+dp.getY()+ getResources().getString(R.string.uric_acid_unit)+" 记录时间："
                        +out_date, Toast.LENGTH_SHORT).show();
            }
        });

        graphView.getViewport().setMinX(1);
        graphView.getViewport().setMaxX(list.size());
        graphView.getViewport().setXAxisBoundsManual(true);

        graphView.addSeries(series);
        graphView.setTitle("尿酸历史记录检测图");
        graphView.setTitleColor(R.color.colorPrimary);
        graphView.setTitleTextSize(18);
    }
}
