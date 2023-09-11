package com.kbcoding.androiddevelopmentbasics

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kbcoding.androiddevelopmentbasics.databinding.ActivityEmptyBinding

private const val APP_PREFERENCES = "APP_PREFERENCES"
private const val PREF_TEXT_VALUE = "PREF_TEXT_VALUE"

class EmptyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmptyBinding

    private lateinit var preferences: SharedPreferences

    private val preferencesListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == PREF_TEXT_VALUE) {
                binding.tvValue.setText(preferences.getString(PREF_TEXT_VALUE, ""))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmptyBinding.inflate(layoutInflater).also { setContentView(it.root) }

        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        binding.etValue.setText(preferences.getString(PREF_TEXT_VALUE, ""))
        binding.tvValue.setText(preferences.getString(PREF_TEXT_VALUE, ""))

        binding.btnSave.setOnClickListener {
            val value = binding.etValue.text.toString()
            preferences.edit()
                .putString(PREF_TEXT_VALUE, value)
                .apply()
        }

        preferences.registerOnSharedPreferenceChangeListener(preferencesListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferences.unregisterOnSharedPreferenceChangeListener(preferencesListener)
    }


}