package com.rezaharis.githubuserapp2.ui.view.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.rezaharis.githubuserapp2.R
import com.rezaharis.githubuserapp2.broadcast.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var setAlarm: String
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var setReminder: SwitchPreferenceCompat

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        alarmReceiver = AlarmReceiver()
        init()

        val sharedPreferences = preferenceManager.sharedPreferences
        setReminder.isChecked = sharedPreferences.getBoolean(setAlarm, false)

    }

    private fun init(){
        setAlarm = resources.getString(R.string.set_reminder)
        setReminder = findPreference<SwitchPreferenceCompat>(setAlarm) as SwitchPreferenceCompat
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == setAlarm){
            if (sharedPreferences != null){
                setReminder.isChecked = sharedPreferences.getBoolean(setAlarm, false)
            }
        }
        val state: Boolean = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(setAlarm, false)
        setReminder(state)
    }

    private fun setReminder(state: Boolean){
        if (state){
            val time = Calendar.getInstance()
            time.set(Calendar.HOUR_OF_DAY, "09".toInt())
            time.set(Calendar.MINUTE, "00".toInt())
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val message = getString(R.string.check_message)
            alarmReceiver.setRepeatingAlarm(requireActivity(), AlarmReceiver.TYPE_REPEATING, dateFormat.format(time.time), message)
        } else{
            alarmReceiver.cancelAlarm(requireActivity(), AlarmReceiver.TYPE_REPEATING)
        }
    }
}