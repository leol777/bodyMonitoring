package liang.junxuan.bodymonitoring.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import liang.junxuan.bodymonitoring.R;
import liang.junxuan.bodymonitoring.item.uricAcid;

public class uaRecyclerViewAdapter extends RecyclerView.Adapter<uaRecyclerViewAdapter.uaRecyclerViewHolder> {
    private ArrayList<uricAcid> list;
    private Context context;

    public uaRecyclerViewAdapter(ArrayList<uricAcid> l){
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
    public void onBindViewHolder(@NonNull uaRecyclerViewHolder holder, int position) {
        uricAcid ua_item = list.get(position);
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
        holder.bloodSugarView.setText(out_bs);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class uaRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView dateTiemView;
        TextView uricAcidView;
        TextView bloodSugarView;

        public uaRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTiemView = itemView.findViewById(R.id.uric_acid_item_date_time);
            uricAcidView = itemView.findViewById(R.id.uric_acid_item_uric_acid);
            bloodSugarView = itemView.findViewById(R.id.uric_acidd_item_blood_sugar);
        }
    }
}
