package com.criddam.medicine.others

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.criddam.medicine.R
import java.util.regex.Pattern

object Utility {

    fun replaceFragmentsOverHomeActivity(
        fragmentManager: FragmentManager?,
        fragment: Fragment
    ) {
        val fragmentTransaction =
            fragmentManager!!.beginTransaction()
        fragmentTransaction.add(R.id.container_activity_home, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun isValidMobile(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length in 7..13
        } else false
    }

    fun verifyAvailableNetwork(activity: AppCompatActivity): Boolean {
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am: ActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses: List<ActivityManager.RunningAppProcessInfo> = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance === ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo: List<ActivityManager.RunningTaskInfo> = am.getRunningTasks(1)
            val componentInfo: ComponentName? = taskInfo[0].topActivity
            if (componentInfo!!.packageName == context.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }
}