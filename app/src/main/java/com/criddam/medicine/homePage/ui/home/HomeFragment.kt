package com.criddam.medicine.homePage.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.criddam.medicine.R
import com.criddam.medicine.sideEffect.views.SideEffectFragment
import com.criddam.medicine.similarityOfDrug.views.SimilarityOfDrugFragment
import com.criddam.medicine.database.OfflineInformation
import com.criddam.medicine.drugCongradiction.views.DrugContradictionFragment
import com.criddam.medicine.drugInteraction.views.DrugInteractionFragment
import com.criddam.medicine.homePage.adapters.RecyclerViewAdapter
import com.criddam.medicine.login.views.LoginActivity
import com.criddam.medicine.medicineEntry.fragments.MedicineEntryOneFragment
import com.criddam.medicine.others.Utility
import com.criddam.medicine.userAllMedicine.AllMedicineListFragment

class HomeFragment : Fragment(), RecyclerViewAdapter.ItemListener {
    private lateinit var arrayList: ArrayList<String>
    private lateinit var offlineInformation: OfflineInformation
    private val NOTIFICATION_TYPE = "notification_type"
    private var notificationType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            notificationType = it.getString(NOTIFICATION_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        action(root)
        return root
    }

    private fun action(root: View) {
        offlineInformation = OfflineInformation(this.context!!)

        arrayList = ArrayList()
        arrayList.add(getString(R.string.add_current_medicine))
        arrayList.add(getString(R.string.view_all_current_medicine))
        arrayList.add(getString(R.string.view_drug_interaction))
        arrayList.add(getString(R.string.view_side_effect_of_drug))
        arrayList.add(getString(R.string.view_similarity_of_drug))
        arrayList.add(getString(R.string.view_contraindication_of_drug))
        arrayList.add(getString(R.string.logout))

        val manager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        val rvHome: RecyclerView = root.findViewById(R.id.rv_home)
        rvHome.layoutManager = manager
        rvHome.adapter =
            RecyclerViewAdapter(
                context,
                arrayList,
                this
            )
    }

    override fun onItemClick(item: String?) {
        when {
            item.equals(getString(R.string.add_current_medicine)) -> {
                Utility.replaceFragmentsOverHomeActivity(
                    activity!!.supportFragmentManager,
                    MedicineEntryOneFragment.newInstance()
                )
            }
            item.equals(getString(R.string.view_all_current_medicine), ignoreCase = true) -> {
                Utility.replaceFragmentsOverHomeActivity(
                    activity!!.supportFragmentManager,
                    AllMedicineListFragment.newInstance()
                )
            }
            item.equals(getString(R.string.view_drug_interaction), ignoreCase = true) -> {
                Utility.replaceFragmentsOverHomeActivity(
                    activity!!.supportFragmentManager,
                    DrugInteractionFragment.newInstance()
                )
            }
            item.equals(getString(R.string.view_side_effect_of_drug), ignoreCase = true) -> {
                Utility.replaceFragmentsOverHomeActivity(
                    activity!!.supportFragmentManager,
                    SideEffectFragment.newInstance()
                )
            }
            item.equals(getString(R.string.view_similarity_of_drug), ignoreCase = true) -> {
                Utility.replaceFragmentsOverHomeActivity(
                    activity!!.supportFragmentManager,
                    SimilarityOfDrugFragment.newInstance()
                )
            }
            item.equals(getString(R.string.view_contraindication_of_drug), ignoreCase = true) -> {
                Utility.replaceFragmentsOverHomeActivity(
                    activity!!.supportFragmentManager,
                    DrugContradictionFragment.newInstance()
                )
            }
            item.equals(getString(R.string.logout), ignoreCase = true) -> {
                offlineInformation.clearAll()
                Toast.makeText(context, getString(R.string.logout_successful), Toast.LENGTH_LONG)
                    .show()
                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }


        }
    }

    companion object {
        @JvmStatic
        fun newInstance(notification: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(NOTIFICATION_TYPE, notification)
                }
            }
    }
}