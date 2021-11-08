package liang.junxuan.bodymonitoring;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Calendar;
import java.util.concurrent.BlockingDeque;

import liang.junxuan.bodymonitoring.item.bloodPressure;
import liang.junxuan.bodymonitoring.dataBase.bodyMonitordbHelper;

public class RecordBloodPressure extends AppCompatActivity {
    private final static String TAG = "Record Blood Pressure";

    private EditText upperBpInput;
    private EditText lowerBpInput;
    private EditText heartBeatInput;

    private bodyMonitordbHelper dBhelper;

    public RecordBloodPressure() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_blood_pressure);

        upperBpInput = findViewById(R.id.upper_bp_input);
        lowerBpInput = findViewById(R.id.lower_bp_input);
        heartBeatInput = findViewById(R.id.heart_beat_input);


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.submit_icon:
                confirmDialog();
                return true;
        }
        return true;
    }

    private void confirmDialog(){
        AlertDialog.Builder cd = new AlertDialog.Builder(this);

        cd.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitBloodPressure();
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

    private void submitBloodPressure(){
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


        Calendar calendar = Calendar.getInstance();
        String dateTime = calendar.getTime().toString();

        bloodPressure bp = new bloodPressure(dateTime, upperBP, lowerBP);
        bp.setHeartBeat(heartBeat);

        SQLiteDatabase db = dBhelper.getWritableDatabase();
        Log.i(TAG, bp.toContentValues().toString() + "--recorded");
        db.insert("BloodPressure",null,bp.toContentValues());
        db.close();
    }
}