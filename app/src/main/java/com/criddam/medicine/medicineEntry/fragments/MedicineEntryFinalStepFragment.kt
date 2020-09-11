package com.criddam.medicine.medicineEntry.fragments

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.criddam.medicine.R
import com.criddam.medicine.database.OfflineInformation
import com.criddam.medicine.medicineEntry.MedicineEntryViewModel.MedicineEntryViewModel
import com.criddam.medicine.medicineEntry.models.TakingPeriods
import com.criddam.medicine.others.FinalAlarmWorker
import com.criddam.medicine.others.MedicineTime
import com.criddam.medicine.others.Utility
import com.criddam.medicine.userAllMedicine.AllMedicineListFragment
import kotlinx.android.synthetic.main.fragment_medicine_entry_final_step.*
import org.greenrobot.eventbus.Subscribe
import java.util.*
import kotlin.collections.ArrayList

class MedicineEntryFinalStepFragment : Fragment(), MedicineTime {
    private val MEDICINE_TYPE = "medicine_type"
    private val MEDICINE_NAME = "medicine_name"
    private val MEDICINE_IMAGE = "medicine_image"
    private val MEDICINE_MEASUREMENT_NAME = "medicine_measurement_name"
    private val MEDICINE_MEASUREMENT_TYPE = "medicine_measurement_type"
    private val medicinetimeconstant= "medicinetime"
    private var medicineMeasurementName: String? = null
    private var medicineMeasurementType: String? = null
    private var medicineType: String? = null
    private var medicineName: String? = null
    private var medicineImage: String? = null
    private var selectingTimeFor: String = ""
    private var qrcodevalue:String =" "
    private var medicnetime:String?=null
    private lateinit var takingPeriodList: ArrayList<TakingPeriods>
    private lateinit var viewModelProvider: MedicineEntryViewModel
    private lateinit var offlineInformation: OfflineInformation
    private lateinit var calSet: Calendar
    private lateinit var mornCal: Calendar
    private lateinit var evenCal: Calendar
    private lateinit var nightCal: Calendar
    private lateinit var otherCal: Calendar
    private lateinit var hourList: java.util.ArrayList<Int>
    private lateinit var minuteList: java.util.ArrayList<Int>
    private lateinit var alarmList: java.util.ArrayList<Calendar>

