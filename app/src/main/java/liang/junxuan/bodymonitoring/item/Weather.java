package liang.junxuan.bodymonitoring.item;

import org.json.JSONException;
import org.json.JSONObject;

public class Weather {
    private float temperature;
    private float humidity;

    private String wind;
    private String weather_description;

    public Weather(float temperature, float humidity, String  wind, String weather_description){
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.weather_description = weather_description;

    }

    public Weather(JSONObject jsonObject){
        try {
            float temp = (float) jsonObject.getDouble("qw");
            float humi = (float) jsonObject.getDouble("sd");
            String des = jsonObject.getString("tq");
            String wind = jsonObject.getString("fl");

            this.temperature = temp;
            this.humidity = humi;
            this.weather_description = des;
            this.wind = wind;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public float getTemperature(){return temperature;}

    public String getWind(){return wind;}

    public float getHumidity(){
        return humidity;
    }

    public String getWeather_description(){ return weather_description;}

}
