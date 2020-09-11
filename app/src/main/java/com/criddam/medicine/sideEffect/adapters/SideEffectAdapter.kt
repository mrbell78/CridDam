package com.criddam.medicine.sideEffect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.criddam.medicine.R
import java.util.ArrayList

class SideEffectAdapter(
    var mContext: Context?,
    var mValues: ArrayList<String>,
    var mListener: ItemListener
) : RecyclerView.Adapter<SideEffectAdapter.MyHolder>() {

    interface ItemListener {
        fun onItemClick(item: String?)
    }

    inner class MyHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var tvSeeDetails = view.findViewById<View>(R.id.tv_see_details_side_effect) as TextView
        var item: String? = null
        override fun onClick(view: View) {
            mListener.onItemClick(item)
        }

        init {
            tvSeeDetails.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.content_side_effect, parent, false)
        return MyHolder(view)

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
    }
}