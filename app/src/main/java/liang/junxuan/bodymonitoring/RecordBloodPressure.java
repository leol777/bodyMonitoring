package liang.junxuan.bodymonitoring;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;

import java.util.Calendar;
import java.util.concurrent.BlockingDeque;

import liang.junxuan.bodymonitoring.item.bloodPressure;
import liang.junxuan.bodymonitoring.dataBase.bodyMonitordbHelper;

public class RecordBloodPressure extends AppCompatActivity {
    private final static String TAG = "Record Blood Pressure";

    private final EditText upperBpInput = findViewById(R.id.upper_bp_input);
    private final EditText lowerBpInput = findViewById(R.id.lower_bp_input);
    private final EditText heartBeatInput = findViewById(R.id.heart_beat_input);

    private bodyMonitordbHelper dBhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_blood_pressure);

        dBhelper = new bodyMonitordbHelper(this, "BodyMonitoring.db", null, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle(R.string.record_blood_pressure_val);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.record_form_menu,menu);
        return true;
    }

    private void submitBloodPressure(){
        int upperBP = Integer.parseInt(String.valueOf(upperBpInput.getText()));
        int lowerBP = Integer.parseInt(String.valueOf(lowerBpInput.getText()));
        int heartBeat = Integer.parseInt(String.valueOf(heartBeatInput.getText()));

        Calendar calendar = Calendar.getInstance();
        String dateTime = calendar.getTime().toString();

        bloodPressure bp = new bloodPressure(dateTime, upperBP, lowerBP);
        bp.setHeartBeat(heartBeat);

        SQLiteDatabase db = dBhelper.getWritableDatabase();
        Log.i(TAG, bp.toContentValues().toString() + "--recorded");
        //db.insert("BloodPressure",null,bp.toContentValues());
    }
}