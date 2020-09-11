package com.criddam.medicine.homePage

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.criddam.medicine.BuildConfig
import com.criddam.medicine.R
import com.criddam.medicine.database.OfflineInformation
import com.criddam.medicine.drugCongradiction.views.DrugContradictionFragment
import com.criddam.medicine.drugInteraction.views.DrugInteractionFragment
import com.criddam.medicine.homePage.ui.home.HomeFragment
import com.criddam.medicine.homePage.viewModels.HomeViewModel
import com.criddam.medicine.login.views.LoginActivity
import com.criddam.medicine.medicineEntry.fragments.MedicineEntryOneFragment
import com.criddam.medicine.medicineEntry.models.TakingPeriods
import com.criddam.medicine.others.MedicineTime
import com.criddam.medicine.others.CridDamOnetimeWorker
import com.criddam.medicine.others.Utility
import com.criddam.medicine.rubelportion.HealthStatus
import com.criddam.medicine.rubelportion.HelthnoteActivity
import com.criddam.medicine.sideEffect.views.SideEffectFragment
import com.criddam.medicine.similarityOfDrug.views.SimilarityOfDrugFragment
import com.criddam.medicine.userAllMedicine.AllMedicineListFragment
import com.criddam.medicine.userAllMedicine.models.Datum
import com.criddam.medicine.userProfile.views.EditProfileFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity(), MedicineTime {
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var offlineInformation: OfflineInformation
    private lateinit var viewModelProvider: HomeViewModel
    private lateinit var medicineList: ArrayList<Datum>
    private lateinit var laterTakeingMedicineList: ArrayList<Datum>

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        offlineInformation.saveUserAlarm(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Changing language to bangla
        val locale = Locale("bn")
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )

        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        action(toolbar)
    }

    private fun action(toolbar: Toolbar) {
        medicineList = ArrayList()
        laterTakeingMedicineList = ArrayList()
        offlineInformation = OfflineInformation(this)
        tv_home_user_name.text = "Welcome ".plus(offlineInformation.userFirstName).plus(" ")
            .plus(offlineInformation.userLastName)

        drawerLayout = findViewById(R.id.drawer_layout)
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        val tvVersionNumber: TextView = findViewById(R.id.tv_verson_number)
        tvVersionNumber.text =
            getString(R.string.txt_version_number).plus("  ").plus(BuildConfig.VERSION_NAME)
        nav_view.setNavigationItemSelectedListener { true }
        nav_view.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawer(GravityCompat.START)
            when (item.itemId) {
                R.id.nav_menu -> {
                    supportFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }
                R.id.nav_add_current_medicine -> Utility.replaceFragmentsOverHomeActivity(
                    supportFragmentManager,
                    MedicineEntryOneFragment.newInstance()
                )
                R.id.nav_view_all_current_medicine -> Utility.replaceFragmentsOverHomeActivity(
                    supportFragmentManager,
                    AllMedicineListFragment.newInstance()
                )
                R.id.nav_view_drug_interaction ->
                    Utility.replaceFragmentsOverHomeActivity(
                        supportFragmentManager,
                        DrugInteractionFragment.newInstance()
                    )
                R.id.nav_view_side_effect_of_drug -> Utility.replaceFragmentsOverHomeActivity(
                    supportFragmentManager,
                    SideEffectFragment.newInstance()
                )
                R.id.nav_view_similarity_of_drug -> Utility.replaceFragmentsOverHomeActivity(
                    supportFragmentManager,
                    SimilarityOfDrugFragment.newInstance()
                )
                R.id.nav_view_contraindication_of_drug -> Utility.replaceFragmentsOverHomeActivity(
                    supportFragmentManager,
                    DrugContradictionFragment.newInstance()
                )
                R.id.nav_view_edit_profile -> Utility.replaceFragmentsOverHomeActivity(
                    supportFragmentManager,
                    EditProfileFragment.newInstance()
                )
                R.id.nav_logout -> {
                    offlineInformation.clearAll()
                    Toast.makeText(this, getString(R.string.logout_successful), Toast.LENGTH_LONG)
                        .show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

                R.id.Helthstatus -> {
                    offlineInformation.clearAll()

                    startActivity(Intent(this, HealthStatus::class.java))
                    finish()
                }

                R.id.noteid -> {
                    offlineInformation.clearAll()

                    startActivity(Intent(this, HelthnoteActivity::class.java))
                    finish()
                }
            }

            return@OnNavigationItemSelectedListener true

        })

        if (intent.hasExtra("notification")) {
            if (intent.getStringExtra("notification") != "")
                showPopup()
        }

        // Firebase
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (offlineInformation.userAlarm!!)
                    if (!Utility.isAppIsInBackground(this@HomeActivity))
                        showPopup()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container_activity_home, HomeFragment.newInstance(""))
        fragmentTransaction.commit()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    override fun getNotified(timeToTakeMedicine: String?) {
        showPopup()
    }

    private fun showPopup() {
        viewModelProvider =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModelProvider.getMedicineList(this)
        viewModelProvider.medicineList.observe(this,
            Observer { listOfMedicine ->
                if (listOfMedicine.size > 0) {
                    medicineList.addAll(listOfMedicine)
                }
            })


        val builder:AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.drugexsitemtpy))
        builder.setPositiveButton(getString(R.string.okbaby)){
            dialog, which ->
            val builder2:AlertDialog.Builder=AlertDialog.Builder(this).setMessage(getString(R.string.emptymlist))

            builder2.setPositiveButton( getString(R.string.done)
            ){
                dialog, which -> dialog.dismiss()
            }
            builder2.show()
        }
        builder.show()


       /* val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(
            offlineInformation.userFirstName.plus(" ").plus(offlineInformation.userLastName)
                .plus(" ").plus(getString(R.string.it_s_time_to_take_your_medicine))
        )

        builder.setCancelable(false)
            .setOnDismissListener {

            }
            .setPositiveButton(
                getString(R.string.okbaby)
            ) { dialog, _ ->

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.emptystomack ))
                    .setCancelable(false)
                    .setNeutralButton(
                        getString(R.string.ok)
                    ) { dialog, _ ->
                        dialog.dismiss()
                    }.show()
                dialog.dismiss()
            }.setNegativeButton(
                getString(R.string.nobaby)
            ) { dialog, _ ->
                val mywork = OneTimeWorkRequest.Builder(
                        CridDamOnetimeWorker::class.java
                    )
                    .setInitialDelay(
                        600000,
                        TimeUnit.MILLISECONDS
                    )
                    .build()
                WorkManager.getInstance(this@HomeActivity).enqueue(mywork);
                dialog.dismiss()
            }.show()*/
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelaste el escaneo", Toast.LENGTH_SHORT).show()
            } else {
                //etCodigo.setText(result.getContents().toString())
                Toast.makeText(this,result.contents.toString(),Toast.LENGTH_LONG)
                Toast.makeText(this,"found data",Toast.LENGTH_SHORT)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
