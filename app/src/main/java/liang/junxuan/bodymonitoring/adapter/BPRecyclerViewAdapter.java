package liang.junxuan.bodymonitoring.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


import liang.junxuan.bodymonitoring.R;
import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;
import liang.junxuan.bodymonitoring.item.BloodPressure;
import liang.junxuan.bodymonitoring.util.DBManager;

public class BPRecyclerViewAdapter extends RecyclerView.Adapter<BPRecyclerViewAdapter.bpRecyclerViewHolder> {
    private ArrayList<BloodPressure> list;
    private Context context;

    static class bpRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView dateTimeView;
        TextView upperBPView;
        TextView lowerBPView;
        TextView heartBeatView;

        Button deleteButton;
        Button editButton;

        public bpRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTimeView = itemView.findViewById(R.id.blood_pressure_item_date_time);
            upperBPView = itemView.findViewById(R.id.blood_pressure_item_upper_pressure);
            lowerBPView = itemView.findViewById(R.id.blood_pressure_item_lower_pressure);
            heartBeatView = itemView.findViewById(R.id.blood_pressure_item_heart_beat);

            deleteButton = itemView.findViewById(R.id.delete_bp_button);
            editButton = itemView.findViewById(R.id.edit_bp_button);
        }
    }

    public BPRecyclerViewAdapter(ArrayList<BloodPressure> l){
        list = l;
    }



    @NonNull
    @Override
    public BPRecyclerViewAdapter.bpRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.blood_pressure_item, parent, false);
        return new bpRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BPRecyclerViewAdapter.bpRecyclerViewHolder holder, final int position) {
        final BloodPressure bp_item = list.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = sdf.parse(bp_item.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat out_sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINA);
        assert date != null;
        String out_date = out_sdf.format(date);

        String out_lbp = context.getString(R.string.blood_upper_pressure_val)+":"+bp_item.getUpperPressure()
                +context.getString(R.string.blood_pressure_unit);

        String out_hbp = context.getString(R.string.blood_lower_pressure_val)+":"+bp_item.getLowerPressure()
                +context.getString(R.string.blood_pressure_unit);

        String out_hb = context.getString(R.string.hear_beat_val)+":"+bp_item.getHeartBeat()
                +context.getString(R.string.heart_beat_unit);

        holder.dateTimeView.setText(out_date);
        holder.lowerBPView.setText(out_lbp);
        holder.upperBPView.setText(out_hbp);

        if (out_hb.equals(String.valueOf(-1))){
            holder.heartBeatView.setText("无录入");
        }else {
            holder.heartBeatView.setText(out_hb);
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteDialog(bp_item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void confirmDeleteDialog(final BloodPressure item, final int position){
        AlertDialog.Builder cd = new AlertDialog.Builder(context);

        cd.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    deleteBloodPressure(item);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "删除血压记录失败", Toast.LENGTH_SHORT).show();
                }
                list.remove(position);
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        cd.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        cd.setMessage("确认删除该条血压记录吗？");
        cd.setTitle("提示");
        cd.show();
    }

    private void deleteBloodPressure(BloodPressure item) throws Exception {
        BodyMonitordbHelper dBhelper = new BodyMonitordbHelper(context, "BodyMonitoring.db",null,1);
        DBManager dbManager = new DBManager(dBhelper);
        dbManager.deleteSingleBP(item);
    }
}
