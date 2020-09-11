package com.criddam.medicine.userAllMedicine

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.criddam.medicine.R
import com.criddam.medicine.userAllMedicine.models.Datum
import com.squareup.picasso.Picasso

class MedicineListAdapter(
    private var mContext: Context?,
    private var medicineList: ArrayList<Datum>,
    private var mListener: ItemListener
) : RecyclerView.Adapter<MedicineListAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.content_medicine_item, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return medicineList.size
    }

    interface ItemListener {
        fun onItemClick(item: Datum?)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(medicineList[position])
    }

    inner class MyHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var tvMedicineName = v.findViewById<View>(R.id.tv_medicine_name) as TextView
        private var tvMedicineEdit = v.findViewById<View>(R.id.tv_edit_medicine) as TextView
        private var tvMedicineType = v.findViewById<View>(R.id.tv_medicine_type) as TextView
        private var tvMedicineTime = v.findViewById<View>(R.id.tv_medicine_time_my_medicine) as TextView
        private var tvMedicineMeasurement = v.findViewById<View>(R.id.tv_medicine_measurement) as TextView
        private var ivMedicine = v.findViewById<View>(R.id.iv_medicine_image_my_medicine) as ImageView

        var item: Datum? = null
        fun setData(item: Datum) {
            this.item = item
            tvMedicineName.text = item.medicineName
            tvMedicineType.text = mContext!!.getString(R.string.medicine_type).plus(item.type)
            tvMedicineMeasurement.text = mContext!!.getString(R.string.medicine_measurement).plus(item.measurement)
            tvMedicineTime.text = mContext!!.getString(R.string.medicine_time).plus(item.takingPeriod!![0].tds)
            val circularProgressDrawable = CircularProgressDrawable(mContext!!)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Picasso.get().load(item.photourl).placeholder(circularProgressDrawable).into(ivMedicine)
        }

        override fun onClick(view: View) {
            mListener.onItemClick(item)
        }

        init {
            tvMedicineEdit.setOnClickListener(this)
        }
    }
}