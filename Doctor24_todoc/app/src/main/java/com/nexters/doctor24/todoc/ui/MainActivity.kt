package com.nexters.doctor24.todoc.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.ui.map.NaverMapFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(
            R.id.mapFragment,
            NaverMapFragment()
        ).commit()
    }
}
