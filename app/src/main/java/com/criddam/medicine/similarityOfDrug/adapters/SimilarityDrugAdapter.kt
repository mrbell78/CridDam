package com.criddam.medicine.similarityOfDrug.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.criddam.medicine.R
import java.util.ArrayList

class SimilarityDrugAdapter(
    var mContext: Context?,
    var mValues: ArrayList<String>) : RecyclerView.Adapter<SimilarityDrugAdapter.MyHolder>() {

    inner class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        var item: String? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.content_similarity_drug, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
    }
}