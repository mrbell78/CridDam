package com.criddam.medicine.rubelportion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.criddam.medicine.R;
import com.criddam.medicine.homePage.adapters.RecyclerViewAdapter;

import java.util.List;

public class Customadapter extends RecyclerView.Adapter<Customadapter.Customclass> {


    Context context;
    List<Helthdata>datalist;

    public Customadapter(Context context, List<Helthdata> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public Customclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.itemforhelth,parent,false);

        return new Customclass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Customclass holder, int position) {

        holder.tv_temparature.setText(datalist.get(position).getTempareture()+"");
        holder.tv_weight.setText(datalist.get(position).getWeight()+"");
        holder.tv_pulse.setText(datalist.get(position).getPulse()+"");
        holder.tv_systolic.setText(datalist.get(position).getSystolic()+"");
        holder.tv_diastolic.setText(datalist.get(position).getDiastolic()+"");
        holder.tv_date.setText(datalist.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class Customclass extends RecyclerView.ViewHolder {

        TextView tv_temparature,tv_weight,tv_pulse,tv_systolic,tv_diastolic,tv_date;
        public Customclass(@NonNull View itemView) {
            super(itemView);

            tv_temparature=itemView.findViewById(R.id.temperatureid);
            tv_weight=itemView.findViewById(R.id.weightid);
            tv_pulse=itemView.findViewById(R.id.pulseid);
            tv_systolic=itemView.findViewById(R.id.systolicid);
            tv_diastolic=itemView.findViewById(R.id.diastolicid);
            tv_date=itemView.findViewById(R.id.dateid);
        }
    }
}
