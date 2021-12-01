package liang.junxuan.bodymonitoring.services;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CityLocatorService extends Service {
    private static final String TAG = "CityLocatorService";
    private Location location;

    private Handler handler;
    private String message;


    @Override
    public void onCreate() {
        Log.i(TAG, TAG+" onCreated");
        super.onCreate();
        getLocation();
    }

    private void getLocation() {
        // 获取位置管理服务
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) this.getSystemService(serviceName);
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        assert provider != null;
        location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
        getCity(location);
    }

    private void getCity(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        handler = new android.os.Handler(Looper.getMainLooper());
        final String baiduMapUrl = "http://api.map.baidu.com/geocoder?output=json&location="+latitude+","+longitude;
        Log.i(TAG, "getCity: "+baiduMapUrl);

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(baiduMapUrl).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                message = e.toString();
                Log.e(TAG, message);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                message = response.body().string();
                Log.i(TAG, message);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (response.code() == 200){
                                JSONObject location_detail = (JSONObject) new JSONObject(message).get("result");
                                JSONObject addressComponent = (JSONObject) location_detail.get("addressComponent");
                                String city = addressComponent.getString("city");
                                SharedPreferences sharedPreferences = getSharedPreferences("weather", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("city", city);
                                editor.apply();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
