package liang.junxuan.bodymonitoring.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.util.Calendar;

import liang.junxuan.bodymonitoring.R;
import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;
import liang.junxuan.bodymonitoring.item.UricAcid;
import liang.junxuan.bodymonitoring.util.DBManager;
import liang.junxuan.bodymonitoring.util.DateTimeStringConverter;

public class EditUricAcid extends AppCompatActivity {
    private final static String TAG = "Record Uric Acid";

    private EditText uricAcidInput;
    private EditText bloodSugarInput;

    private ImageButton datePicker;
    private TextView dateText;

    private ImageButton timePicker;
    private TextView timeText;

    private Calendar record_date_time;

    private int ua_id;

    private BodyMonitordbHelper dBhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_uric_acid);

        ua_id = getIntent().getIntExtra("ua_id", -1);

        dBhelper = new BodyMonitordbHelper(this, "BodyMonitoring.db",null,1);
        DBManager manager = new DBManager(dBhelper);

        UricAcid original_ua = manager.findUAbyId(ua_id);

        datePicker = findViewById(R.id.record_ua_date_picker_button);
        timePicker = findViewById(R.id.record_ua_time_picker_button);

        record_date_time = DateTimeStringConverter.toCalendarFromDate(original_ua.getDateTimeInDate());
        final int mHourOfDay = record_date_time.get(Calendar.HOUR_OF_DAY);
        final int mMinute = record_date_time.get(Calendar.MINUTE);
        final int mDateOfMonth = record_date_time.get(Calendar.DAY_OF_MONTH);
        final int mMonth = record_date_time.get(Calendar.MONTH);
        final int mYear = record_date_time.get(Calendar.YEAR);

        uricAcidInput = findViewById(R.id.uric_acid_input);
        uricAcidInput.setText(String.valueOf(original_ua.getUricAcid()));

        bloodSugarInput = findViewById(R.id.blood_sugar_input);
        bloodSugarInput.setText(String.valueOf(original_ua.getBloodSugar()));

        dateText = findViewById(R.id.record_ua_date);
        try {
            dateText.setText(DateTimeStringConverter.toDateStringInChinese(record_date_time.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeText = findViewById(R.id.record_ua_time);
        try {
            String text = DateTimeStringConverter.toTimeStringInChinese(record_date_time.getTime());
            timeText.setText(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditUricAcid.this,
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
                new TimePickerDialog(EditUricAcid.this,
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



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.edit_uric_acid_val);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_form_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cancel_edition:
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.confirm_edition:
                confirmDialog();
                return true;
        }
        return true;
    }

    private void confirmDialog(){
        AlertDialog.Builder cd = new AlertDialog.Builder(EditUricAcid.this);

        cd.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    editUricAcid();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditUricAcid.this, "修改尿酸记录失败", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                EditUricAcid.this.finish();
            }
        });

        cd.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        cd.setMessage("尿酸值为："+ uricAcidInput.getText() +"μmol/L\n"
                +"血糖值为："+ bloodSugarInput.getText() +"mmol/L");

        cd.setTitle("确定修改尿酸数据：");
        cd.show();
    }

    private void editUricAcid() throws Exception {
        int uricAcidVal;
        if (!uricAcidInput.getText().toString().equals("")){
            uricAcidVal = Integer.parseInt(String.valueOf(uricAcidInput.getText()));
        }else{
            uricAcidVal = -1;
        }

        int bloodSugarVal;
        if (!bloodSugarInput.getText().toString().equals("")){
            bloodSugarVal = Integer.parseInt(String.valueOf(bloodSugarInput.getText()));
        }else{
            bloodSugarVal = -1;
        }

        String dateTime = record_date_time.getTime().toString();

        UricAcid ua = new UricAcid(dateTime, uricAcidVal);
        ua.setBloodSugar(bloodSugarVal);

        DBManager dbManager = new DBManager(dBhelper);
        dbManager.modifyUA(ua_id, ua);
    }
}
