package liang.junxuan.bodymonitoring.item;

import android.content.ContentValues;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class uricAcid {
    private int uricAcid;
    private int bloodSugar;
    private String date_time;

    public uricAcid(String dt, int uaVal){
        uricAcid = uaVal;
        date_time = dt;
    }

    public int getUricAcid(){
        return uricAcid;
    }

    public void setBloodSugar(int bs){
        bloodSugar = bs;
    }

    public int getBloodSugar(){
        return bloodSugar;
    }

    public String getDate_time(){
        return date_time;
    }

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put("dateTime", date_time);
        values.put("uricAcid", uricAcid);
        values.put("bloodSugar", bloodSugar);

        return values;
    }

    public Date getDateTimeInDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        return sdf.parse(this.getDate_time());
    }
}
