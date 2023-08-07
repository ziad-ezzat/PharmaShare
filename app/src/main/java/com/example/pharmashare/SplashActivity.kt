package com.example.pharmashare

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class SplashActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val animationView: LottieAnimationView = findViewById(R.id.animation_view)
        val img = findViewById<ImageView>(R.id.img)
        var alp: Float
        animationView.addAnimatorUpdateListener {
            alp = it.animatedValue as Float
            img.alpha = alp
        }
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }, 5700)

    }
}