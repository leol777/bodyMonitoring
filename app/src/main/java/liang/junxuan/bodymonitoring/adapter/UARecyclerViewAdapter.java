package liang.junxuan.bodymonitoring.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import liang.junxuan.bodymonitoring.activities.EditUricAcid;
import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;
import liang.junxuan.bodymonitoring.item.UricAcid;
import liang.junxuan.bodymonitoring.util.DBManager;

public class UARecyclerViewAdapter extends RecyclerView.Adapter<UARecyclerViewAdapter.uaRecyclerViewHolder> {
    private ArrayList<UricAcid> list;
    private Context context;

    public UARecyclerViewAdapter(ArrayList<UricAcid> l){
        list = l;
    }

    @NonNull
    @Override
    public uaRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.uric_acid_item,parent,false);
        return new uaRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull uaRecyclerViewHolder holder, final int position) {
        final UricAcid ua_item = list.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = sdf.parse(ua_item.getDate_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat out_sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINA);
        assert date != null;
        String out_date = out_sdf.format(date);

        String out_ua = context.getString(R.string.uric_acid_val)+":"+ua_item.getUricAcid()
                +context.getString(R.string.uric_acid_unit);

        String out_bs = context.getString(R.string.blood_sugar_val) +":"+ua_item.getBloodSugar()
                +context.getString(R.string.blood_sugar_unit);

        holder.dateTiemView.setText(out_date);
        holder.uricAcidView.setText(out_ua);

        if (out_bs.equals(String.valueOf(-1))){
            holder.bloodSugarView.setText("无录入");
        }else {
            holder.bloodSugarView.setText(out_bs);
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteDialog(ua_item, position);
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmEditDialog(ua_item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class uaRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView dateTiemView;
        TextView uricAcidView;
        TextView bloodSugarView;

        Button deleteButton;
        Button editButton;

        public uaRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTiemView = itemView.findViewById(R.id.uric_acid_item_date_time);
            uricAcidView = itemView.findViewById(R.id.uric_acid_item_uric_acid);
            bloodSugarView = itemView.findViewById(R.id.uric_acidd_item_blood_sugar);

            deleteButton = itemView.findViewById(R.id.delete_ua_button);
            editButton = itemView.findViewById(R.id.edit_ua_button);
        }
    }

    private void confirmDeleteDialog(final UricAcid item, final int position){
        AlertDialog.Builder cd = new AlertDialog.Builder(context);

        cd.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    deleteUricAcid(item);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "删除尿酸记录失败", Toast.LENGTH_SHORT).show();
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

        cd.setMessage("确认删除该条尿酸记录吗？");
        cd.setTitle("提示");
        cd.show();
    }

    private void confirmEditDialog(final UricAcid item, int position){
        AlertDialog.Builder cd = new AlertDialog.Builder(context);

        cd.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, EditUricAcid.class);
                intent.putExtra("ua_id", item.getId());
                context.startActivity(intent);
            }
        });

        cd.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        cd.setTitle("提示");
        cd.setMessage("确定修改该尿酸吗？");
        cd.show();
    }

    private void deleteUricAcid(UricAcid item) throws Exception {
        BodyMonitordbHelper dBhelper = new BodyMonitordbHelper(context, "BodyMonitoring.db",null,1);
        DBManager dbManager = new DBManager(dBhelper);
        dbManager.deleteSingleUA(item);

    }
}
