package liang.junxuan.bodymonitoring.item;

import android.content.ContentValues;
import android.text.PrecomputedText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BloodPressure implements Comparable {
    private int upperPressure;
    private int lowerPressure;
    private int heartBeat;
    private String dateTime;
    private int id;
    private Date date_time_in_date;

    public BloodPressure(String dt, int up, int low) {
        upperPressure = up;
        lowerPressure = low;
        dateTime = dt;
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            date_time_in_date = sdf.parse(dt);
        }catch (ParseException ex){
            ex.printStackTrace();
        }
    }

    public int getUpperPressure(){
        return upperPressure;
    }

    public int getLowerPressure(){
        return lowerPressure;
    }

    public void setHeartBeat(int hb){
        heartBeat = hb;
    }

    public int getHeartBeat(){
        return heartBeat;
    }

    public String getDateTime(){
        return dateTime;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put("dateTime", dateTime);
        values.put("upperBP", upperPressure);
        values.put("lowerBP", lowerPressure);
        values.put("heartBeat", heartBeat);

        return values;
    }

    public Date getDateTimeInDate() {
        return date_time_in_date;
    }

    @Override
    public int compareTo(Object other) {
        BloodPressure other_bp = (BloodPressure) other;
        int date_difference = this.date_time_in_date.compareTo(other_bp.getDateTimeInDate());
        if (date_difference == 0){
            return upperPressure - other_bp.getUpperPressure();
        }else {
            return date_difference;
        }
    }
}
