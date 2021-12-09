package liang.junxuan.bodymonitoring.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;

import liang.junxuan.bodymonitoring.R;
import liang.junxuan.bodymonitoring.item.BloodPressure;
import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;
import liang.junxuan.bodymonitoring.services.CityLocatorService;
import liang.junxuan.bodymonitoring.util.DBManager;
import liang.junxuan.bodymonitoring.util.DateTimeStringConverter;

public class RecordBloodPressure extends AppCompatActivity {
    private final static String TAG = "Record Blood Pressure";
    private BPWeatherRecordedBroadcastReceiver receiver;

    private EditText upperBpInput;
    private EditText lowerBpInput;
    private EditText heartBeatInput;

    private ImageButton datePicker;
    private TextView dateText;

    private ImageButton timePicker;
    private TextView timeText;

    private Calendar record_date_time;

    private BodyMonitordbHelper dBhelper;

    private TextView weatherText;

    public RecordBloodPressure() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_blood_pressure);

        datePicker = findViewById(R.id.record_bp_date_picker_button);
        timePicker = findViewById(R.id.record_bp_time_picker_button);

        record_date_time = Calendar.getInstance();
        final int mHourOfDay = record_date_time.get(Calendar.HOUR_OF_DAY);
        final int mMinute = record_date_time.get(Calendar.MINUTE);
        final int mDateOfMonth = record_date_time.get(Calendar.DAY_OF_MONTH);
        final int mMonth = record_date_time.get(Calendar.MONTH);
        final int mYear = record_date_time.get(Calendar.YEAR);

        upperBpInput = findViewById(R.id.upper_bp_input);
        lowerBpInput = findViewById(R.id.lower_bp_input);
        heartBeatInput = findViewById(R.id.heart_beat_input);

        dateText = findViewById(R.id.record_bp_date);
        try {
            dateText.setText(DateTimeStringConverter.toDateStringInChinese(record_date_time.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeText = findViewById(R.id.record_bp_time);
        try {
            String text = DateTimeStringConverter.toTimeStringInChinese(record_date_time.getTime());
            timeText.setText(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RecordBloodPressure.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                record_date_time.set(Calendar.YEAR, year);
                                record_date_time.set(Calendar.MONTH, month);
                                record_date_time.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                try {
                                    dateText.setText(DateTimeStringConverter.toDateStringInChinese(record_date_time.getTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },mYear, mMonth, mDateOfMonth).show();
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new TimePickerDialog(RecordBloodPressure.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                record_date_time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                record_date_time.set(Calendar.MINUTE, minute);

                                try {
                                    timeText.setText(DateTimeStringConverter.toTimeStringInChinese(record_date_time.getTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mHourOfDay, mMinute, true).show();
            }
        });


        dBhelper = new BodyMonitordbHelper(this, "BodyMonitoring.db", null, 1);

         weatherText = findViewById(R.id.record_bp_weather);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.submit_icon:
                confirmDialog();
                return true;
            case R.id.record_view_body_data:
                Intent intent = new Intent(RecordBloodPressure.this, ViewBodyData.class);
                startActivity(intent);
        }
        return true;
    }

    private void confirmDialog(){
        AlertDialog.Builder cd = new AlertDialog.Builder(this);

        cd.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    submitBloodPressure();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(RecordBloodPressure.this, "创建血压记录失败", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                RecordBloodPressure.this.finish();
            }
        });

        cd.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        cd.setMessage("血压高压为："+upperBpInput.getText()+"mmHg\n"
                +"血压低压为："+lowerBpInput.getText()+"mmHg\n"
                +"心率为："+heartBeatInput.getText()+"bpm");

        cd.setTitle("请确认您输入的血压信息是否准确？");
        cd.show();
    }

    private void submitBloodPressure() throws Exception {
        int upperBP;
        if (!upperBpInput.getText().toString().equals("")){
            upperBP = Integer.parseInt(String.valueOf(upperBpInput.getText()));
        }else{
            upperBP = -1;
        }

        int lowerBP;
        if (!lowerBpInput.getText().toString().equals("")){
            lowerBP = Integer.parseInt(String.valueOf(lowerBpInput.getText()));

        }else{
            lowerBP = -1;
        }

        int heartBeat;
        if (!heartBeatInput.getText().toString().equals("")){
            heartBeat = Integer.parseInt(String.valueOf(heartBeatInput.getText()));
        }else {
            heartBeat = -1;
        }


        String dateTime = record_date_time.getTime().toString();

        BloodPressure bp = new BloodPressure(dateTime, upperBP, lowerBP);
        bp.setHeartBeat(heartBeat);

        DBManager dbManager = new DBManager(dBhelper);
        dbManager.addBP(bp);
    }

    @Override
    protected void onStart() {
        Intent intent = new Intent(this, CityLocatorService.class);
        startService(intent);

        receiver = new BPWeatherRecordedBroadcastReceiver(this);
        receiver.register();

        super.onStart();
    }

    @Override
    protected void onStop() {
        receiver.unregister();
        super.onStop();
    }

    class BPWeatherRecordedBroadcastReceiver extends BroadcastReceiver{
        private Context context;

        public BPWeatherRecordedBroadcastReceiver(Context context){
            this.context = context;

        }

        public void register(){
            IntentFilter filter = new IntentFilter("weather_stored");
            context.registerReceiver(this, filter);
        }

        public void unregister(){
            context.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences preferences = getSharedPreferences("weather", Context.MODE_PRIVATE);
            String des = preferences.getString("weather_description", "" );
            String wind = preferences.getString("wind", "");
            float humidity = preferences.getFloat("humidity", 0);
            float temp = preferences.getFloat("temperature", 0);

            String weather_text = "今天天气：" + des + "，温度为：" + temp + "，湿度：" + humidity + "，风：" + wind;
            weatherText.setText(weather_text);
        }
    }
}