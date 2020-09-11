package com.criddam.medicine.medicineEntry.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Base64OutputStream
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.criddam.medicine.R
import com.criddam.medicine.others.Utility
import com.criddam.medicine.rubelportion.BarcodeActivity
import com.google.zxing.integration.android.IntentIntegrator
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class MedicineEntryTwoFragment : Fragment() {
    private val MEDICINE_TYPE = "medicine_type"
    private val MY_PERMISSIONS_REQUEST_CAMERA = 1001
    //    private lateinit var codeScanner: CodeScanner
    private lateinit var ivMedicineImage: ImageView
    private var imageString: String = "imagestringnotvailable"
    private var medicineType: String? = null
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "mindorks-welcome"
    private var qrvalue:String ="qrkeyfinal"
    private var qrvaluecheck:String=""

    var pref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            medicineType = it.getString(MEDICINE_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_medicine_entry_two, container, false)
        pref = context?.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()
        qrvaluecheck= pref?.getString("qrkeyfinal",null).toString()
        action(root)
        return root
    }

    private fun action(root: View) {
        checkPermission()

        ivMedicineImage = root.findViewById(R.id.iv_medicine_image)
        val etMedicineName: AppCompatEditText = root.findViewById(R.id.et_medicine_name)
        val next: Button = root.findViewById(R.id.btn_medicine_entry_step_two_next)

        ivMedicineImage.setOnClickListener {


            startActivity(Intent(context, BarcodeActivity::class.java))


            //startscaning()

            //startActivity(Intent(context,QrimagecameraopenrActivity::class.java))


           /* ImagePicker.Companion.with(this)
//                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)//Final image size will be less than 1 MB(Optional)
                .cameraOnly()
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()*/
        }

        next.setOnClickListener {



            if (etMedicineName.text.toString() == "")
                etMedicineName.error = getString(R.string.medicine_name)
            else if(qrvaluecheck==""){
                Toast.makeText(context,"Scan qr code please",Toast.LENGTH_SHORT)
            }
            else
            {
                Utility.replaceFragmentsOverHomeActivity(
                    fragmentManager,
                    MedicineEntryThreeFragment.newInstance(
                        medicineType.toString(),
                        etMedicineName.text.toString(),
                        imageString

                    )
                )

                editor?.remove("qrkeyfinal")
                editor?.commit()
            }
        }
    }



    companion object {
        @JvmStatic
        fun newInstance(medicineType: String) =
            MedicineEntryTwoFragment().apply {
                arguments = Bundle().apply { putString(MEDICINE_TYPE, medicineType) }
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
                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission already granted
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        println("never here")
        val scanResult =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, intent)
        if (scanResult != null) {
            // handle scan result
            Toast.makeText(context, scanResult.contents.toString(), Toast.LENGTH_SHORT).show()
        }

        Toast.makeText(context,"data not found",Toast.LENGTH_SHORT)
// else continue with any other code you need in the method
    }


}
