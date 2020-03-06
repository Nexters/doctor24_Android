package com.nexters.doctor24.todoc.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.ui.MainActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    lateinit var job : Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window?.run{
            addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        job = GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}
