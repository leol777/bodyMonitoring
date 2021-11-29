package liang.junxuan.bodymonitoring.util;

import com.jjoe64.graphview.series.DataPoint;

import java.util.Date;

public class BodyMonitorDataPoint extends DataPoint {
    private Date DateTime;
    private double extra;

    public BodyMonitorDataPoint(int x, double y) {
        super(x, y);
    }

    public BodyMonitorDataPoint(Date x, double y) {super(x, y);}

    public void setDateTime(Date d){
        DateTime = d;
    }

    public Date getDateTime(){
        return DateTime;
    }

    public void setExtra(double extra){
        this.extra = extra;
    }

    public double getExtra(){return extra;}
}
