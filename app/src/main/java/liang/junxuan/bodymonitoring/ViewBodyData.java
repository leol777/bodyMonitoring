package liang.junxuan.bodymonitoring;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import liang.junxuan.bodymonitoring.fragment.ViewBPGraphFragment;
import liang.junxuan.bodymonitoring.fragment.ViewBPTableFragment;
import liang.junxuan.bodymonitoring.fragment.ViewUAGraphFragment;
import liang.junxuan.bodymonitoring.fragment.ViewUATableFragment;

public class ViewBodyData extends AppCompatActivity implements View.OnClickListener{

    private ViewUATableFragment uaTableFragment;
    private ViewBPTableFragment bpTableFragment;
    private ViewUAGraphFragment uaGraphFragment;
    private ViewBPGraphFragment bpGraphFragment;

    private RadioButton rb_table;
    private RadioButton rb_graph;
    private RadioButton rb_bp;
    private RadioButton rb_ua;

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
                getSupportFragmentManager().beginTransaction().replace(R.id.view_data_fl_container
                        ,bpTableFragment).commit();
            }else if (rb_graph.isChecked()){
                getSupportFragmentManager().beginTransaction().replace(R.id.view_data_fl_container
                        ,bpGraphFragment).commit();
            }
        }else if (v.getId() == R.id.view_ua_radio_button){
            if (rb_table.isChecked()){
                getSupportFragmentManager().beginTransaction().replace(R.id.view_data_fl_container
                        ,uaTableFragment).commit();
            }else if (rb_graph.isChecked()){
                getSupportFragmentManager().beginTransaction().replace(R.id.view_data_fl_container
                        ,uaGraphFragment).commit();
            }
        }else if (v.getId() == R.id.view_graph_radio_button){
            if (rb_bp.isChecked()){
                getSupportFragmentManager().beginTransaction().replace(R.id.view_data_fl_container
                        ,bpGraphFragment).commit();
            }else if (rb_ua.isChecked()){
                getSupportFragmentManager().beginTransaction().replace(R.id.view_data_fl_container
                        ,uaGraphFragment).commit();
            }
        }else if (v.getId() == R.id.view_table_radio_button){
            if (rb_bp.isChecked()){
                getSupportFragmentManager().beginTransaction().replace(R.id.view_data_fl_container
                        ,bpTableFragment).commit();
            }else if (rb_ua.isChecked()){
                getSupportFragmentManager().beginTransaction().replace(R.id.view_data_fl_container
                        ,uaTableFragment).commit();
            }
        }
    }


}