package com.criddam.medicine.userAllMedicine

import android.Manifest
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Base64OutputStream
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.criddam.medicine.R
import com.criddam.medicine.medicineEntry.models.TakingPeriods
import com.criddam.medicine.userAllMedicine.models.Datum
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.util.*

class EditMedicineFragment : Fragment() {
    private var datum: Datum? = null
    private val MEDICINE_MODEL = "medicine_model"
    private val MY_PERMISSIONS_REQUEST_CAMERA = 1001
    private lateinit var ivMedicineImage: ImageView
    private lateinit var btnSubmitMedicine: Button
    private var imageString: String = ""
    private lateinit var  cbMorning: CheckBox
    private lateinit var cbAfternoon: CheckBox
    private lateinit var cbNight: CheckBox
    private lateinit var cbOther: CheckBox

    private lateinit var tvTimeMorning: TextView
    private lateinit var tvTimeAfternoon: TextView
    private lateinit var tvTimeNight: TextView
    private lateinit var tvTimeOther: TextView
    private var selectingTimeFor: String = ""
    private lateinit var calSet: Calendar
    private lateinit var alarmList:ArrayList<Calendar>
    private lateinit var takingPeriodList: ArrayList<TakingPeriods>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            datum = it.getParcelable(MEDICINE_MODEL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_edit_medicine, container, false)
        initialize(rootView)
        action()
        return rootView
    }

    private fun initialize(rootView: View) {
        btnSubmitMedicine = rootView.findViewById(R.id.btn_medicine_update)
        ivMedicineImage = rootView.findViewById(R.id.iv_medicine_image_edit_medicine)

        cbMorning = rootView.findViewById(R.id.cb_morning_edit_medicine)
        cbAfternoon = rootView.findViewById(R.id.cb_afternoon_edit_medicine)
        cbNight = rootView.findViewById(R.id.cb_night_edit_medicine)
        cbOther = rootView.findViewById(R.id.cb_other_edit_medicine)
        tvTimeMorning = rootView.findViewById(R.id.tv_select_time_morning_edit_medicine)
        tvTimeAfternoon = rootView.findViewById(R.id.tv_select_time_afternoon_edit_medicine)
        tvTimeNight = rootView.findViewById(R.id.tv_select_time_night_edit_medicine)
        tvTimeOther = rootView.findViewById(R.id.tv_select_time_other_edit_medicine)
    }

    private fun action() {
        takingPeriodList = ArrayList()
        alarmList = ArrayList()
        checkPermission()
        btnSubmitMedicine.setOnClickListener {
            Toast.makeText(
                context,
                getString(R.string.under_development),
                Toast.LENGTH_LONG
            ).show()
        }
        ivMedicineImage.setOnClickListener {
            ImagePicker.Companion.with(this@EditMedicineFragment)
                .compress(1024)
                .cameraOnly()
                .maxResultSize(
                    1080,
                    1080
                )
                .start()
        }
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

    }

    private fun openTimePickerDialog(is24r: Boolean) {
        val calendar: Calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            context,
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
            val calNow = Calendar.getInstance()
            calSet = calNow.clone() as Calendar
            calSet[Calendar.HOUR_OF_DAY] = hourOfDay
            calSet[Calendar.MINUTE] = minute
            calSet[Calendar.SECOND] = 0
            calSet[Calendar.MILLISECOND] = 0
            when (selectingTimeFor) {
                "morning" -> {
                    tvTimeMorning.text =
                        hourOfDay.toString().plus(":").plus(minute.toString())
                    cbMorning.isChecked = true
                }
                "afternoon" -> {
                    tvTimeAfternoon.text =
                        hourOfDay.toString().plus(":").plus(minute.toString())
                    cbAfternoon.isChecked = true
                }
                "night" -> {
                    tvTimeNight.text =
                        hourOfDay.toString().plus(":").plus(minute.toString())
                    cbNight.isChecked = true
                }
                "other" -> {
                    tvTimeOther.text =
                        hourOfDay.toString().plus(":").plus(minute.toString())
                    cbOther.isChecked = true
                }
            }

            takingPeriodList.add(
                TakingPeriods(
                    selectingTimeFor,
                    hourOfDay.toString().plus(":").plus(minute.toString())
                )
            )
            alarmList.add(calSet)
        }
    companion object {
        @JvmStatic
        fun newInstance(datum: Datum) =
            EditMedicineFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MEDICINE_MODEL, datum)
                }
            }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this.context!!, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity, Manifest.permission.CAMERA
                )
            ) {
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA
                )
            }
        } else {
            // Permission already granted
        }
    }

   private fun convertImageFileToBase64(imageFile: File): String {
        return FileInputStream(imageFile).use { inputStream ->
            ByteArrayOutputStream().use { outputStream ->
                Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                    inputStream.copyTo(base64FilterStream)
                    base64FilterStream.close() // This line is required, see comments
                    outputStream.toString()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                ivMedicineImage.setImageURI(fileUri)
                val file: File = ImagePicker.getFile(data)!!
                imageString = convertImageFileToBase64(file)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
