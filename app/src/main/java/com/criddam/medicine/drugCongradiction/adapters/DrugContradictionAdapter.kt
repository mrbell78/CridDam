package com.criddam.medicine.drugCongradiction.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.criddam.medicine.R
import java.util.ArrayList

class DrugContradictionAdapter(
    var mContext: Context?,
    var mValues: ArrayList<String>
) : RecyclerView.Adapter<DrugContradictionAdapter.MyHolder>() {


    interface ItemListener {
        fun onItemClick(item: String?)
    }

    inner class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvMedicineName = view.findViewById<View>(R.id.tv_medicine_name) as TextView
        var tvContradiction = view.findViewById<View>(R.id.tv_medicine_type) as TextView
        var item: String? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.content_medicine_item, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.tvMedicineName.text = "Renidine"
        holder.tvContradiction.text = "Contradiction details of Renidine Tablet here"
    }
}