package liang.junxuan.bodymonitoring.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import liang.junxuan.bodymonitoring.R;
import liang.junxuan.bodymonitoring.services.CityLocatorService;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_GRANT_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button bp_button = findViewById(R.id.button_record_blood_pressure);
        final Button ua_button = findViewById(R.id.button_record_uric_acid);
        final Button view_data_button = findViewById(R.id.button_view_body_data);

        bp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent record_bp_intent = new Intent(MainActivity.this, RecordBloodPressure.class);
                startActivity(record_bp_intent);
            }
        });

        ua_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent record_ua_intent = new Intent(MainActivity.this, RecordUricAcid.class);
                startActivity(record_ua_intent);
            }
        });

        view_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_data_intent = new Intent(MainActivity.this, ViewBodyData.class);
                startActivity(view_data_intent);
            }
        });

        setRequestGrantPermission();
        Intent intent = new Intent(this, CityLocatorService.class);
        startService(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void setRequestGrantPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_GRANT_PERMISSION){
            Intent intent = new Intent(this, CityLocatorService.class);
            startService(intent);
        }else {
            Toast.makeText(this, "无位置权限无法获得天气信息", Toast.LENGTH_LONG).show();
        }
    }
}