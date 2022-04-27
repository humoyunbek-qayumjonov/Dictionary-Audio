package com.humoyunbek.dictionary

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.humoyunbek.dictionary.databinding.ActivityLiveBinding
import kotlinx.android.synthetic.main.help_live_layout.*

class LiveActivity : AppCompatActivity(),HomeFragment.onSomeEventListener {
    lateinit var binding: ActivityLiveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navCantroller = findNavController(R.id.nav_host_fragment_activity_main)
        binding.include.bottomBar.setupWithNavController(navCantroller)
        bottom_bar.itemIconTintList = null
        binding.navView.setupWithNavController(navCantroller)

    }


    override fun someEvent(s: Int?) {
        if (s==1){
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}