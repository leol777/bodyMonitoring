package liang.junxuan.bodymonitoring;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import liang.junxuan.bodymonitoring.dataBase.bodyMonitordbHelper;
import liang.junxuan.bodymonitoring.item.uricAcid;

public class RecordUricAcid extends AppCompatActivity {
    private final static String TAG = "Record Uric Acid";

    private final EditText uricAcidInput = findViewById(R.id.uric_acid_input);
    private final EditText bloodSugarInput = findViewById(R.id.blood_sugar_input);

    private  bodyMonitordbHelper dBhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_uric_acid);

        dBhelper = new bodyMonitordbHelper(this, "BodyMonitor.db",null,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.record_uric_acid_val);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.record_form_menu,menu);
        return true;
    }

    private void submitUricAcid(){
        int uricAcidVal = Integer.parseInt(String.valueOf(uricAcidInput.getText()));
        int bloodSugarVal = Integer.parseInt(String.valueOf(bloodSugarInput.getText()));

        Calendar calendar = Calendar.getInstance();
        String dateTime = calendar.getTime().toString();

        uricAcid ua = new uricAcid(dateTime, uricAcidVal);
        ua.setBloodSugar(bloodSugarVal);

        SQLiteDatabase db = dBhelper.getWritableDatabase();
        Log.i(TAG, ua.toContentValues().toString() + "--recorded");
        //db.insert("UricAcid", null, ua.toContentValues());

    }
}