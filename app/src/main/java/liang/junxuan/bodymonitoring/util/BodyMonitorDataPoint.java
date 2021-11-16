package liang.junxuan.bodymonitoring.util;

import com.jjoe64.graphview.series.DataPoint;

import java.util.Date;

public class BodyMonitorDataPoint extends DataPoint {
    private Date DateTime;

    public BodyMonitorDataPoint(int x, double y) {
        super(x, y);
    }

    public void setDateTime(Date d){
        DateTime = d;
    }

    public Date getDateTime(){
        return DateTime;
    }
}
