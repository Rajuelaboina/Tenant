package com.tenant.mytenant.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.TextView
import com.tenant.mytenant.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var textView:TextView = findViewById(R.id.textView25)
        object : CountDownTimer(5000,1000){
            override fun onTick(millisUntilFinished: Long) {
                textView.setText("Loading !: "+millisUntilFinished/1000)
            }

            override fun onFinish() {
                //textView.setText("done!")
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                finish()
            }

        }.start()
        /*Handler(Looper.getMainLooper()).postDelayed(Runnable {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()

        },3000)*/
    }
}