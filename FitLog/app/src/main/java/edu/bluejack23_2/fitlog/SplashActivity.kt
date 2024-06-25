package edu.bluejack23_2.fitlog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import edu.bluejack23_2.fitlog.handler.UserHandler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        checkUser()
    }

    private fun checkUser() {
        val userHandler = UserHandler()

        userHandler.getUserDetails { response ->
            val nextActivity = if (response.status) {
                HomeActivity::class.java
            } else {
                OnboardingActivity::class.java
            }

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@SplashActivity, nextActivity)
                startActivity(intent)
                finish()
            }, 2000)

        }
    }
}