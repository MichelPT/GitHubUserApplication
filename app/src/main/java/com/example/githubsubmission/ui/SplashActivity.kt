package com.example.githubsubmission.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubsubmission.R
import com.example.githubsubmission.data.datastore.SettingPreferences
import com.example.githubsubmission.data.datastore.SwitchViewModelFactory
import com.example.githubsubmission.data.datastore.dataStore
import com.example.githubsubmission.ui.main.MainActivity
import com.example.githubsubmission.ui.modeSwitch.ModeSwitchViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        val pref = SettingPreferences.getInstance(application.dataStore)
        val modeSwitchViewModel =
            ViewModelProvider(this, SwitchViewModelFactory(pref))[ModeSwitchViewModel::class.java]
        modeSwitchViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        GlobalScope.launch {
            delay(1500)
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}