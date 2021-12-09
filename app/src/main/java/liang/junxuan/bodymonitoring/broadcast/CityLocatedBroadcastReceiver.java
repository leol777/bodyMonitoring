package liang.junxuan.bodymonitoring.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import liang.junxuan.bodymonitoring.services.getLocalWeatherService;

public class CityLocatedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);

        Intent intent1 = new Intent(context, getLocalWeatherService.class);
        intent1.putExtra("latitude", latitude);
        intent1.putExtra("longitude", longitude);
        context.startService(intent1);
    }

}
