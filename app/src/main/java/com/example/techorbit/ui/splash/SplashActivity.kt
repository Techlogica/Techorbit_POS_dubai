package com.example.techorbit.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.techorbit.R
import com.example.techorbit.ui.activity.HomeActivity
import com.example.techorbit.ui.activity.SignUpActivity
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            if(TechorbitSharedPreference(this).getValue(KEYS.TOKEN)!=null){
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }else  {
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }


        }, 500)
    }
}