package com.criddam.medicine.others

import android.app.Application
import com.google.firebase.FirebaseApp

class CriddamApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}