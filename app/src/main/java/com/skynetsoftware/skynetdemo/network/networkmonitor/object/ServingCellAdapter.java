package com.skynetsoftware.skynetdemo.network.networkmonitor.object;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skynetsoftware.skynetdemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ServingCellAdapter extends ArrayAdapter<ServingCell> {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private ArrayList<ServingCell> lServingCell;

    public static class ViewHolder {
        public TextView ci;
        public TextView lac;
        public TextView level;
        public TextView mode;
        public TextView node;
        public TextView qual;
        public TextView srvTime;
        public TextView time;
    }

    public ServingCellAdapter(Activity activity, int textViewResourceId, ArrayList<ServingCell> lServingCell) {
        super(activity, textViewResourceId, lServingCell);
        try {
            this.activity = activity;
            this.lServingCell = lServingCell;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return this.lServingCell.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View vi = convertView;

        if (vi == null) {
            try {
                vi = inflater.inflate(R.layout.list_serving_cell, null);
                holder = new ViewHolder();
                holder.time = (TextView) vi.findViewById(R.id.txtAdpTime);
                holder.lac = (TextView) vi.findViewById(R.id.txtAdpLac);
                holder.node = (TextView) vi.findViewById(R.id.txtAdpNode);
                holder.ci = (TextView) vi.findViewById(R.id.txtAdpCellID);
                holder.level = (TextView) vi.findViewById(R.id.txtAdpLevel);
                holder.qual = (TextView) vi.findViewById(R.id.txtAdpQual);
                holder.mode = (TextView) vi.findViewById(R.id.txtAdpMode);
                holder.srvTime = (TextView) vi.findViewById(R.id.txtAdpSrvTime);
                vi.setTag(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        holder.time.setText(new SimpleDateFormat("hh:mm:ss").format(((ServingCell) this.lServingCell.get(position)).getTime()));
        holder.lac.setText(((ServingCell) this.lServingCell.get(position)).getLac());
        holder.node.setText(((ServingCell) this.lServingCell.get(position)).getNode());
        holder.ci.setText(((ServingCell) this.lServingCell.get(position)).getCellId());
        holder.level.setText(((ServingCell) this.lServingCell.get(position)).getLevel());
        holder.qual.setText(((ServingCell) this.lServingCell.get(position)).getQual());
        holder.mode.setText(((ServingCell) this.lServingCell.get(position)).getMode());
        holder.srvTime.setText(String.valueOf(((ServingCell) this.lServingCell.get(position)).getServingTime()));
        return vi;
    }
}
