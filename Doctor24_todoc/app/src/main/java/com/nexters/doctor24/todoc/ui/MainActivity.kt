package com.nexters.doctor24.todoc.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.ui.map.NaverMapFragment

class MainActivity : AppCompatActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportFragmentManager.beginTransaction().replace(
            R.id.mapFragment,
            NaverMapFragment()
        ).commit()
    }
}
