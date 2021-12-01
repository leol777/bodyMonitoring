package liang.junxuan.bodymonitoring.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import liang.junxuan.bodymonitoring.item.Weather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class getLocalWeatherService extends Service {
    private static final String TAG = "getLocalWeatherService";
    private Handler handler;
    private String message;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getWeather(int cityCode){
        handler = new Handler(getMainLooper());
        final String weather_url = "";

        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                message = e.toString();
                Log.e(TAG, message);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Unable to load weather info", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                message = response.body().string();
                Log.i(TAG, message);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 201){
                            Toast.makeText(getApplicationContext(), "气象数据库内部错误", Toast.LENGTH_LONG).show();
                        }else if (response.code() == 202){
                            Toast.makeText(getApplicationContext(), "气象数据库结果集为空", Toast.LENGTH_LONG).show();
                        }else if (response.code() == 203){
                            Toast.makeText(getApplicationContext(), "气象数据库参数错误", Toast.LENGTH_LONG).show();
                        }else {
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Weather weather = parseWetherJson(jsonArray);
                            storeWeather(weather);
                        }
                    }
                });
            }
        });

    }

    private void storeWeather(Weather weather){
        SharedPreferences preferences = getSharedPreferences("weather", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        editor.putInt("upperTemperature", weather.getUpper_temp());
        editor.putInt("lowerTemperature", weather.getLower_temp());
        editor.putInt("humidity", weather.getHumidity());

        editor.apply();
    }

    private Weather parseWetherJson(JSONArray jsonArray ){
        Weather weather = new Weather(0,0,0);
        return weather;

    }
}
