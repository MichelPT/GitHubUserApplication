package com.example.githubsubmission.ui.modeSwitch

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubsubmission.data.datastore.SettingPreferences
import com.example.githubsubmission.data.datastore.SwitchViewModelFactory
import com.example.githubsubmission.data.datastore.dataStore
import com.example.githubsubmission.databinding.ActivityModeSwitchBinding

class ModeSwitch : AppCompatActivity() {
    private lateinit var binding: ActivityModeSwitchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModeSwitchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val modeSwitchViewModel =
            ViewModelProvider(this, SwitchViewModelFactory(pref))[ModeSwitchViewModel::class.java]
        modeSwitchViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            modeSwitchViewModel.saveThemeSetting(isChecked)
        }
    }

}