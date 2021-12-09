package liang.junxuan.bodymonitoring.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import org.json.JSONObject;

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
        Log.i(TAG, TAG+" onCreated");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, TAG+" onStartCommand");
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);

        getWeather(latitude, longitude);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getWeather(double lat, double lon) {
        handler = new Handler(getMainLooper());
        String ak = null;
        try {
            ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            ak = appInfo.metaData.getString("Weather_Access_key");
        }catch (Exception e){
            e.printStackTrace();
        }
        final String weather_url = " http://api.yytianqi.com/observe?key=bopi3li1ip93ae0n&city="+lat+","+lon;


        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder().url(weather_url).get().build();

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
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(message);
                                JSONObject data = jsonObject.getJSONObject("data");
                                Weather weather = new Weather(data);
                                storeWeather(weather);
                                Log.i(TAG, data.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });

    }

    private void storeWeather(Weather weather){
        SharedPreferences preferences = getSharedPreferences("weather", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        editor.putFloat("temperature", weather.getTemperature());
        editor.putFloat("humidity", weather.getHumidity());
        editor.putString("weather_description", weather.getWeather_description());
        editor.putString("wind", weather.getWind());

        editor.apply();

        Intent intent = new Intent("weather_stored");
        sendBroadcast(intent);
    }


}


