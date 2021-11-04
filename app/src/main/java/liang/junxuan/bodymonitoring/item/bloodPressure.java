package liang.junxuan.bodymonitoring.item;

import android.content.ContentValues;
import android.text.PrecomputedText;

public class bloodPressure {
    private int upperPressure;
    private int lowerPressure;
    private int heartBeat;
    private String dateTime;

    public bloodPressure(String dt, int up, int low){
        upperPressure = up;
        lowerPressure = low;
        dateTime = dt;
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

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put("dateTime", dateTime);
        values.put("upperBP", upperPressure);
        values.put("lowerBP", lowerPressure);
        values.put("heartBeat", heartBeat);

        return values;
    }
}
