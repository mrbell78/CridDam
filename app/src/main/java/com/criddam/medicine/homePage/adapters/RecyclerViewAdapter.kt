package com.criddam.medicine.homePage.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.criddam.medicine.R
import com.criddam.medicine.homePage.adapters.RecyclerViewAdapter.MyHolder
import java.util.*

class RecyclerViewAdapter(
    var mContext: Context?,
    var mValues: ArrayList<String>,
    private var mListener: ItemListener
) : RecyclerView.Adapter<MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.textView.text = mValues[position]
        holder.setData(mValues[position])
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    interface ItemListener {
        fun onItemClick(item: String?)
    }

    inner class MyHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var textView: TextView
        var relativeLayout: RelativeLayout
        var item: String? = null
        fun setData(item: String) {
            this.item = item
            textView.text = item
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                relativeLayout.setBackgroundColor(mContext!!.getColor(R.color.colorBlue))
            }
        }

        override fun onClick(view: View) {
            mListener.onItemClick(item)
        }

        init {
            v.setOnClickListener(this)
            textView = v.findViewById<View>(R.id.textView) as TextView
            relativeLayout =
                v.findViewById<View>(R.id.relativeLayout) as RelativeLayout
        }
    }

}