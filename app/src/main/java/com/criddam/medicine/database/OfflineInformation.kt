package com.criddam.medicine.database

import android.content.Context
import android.content.SharedPreferences
import com.criddam.medicine.others.Keys
import com.criddam.medicine.rubelportion.HealthStatus
import com.criddam.medicine.userAllMedicine.models.Datum
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



class OfflineInformation(var context: Context) {
    private var sharedpreferences: SharedPreferences =
        context.getSharedPreferences(Keys.USER_INFO, Context.MODE_PRIVATE)

    fun saveToken(token: String?) {
        val editor = sharedpreferences.edit()
        editor.putString(Keys.USER_TOKEN, token)
        editor.apply()
    }

    val token: String?
        get() = sharedpreferences.getString(Keys.USER_TOKEN, "")

    fun saveUserId(userId: Int?) {
        val editor = sharedpreferences.edit()
        editor.putInt(Keys.USER_ID, userId!!)
        editor.apply()
    }

    val userId: Int?
        get() = sharedpreferences.getInt(Keys.USER_ID, -1)

    fun saveUserAlarm(isAlarm: Boolean?) {
        val editor = sharedpreferences.edit()
        editor.putBoolean(Keys.USER_ALARM, isAlarm!!)
        editor.apply()
    }

    val userAlarm: Boolean?
        get() = sharedpreferences.getBoolean(Keys.USER_ALARM, false)

    fun saveUserName(userName: String?) {
        val editor = sharedpreferences.edit()
        editor.putString(Keys.USER_NAME, userName)
        editor.apply()
    }

    val userName: String?
        get() = sharedpreferences.getString(Keys.USER_NAME, "")

    fun saveUserFirstName(userFirstName: String?) {
        val editor = sharedpreferences.edit()
        editor.putString(Keys.USER_FIRST_NAME, userFirstName)
        editor.apply()
    }

    val userFirstName: String?
        get() = sharedpreferences.getString(Keys.USER_FIRST_NAME, "")

    fun saveUserLastName(userLastName: String?) {
        val editor = sharedpreferences.edit()
        editor.putString(Keys.USER_LAST_NAME, userLastName)
        editor.apply()
    }

    val userLastName: String? get() = sharedpreferences.getString(Keys.USER_LAST_NAME, "")

    fun saveAlarmType(alarmType: String?) {
        val editor = sharedpreferences.edit()
        editor.putString(Keys.ALARM_TYPE, userLastName)
        editor.apply()
    }

    val getAlarmType: String? get() = sharedpreferences.getString(Keys.ALARM_TYPE, "")

    open fun saveLaterTakingMedicines(list: ArrayList<Datum>): Unit {
        val editor = sharedpreferences.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(Keys.NOT_TAKEN_MEDICINE, json)
        editor.apply()
    }

    fun getLaterTakingMedicines(): ArrayList<Datum?>? {
        val gson = Gson()
        val json = sharedpreferences.getString(Keys.NOT_TAKEN_MEDICINE, null)
        val datum = object : TypeToken<ArrayList<Datum?>?>() {}.type
        return gson.fromJson(json, datum)
    }

    fun saveUserPhone(userPhone: String?) {
        val editor = sharedpreferences.edit()
        editor.putString(Keys.USER_PHONE, userPhone)
        editor.apply()
    }

    val userPhone: String?
        get() = sharedpreferences.getString(Keys.USER_PHONE, "")


    fun saveUserPassword(userPassword: String?) {
        val editor = sharedpreferences.edit()
        editor.putString(Keys.USER_PASSWORD, userPassword)
        editor.apply()
    }

    val userPassword: String?
        get() = sharedpreferences.getString(Keys.USER_PASSWORD, "")

    fun clearAll() {
        sharedpreferences.edit().clear().apply()
    }

}