package liang.junxuan.bodymonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.UriCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import liang.junxuan.bodymonitoring.fragment.ViewBPGraphFragment;
import liang.junxuan.bodymonitoring.fragment.ViewBPTableFragment;
import liang.junxuan.bodymonitoring.fragment.ViewUAGraphFragment;
import liang.junxuan.bodymonitoring.fragment.ViewUATableFragment;
import liang.junxuan.bodymonitoring.item.*;
import liang.junxuan.bodymonitoring.dataBase.bodyMonitordbHelper;
import liang.junxuan.bodymonitoring.adapter.*;

public class ViewBodyData extends AppCompatActivity implements View.OnClickListener{

    private ViewUATableFragment uaTableFragment;
    private ViewBPTableFragment bpTableFragment;
    private ViewUAGraphFragment uaGraphFragment;
    private ViewBPGraphFragment bpGraphFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_body_data);

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
        uaTableFragment = new ViewUATableFragment();
        bpTableFragment = new ViewBPTableFragment();
        uaGraphFragment = new ViewUAGraphFragment();
        bpGraphFragment = new ViewBPGraphFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.view_data_fl_container
                ,bpTableFragment).commit();
    }

    private void bindViews(){
        RadioButton rb_bp = findViewById(R.id.view_bp_radio_button);
        RadioButton rb_ua = findViewById(R.id.view_ua_radio_button);

        rb_bp.setChecked(true);

        rb_bp.setOnClickListener(this);
        rb_ua.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.view_bp_radio_button){
            getSupportFragmentManager().beginTransaction().replace(R.id.view_data_fl_container
                ,bpGraphFragment).commit();
        }else if (v.getId() == R.id.view_ua_radio_button){
            getSupportFragmentManager().beginTransaction().replace(R.id.view_data_fl_container
                ,uaGraphFragment).commit();
        }
    }


}