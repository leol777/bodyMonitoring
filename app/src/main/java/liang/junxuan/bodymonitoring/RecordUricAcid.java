package liang.junxuan.bodymonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import liang.junxuan.bodymonitoring.dataBase.bodyMonitordbHelper;
import liang.junxuan.bodymonitoring.item.uricAcid;

public class RecordUricAcid extends AppCompatActivity {
    private final static String TAG = "Record Uric Acid";

    private EditText uricAcidInput;
    private EditText bloodSugarInput;

    private  bodyMonitordbHelper dBhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_uric_acid);

        uricAcidInput = findViewById(R.id.uric_acid_input);
        bloodSugarInput = findViewById(R.id.blood_sugar_input);

        dBhelper = new bodyMonitordbHelper(this, "BodyMonitoring.db",null,1);

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
        AlertDialog.Builder cd = new AlertDialog.Builder(RecordUricAcid.this);

        cd.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitUricAcid();
                dialog.dismiss();
                RecordUricAcid.this.finish();
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

        cd.setTitle("请确认您输入的血液信息是否准确？");
        cd.show();
    }

    private void submitUricAcid(){
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

        Calendar calendar = Calendar.getInstance();
        String dateTime = calendar.getTime().toString();

        uricAcid ua = new uricAcid(dateTime, uricAcidVal);
        ua.setBloodSugar(bloodSugarVal);

        SQLiteDatabase db = dBhelper.getWritableDatabase();
        long result = db.insert("UricAcid", null, ua.toContentValues());
        if (result == -1){
            Log.i(TAG, "Insertion denied");
        }else {
            Log.i(TAG, ua.toContentValues().toString() + "--recorded");
        }
        db.close();
    }
}