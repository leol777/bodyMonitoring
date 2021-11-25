package liang.junxuan.bodymonitoring.item;

import android.content.ContentValues;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UricAcid implements Comparable{
    private int uricAcid;
    private int bloodSugar;
    private String date_time;
    private int id;
    private Date date_time_in_date;

    public UricAcid(String dt, int uaVal) {
        uricAcid = uaVal;
        date_time = dt;
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            date_time_in_date = sdf.parse(date_time);
        }catch (ParseException ex){
            ex.printStackTrace();
        }
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

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put("dateTime", date_time);
        values.put("uricAcid", uricAcid);
        values.put("bloodSugar", bloodSugar);

        return values;
    }

    public Date getDateTimeInDate() {
        return date_time_in_date;
    }

    @Override
    public int compareTo(Object o) {
        UricAcid other_ua = (UricAcid) o;
        int date_difference = this.date_time_in_date.compareTo(other_ua.getDateTimeInDate());
        if (date_difference == 0){
            return uricAcid - other_ua.getUricAcid();
        }else {
            return date_difference;
        }
    }
}
