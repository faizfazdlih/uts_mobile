package com.example.uts_mobile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.uts_mobile.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvAppName.text = "UTS Mobile"
        binding.tvName.text = "Faiz Fazdlih"
        binding.tvNim.text = "152023080"
        binding.ivProfile.setImageResource(R.drawable.faiz)

        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 1000
        binding.ivProfile.startAnimation(fadeIn)

        val slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        slideUp.duration = 1000
        slideUp.startOffset = 300
        binding.root.findViewById<androidx.cardview.widget.CardView>(R.id.cardContent)?.startAnimation(slideUp)

        binding.progressBar.visibility = android.view.View.VISIBLE

        animateProgressBar()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 5000)
    }

    private fun animateProgressBar() {
        val handler = Handler(Looper.getMainLooper())
        var progress = 0

        val runnable = object : Runnable {
            override fun run() {
                if (progress <= 100) {
                    binding.progressBar.progress = progress
                    binding.tvProgress.text = "$progress%"
                    progress += 2
                    handler.postDelayed(this, 100)
                }
            }
        }
        handler.post(runnable)
    }
}