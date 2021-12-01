package liang.junxuan.bodymonitoring.item;

public class Weather {
    private int upper_temp;
    private int lower_temp;
    private int humidity;

    public Weather(int up, int low, int humidity){
        upper_temp = up;
        lower_temp = low;
        this.humidity = humidity;
    }

    public int getUpper_temp(){return upper_temp;}

    public int getLower_temp(){return lower_temp;}

    public int getHumidity(){
        return humidity;
    }
}
