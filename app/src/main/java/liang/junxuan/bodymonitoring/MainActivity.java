package liang.junxuan.bodymonitoring;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

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
                Intent record_bp_intent = new Intent(MainActivity.this,RecordBloodPressure.class);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}