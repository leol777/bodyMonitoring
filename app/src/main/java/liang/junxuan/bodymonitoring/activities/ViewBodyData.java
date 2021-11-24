package liang.junxuan.bodymonitoring.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;

import java.time.Year;

import liang.junxuan.bodymonitoring.R;
import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;
import liang.junxuan.bodymonitoring.fragment.ViewBPGraphFragment;
import liang.junxuan.bodymonitoring.fragment.ViewBPTableFragment;
import liang.junxuan.bodymonitoring.fragment.ViewUAGraphFragment;
import liang.junxuan.bodymonitoring.fragment.ViewUATableFragment;
import liang.junxuan.bodymonitoring.util.DBManager;

public class ViewBodyData extends AppCompatActivity implements View.OnClickListener{
    private BodyMonitordbHelper dbHelper;
    private DBManager manager;

    private ViewUATableFragment uaTableFragmentAll;
    private ViewBPTableFragment bpTableFragmentAll;
    private ViewUAGraphFragment uaGraphFragmentAll;
    private ViewBPGraphFragment bpGraphFragmentAll;

    private ViewUATableFragment uaTableYear;
    private ViewUATableFragment uaTableMonth;
    private ViewUATableFragment uaTableWeek;

    private ViewBPTableFragment bpTableYear;
    private ViewBPTableFragment bpTableMonth;
    private ViewBPTableFragment bpTableWeek;

    private ViewUAGraphFragment uaGraphYear;
    private ViewUAGraphFragment uaGraphMonth;
    private ViewUAGraphFragment uaGraphWeek;

    private ViewBPGraphFragment bpGraphYear;
    private ViewBPGraphFragment bpGraphMonth;
    private ViewBPGraphFragment bpGraphWeek;


    private RadioButton rb_table;
    private RadioButton rb_graph;
    private RadioButton rb_bp;
    private RadioButton rb_ua;

    private Button time_period_hint_button;

    private PopupMenu popupMenu;