    var pref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            medicineType = it.getString(MEDICINE_TYPE)
            medicineName = it.getString(MEDICINE_NAME)
            medicineImage = it.getString(MEDICINE_IMAGE)
            medicineMeasurementName = it.getString(MEDICINE_MEASUREMENT_NAME)
            medicineMeasurementType = it.getString(MEDICINE_MEASUREMENT_TYPE)
            medicnetime=it.getString(medicinetimeconstant)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_medicine_entry_final_step, container, false)
        pref = context?.getSharedPreferences("MyPref", 0)
        qrcodevalue= pref?.getString("qrkeyfinal",null).toString()
        action(root)
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(
            medicineType: String,
            medicineName: String,
            medicineImage: String,
            medicineMeasurementName: String,
            medicinetimelocal:String
        ) =
            MedicineEntryFinalStepFragment().apply {
                arguments = Bundle().apply {
                    putString(MEDICINE_TYPE, medicineType)
                    putString(MEDICINE_NAME, medicineName)
                    putString(MEDICINE_IMAGE, medicineImage)
                    putString(MEDICINE_MEASUREMENT_NAME, medicineMeasurementName)
                    putString(medicinetimeconstant,medicinetimelocal)
                }
            }
    }

    private fun action(root: View) {
        offlineInformation = OfflineInformation(this.context!!)
        takingPeriodList = ArrayList()
        hourList = ArrayList()
        minuteList = ArrayList()
        alarmList = ArrayList()
        calSet = Calendar.getInstance()
        mornCal = Calendar.getInstance()
        evenCal = Calendar.getInstance()
        nightCal = Calendar.getInstance()
        otherCal = Calendar.getInstance()
        viewModelProvider =
            androidx.lifecycle.ViewModelProviders.of(this).get(MedicineEntryViewModel::class.java)

        val cbMorning: CheckBox = root.findViewById(R.id.cb_morning)
        val cbAfternoon: CheckBox = root.findViewById(R.id.cb_afternoon)
        val cbNight: CheckBox = root.findViewById(R.id.cb_night)
        val cbOther: CheckBox = root.findViewById(R.id.cb_other)

        val tvTimeMorning: TextView = root.findViewById(R.id.tv_select_time_morning)
        val tvTimeAfternoon: TextView = root.findViewById(R.id.tv_select_time_afternoon)
        val tvTimeNight: TextView = root.findViewById(R.id.tv_select_time_night)
        val tvTimeOther: TextView = root.findViewById(R.id.tv_select_time_other)

        tvTimeMorning.setOnClickListener {
            selectingTimeFor = "morning"
            openTimePickerDialog(true)
        }

        tvTimeAfternoon.setOnClickListener {
            selectingTimeFor = "afternoon"
            openTimePickerDialog(true)
        }

        tvTimeNight.setOnClickListener {
            selectingTimeFor = "night"
            openTimePickerDialog(true)
        }

        tvTimeOther.setOnClickListener {
            selectingTimeFor = "other"
            openTimePickerDialog(true)
        }

        val btnSubmitMedicine: Button = root.findViewById(R.id.btn_medicine_entry_submit)
        btnSubmitMedicine.setOnClickListener {
            if (cbMorning.isChecked || cbAfternoon.isChecked
                || cbNight.isChecked || cbOther.isChecked
            ) {
                if (cbMorning.isChecked && tvTimeMorning.text == getString(R.string.select_time)) {
                    Toast.makeText(context, getString(R.string.select_morning), Toast.LENGTH_LONG)
                        .show()
                } else if (cbAfternoon.isChecked && tvTimeAfternoon.text == getString(R.string.select_time)) {
                    Toast.makeText(context, getString(R.string.select_afternoon), Toast.LENGTH_LONG)
                        .show()
                } else if (cbNight.isChecked && tvTimeNight.text == getString(R.string.select_time)) {
                    Toast.makeText(context, getString(R.string.select_night), Toast.LENGTH_LONG)
                        .show()
                } else if (cbOther.isChecked && tvTimeOther.text == getString(R.string.select_time)) {
                    Toast.makeText(context, getString(R.string.select_other), Toast.LENGTH_LONG)
                        .show()
                } else {
                    if (Utility.verifyAvailableNetwork(activity as AppCompatActivity)) {

                        Toast.makeText(context,"medicine time "+ medicnetime,Toast.LENGTH_SHORT)
                        viewModelProvider.submitMedicine(
                            this.context!!,
                            medicineType.toString(), medicineName.toString(),
                            medicineMeasurementType.toString().plus(" ").plus(medicineMeasurementName.toString()),
                            takingPeriodList,
                            offlineInformation.userId!!,
                            medicineImage!!,
                            qrcodevalue
                        )
                    } else Toast.makeText(
                        context,
                        getString(R.string.no_internet_connection),
                        Toast.LENGTH_LONG
                    ).show()

                }
            } else {
                Toast.makeText(context, getString(R.string.select_time), Toast.LENGTH_LONG)
                    .show()
            }
        }

        viewModelProvider.isSuccess.observe(
            MedicineEntryFinalStepFragment@ this,
            androidx.lifecycle.Observer {
                if (it) {
                    setAllAlarm()
                    Toast.makeText(
                        context,
                        getString(R.string.medicine_entry_successful),
                        Toast.LENGTH_LONG
                    ).show()
                    for (entry in 0 until fragmentManager!!.backStackEntryCount) {
                        fragmentManager!!.popBackStack()
                    }
                    Utility.replaceFragmentsOverHomeActivity(
                        fragmentManager,
                        AllMedicineListFragment.newInstance()
                    )
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.medicine_entry_failed),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

    }

    private fun openTimePickerDialog(is24r: Boolean) {
        val calendar: Calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            context,AlertDialog.THEME_HOLO_LIGHT,
            onTimeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            is24r
        )
        timePickerDialog.setTitle("Set Alarm Time")
        timePickerDialog.show()
    }

    private var onTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calSet[Calendar.HOUR_OF_DAY] = hourOfDay
            calSet[Calendar.MINUTE] = minute
            calSet[Calendar.SECOND] = 0
            calSet[Calendar.MILLISECOND] = 0
            if (Calendar.getInstance().after(calSet)) {
                calSet.add(Calendar.DAY_OF_MONTH, 1)
            }
            when (selectingTimeFor) {
                "morning" -> {
                    tv_select_time_morning.text =
                        hourOfDay.toString().plus(":").plus(minute.toString())
                    cb_morning.isChecked = true
                    mornCal[Calendar.HOUR_OF_DAY] = hourOfDay
                    mornCal[Calendar.MINUTE] = minute
                    mornCal[Calendar.SECOND] = 0
                    mornCal[Calendar.MILLISECOND] = 0
                }
                "afternoon" -> {
                    tv_select_time_afternoon.text =
                        hourOfDay.toString().plus(":").plus(minute.toString())
                    cb_afternoon.isChecked = true
                    evenCal[Calendar.HOUR_OF_DAY] = hourOfDay
                    evenCal[Calendar.MINUTE] = minute
                    evenCal[Calendar.SECOND] = 0
                    evenCal[Calendar.MILLISECOND] = 0
                }
                "night" -> {
                    tv_select_time_night.text =
                        hourOfDay.toString().plus(":").plus(minute.toString())
                    cb_night.isChecked = true
                    nightCal[Calendar.HOUR_OF_DAY] = hourOfDay
                    nightCal[Calendar.MINUTE] = minute
                    nightCal[Calendar.SECOND] = 0
                    nightCal[Calendar.MILLISECOND] = 0
                }
                "other" -> {
                    tv_select_time_other.text =
                        hourOfDay.toString().plus(":").plus(minute.toString())
                    cb_other.isChecked = true
                    otherCal[Calendar.HOUR_OF_DAY] = hourOfDay
                    otherCal[Calendar.MINUTE] = minute
                    otherCal[Calendar.SECOND] = 0
                    otherCal[Calendar.MILLISECOND] = 0
                }
            }

            takingPeriodList.add(
                TakingPeriods(
                    selectingTimeFor,
                    hourOfDay.toString().plus(":").plus(minute.toString())
                )
            )
            hourList.add(hourOfDay)
            minuteList.add(minute)
            alarmList.add(calSet)
        }

    private fun setAllAlarm() {
        // Trying periodic
        if (cb_morning.isChecked) {
            val flexTime: Long =
                calculateFlex(mornCal.get(Calendar.HOUR_OF_DAY), mornCal.get(Calendar.MINUTE), 1)
            val workRequest = PeriodicWorkRequest.Builder(
                    FinalAlarmWorker::class.java,
                    1, java.util.concurrent.TimeUnit.DAYS,
                    flexTime, java.util.concurrent.TimeUnit.MILLISECONDS
                )
                .build()

            WorkManager.getInstance(context!!).enqueueUniquePeriodicWork(
                "AlarmMorn",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
        }

        if (cb_afternoon.isChecked) {
            val flexTime: Long =
                calculateFlex(evenCal.get(Calendar.HOUR_OF_DAY), evenCal.get(Calendar.MINUTE), 1)
            val workRequest2 = PeriodicWorkRequest.Builder(
                    FinalAlarmWorker::class.java,
                    1, java.util.concurrent.TimeUnit.DAYS,
                    flexTime, java.util.concurrent.TimeUnit.MILLISECONDS
                )
                .build()

            WorkManager.getInstance(context!!).enqueueUniquePeriodicWork(
                "AlarmEven",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest2
            )
        }


        if (cb_night.isChecked) {
            val flexTime: Long =
                calculateFlex(nightCal.get(Calendar.HOUR_OF_DAY), nightCal.get(Calendar.MINUTE), 1)
            val workRequest3 = PeriodicWorkRequest.Builder(
                    FinalAlarmWorker::class.java,
                    1, java.util.concurrent.TimeUnit.DAYS,
                    flexTime, java.util.concurrent.TimeUnit.MILLISECONDS
                )
                .build()

            WorkManager.getInstance(context!!).enqueueUniquePeriodicWork(
                "AlarmNight",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest3
            )
        }

        if (cb_other.isChecked) {
            val flexTime: Long =
                calculateFlex(otherCal.get(Calendar.HOUR_OF_DAY), otherCal.get(Calendar.MINUTE), 1)
            val workRequest4 = PeriodicWorkRequest.Builder(
                    FinalAlarmWorker::class.java,
                    1, java.util.concurrent.TimeUnit.DAYS,
                    flexTime, java.util.concurrent.TimeUnit.MILLISECONDS
                )
                .build()

            WorkManager.getInstance(context!!).enqueueUniquePeriodicWork(
                "AlarmOthers",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest4
            )
        }


    }

    @Subscribe
    override fun getNotified(timeToTakeMedicine: String?) {

    }

    private fun calculateFlex(hourOfTheDay: Int, minOfTheDay: Int, periodInDays: Long): Long {
        // Initialize the calendar with today and the preferred time to run the job.
        val cal1 = Calendar.getInstance()
        cal1[Calendar.HOUR_OF_DAY] = hourOfTheDay
        cal1[Calendar.MINUTE] = minOfTheDay
        cal1[Calendar.SECOND] = 0

        // Initialize a calendar with now.
        val cal2 = Calendar.getInstance()
        if (cal2.timeInMillis < cal1.timeInMillis) {
            // Add the worker periodicity.
            cal2.timeInMillis =
                cal2.timeInMillis + java.util.concurrent.TimeUnit.DAYS.toMillis(periodInDays)
        }
        val delta = cal2.timeInMillis - cal1.timeInMillis
        return if (delta > PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS) delta else PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS
    }
}