    private DBManager.Time_Interval time_interval = DBManager.Time_Interval.ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_body_data);

        time_period_hint_button = findViewById(R.id.view_data_time_period_hint_button);

        popupMenu = new PopupMenu(ViewBodyData.this, time_period_hint_button);
        popupMenu.inflate(R.menu.time_period_menu);

        time_period_hint_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int bp_table = 1;
                int bp_graph = 2;
                int ua_table = 3;
                int ua_graph = 4;
                int status = 0;

                if (rb_bp.isChecked() && rb_table.isChecked()){
                    status = bp_table;
                }else if (rb_bp.isChecked() && rb_graph.isChecked()){
                    status = bp_graph;
                }else if (rb_ua.isChecked() && rb_table.isChecked()){
                    status = ua_table;
                }else if (rb_ua.isChecked() && rb_graph.isChecked()){
                    status = ua_graph;
                }

                switch (item.getItemId()){
                    case R.id.view_time_period_all:
                        time_period_hint_button.setText(R.string.all);
                        time_interval = DBManager.Time_Interval.ALL;
                        item.setChecked(true);
                        if (status == bp_graph){
                            replaceFragment(bpGraphFragmentAll);
                        }else if (status == bp_table){
                            replaceFragment(bpTableFragmentAll);
                        }else if (status == ua_graph){
                            replaceFragment(uaGraphFragmentAll);
                        }else if (status == ua_table){
                            replaceFragment(uaTableFragmentAll);
                        }
                        break;
                    case R.id.view_time_period_past_year:
                        time_period_hint_button.setText(R.string.past_year);
                        time_interval = DBManager.Time_Interval.YEAR;
                        item.setChecked(true);
                        if (status == bp_graph){
                            replaceFragment(bpGraphYear);
                        }else if (status == bp_table){
                            replaceFragment(bpTableYear);
                        }else if (status == ua_graph){
                            replaceFragment(uaGraphYear);
                        }else if (status == ua_table){
                            replaceFragment(uaTableYear);
                        }
                        break;
                    case R.id.view_time_period_past_month:
                        time_period_hint_button.setText(R.string.past_month);
                        time_interval = DBManager.Time_Interval.MONTH;
                        item.setChecked(true);
                        if (status == bp_graph){
                            replaceFragment(bpGraphMonth);
                        }else if (status == bp_table){
                            replaceFragment(bpTableMonth);
                        }else if (status == ua_graph){
                            replaceFragment(uaGraphMonth);
                        }else if (status == ua_table){
                            replaceFragment(uaTableMonth);
                        }
                        break;
                    case R.id.view_time_period_past_week:
                        time_period_hint_button.setText(R.string.past_week);
                        time_interval = DBManager.Time_Interval.WEEK;
                        item.setChecked(true);
                        if (status == bp_graph){
                            replaceFragment(bpGraphWeek);
                        }else if (status == bp_table){
                            replaceFragment(bpTableWeek);
                        }else if (status == ua_graph){
                            replaceFragment(uaGraphWeek);
                        }else if (status == ua_table){
                            replaceFragment(uaTableWeek);
                        }
                        break;
                }
                return false;
            }
        });


        dbHelper = new BodyMonitordbHelper(ViewBodyData.this, "BodyMonitoring.db", null, 1);
        manager = new DBManager(dbHelper);

        bindViews();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.view_historical_body_data);
        actionBar.setDisplayHomeAsUpEnabled(true);

        MenuInflater inflater = new MenuInflater(ViewBodyData.this);
        inflater.inflate(R.menu.view_data_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

        }
        return true;
    }

    private void initData(){
        uaTableFragmentAll = new ViewUATableFragment(manager.findAllUA(), DBManager.Time_Interval.ALL);
        bpTableFragmentAll = new ViewBPTableFragment(manager.findAllBP(), DBManager.Time_Interval.ALL);
        uaGraphFragmentAll = new ViewUAGraphFragment(manager.findAllUA());
        bpGraphFragmentAll = new ViewBPGraphFragment(manager.findAllBP());

        uaTableYear = new ViewUATableFragment(manager.findUAbyTime(DBManager.Time_Interval.YEAR), DBManager.Time_Interval.YEAR);
        uaTableMonth = new ViewUATableFragment(manager.findUAbyTime(DBManager.Time_Interval.MONTH), DBManager.Time_Interval.MONTH);
        uaTableWeek = new ViewUATableFragment(manager.findUAbyTime(DBManager.Time_Interval.WEEK), DBManager.Time_Interval.WEEK);

        uaGraphYear = new ViewUAGraphFragment(manager.findUAbyTime(DBManager.Time_Interval.YEAR));
        uaGraphMonth = new ViewUAGraphFragment(manager.findUAbyTime(DBManager.Time_Interval.MONTH));
        uaGraphWeek = new ViewUAGraphFragment(manager.findUAbyTime(DBManager.Time_Interval.WEEK));

        bpTableYear = new ViewBPTableFragment(manager.findBPbyTime(DBManager.Time_Interval.YEAR), DBManager.Time_Interval.YEAR);
        bpTableMonth = new ViewBPTableFragment(manager.findBPbyTime(DBManager.Time_Interval.MONTH), DBManager.Time_Interval.MONTH);
        bpTableWeek = new ViewBPTableFragment(manager.findBPbyTime(DBManager.Time_Interval.WEEK), DBManager.Time_Interval.WEEK);

        bpGraphYear = new ViewBPGraphFragment(manager.findBPbyTime(DBManager.Time_Interval.YEAR));
        bpGraphMonth = new ViewBPGraphFragment(manager.findBPbyTime(DBManager.Time_Interval.MONTH));
        bpGraphWeek = new ViewBPGraphFragment(manager.findBPbyTime(DBManager.Time_Interval.WEEK));




        getSupportFragmentManager().beginTransaction().replace(R.id.view_data_fl_container
                ,bpTableFragmentAll).commit();
    }

    private void bindViews(){
        rb_table = findViewById(R.id.view_table_radio_button);
        rb_graph = findViewById(R.id.view_graph_radio_button);

        rb_bp = findViewById(R.id.view_bp_radio_button);
        rb_ua = findViewById(R.id.view_ua_radio_button);

        rb_table.setChecked(true);
        rb_bp.setChecked(true);

        rb_table.setOnClickListener(this);
        rb_graph.setOnClickListener(this);

        rb_bp.setOnClickListener(this);
        rb_ua.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.view_bp_radio_button){
            if (rb_table.isChecked()){
                switch (time_interval){
                    case ALL:
                        replaceFragment(bpTableFragmentAll);
                    case YEAR:
                        replaceFragment(bpTableYear);
                    case MONTH:
                        replaceFragment(bpTableMonth);
                    case WEEK:
                        replaceFragment(bpTableWeek);
                }
            }else if (rb_graph.isChecked()){
                switch (time_interval){
                    case ALL:
                        replaceFragment(bpGraphFragmentAll);
                    case YEAR:
                        replaceFragment(bpGraphYear);
                    case MONTH:
                        replaceFragment(bpGraphMonth);
                    case WEEK:
                        replaceFragment(bpGraphWeek);
                }
            }
        }else if (v.getId() == R.id.view_ua_radio_button){
            if (rb_table.isChecked()){
                switch (time_interval){
                    case ALL:
                        replaceFragment(uaTableFragmentAll);
                    case YEAR:
                        replaceFragment(uaTableYear);
                    case MONTH:
                        replaceFragment(uaTableMonth);
                    case WEEK:
                        replaceFragment(uaTableWeek);
                }
            }else if (rb_graph.isChecked()){
                switch (time_interval){
                    case ALL:
                        replaceFragment(uaGraphFragmentAll);
                    case YEAR:
                        replaceFragment(uaGraphYear);
                    case MONTH:
                        replaceFragment(uaGraphMonth);
                    case WEEK:
                        replaceFragment(uaGraphWeek);
                }
            }
        }else if (v.getId() == R.id.view_graph_radio_button){
            if (rb_bp.isChecked()){
                switch (time_interval){
                    case ALL:
                        replaceFragment(bpGraphFragmentAll);
                    case YEAR:
                        replaceFragment(bpGraphYear);
                    case MONTH:
                        replaceFragment(bpGraphMonth);
                    case WEEK:
                        replaceFragment(bpGraphWeek);
                }
            }else if (rb_ua.isChecked()){
                switch (time_interval){
                    case ALL:
                        replaceFragment(uaGraphFragmentAll);
                    case YEAR:
                        replaceFragment(uaGraphYear);
                    case MONTH:
                        replaceFragment(uaGraphMonth);
                    case WEEK:
                        replaceFragment(uaGraphWeek);
                }
            }
        }else if (v.getId() == R.id.view_table_radio_button){
            if (rb_bp.isChecked()){
                switch (time_interval){
                    case ALL:
                        replaceFragment(bpTableFragmentAll);
                    case YEAR:
                        replaceFragment(bpTableYear);
                    case MONTH:
                        replaceFragment(bpTableMonth);
                    case WEEK:
                        replaceFragment(bpTableWeek);
                }
            }else if (rb_ua.isChecked()){
                switch (time_interval){
                    case ALL:
                        replaceFragment(uaTableFragmentAll);
                    case YEAR:
                        replaceFragment(uaTableYear);
                    case MONTH:
                        replaceFragment(uaTableMonth);
                    case WEEK:
                        replaceFragment(uaTableWeek);
                }
            }
        }
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.view_data_fl_container
            ,fragment).commit();
    }


}