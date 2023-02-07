package com.abdurashidov.dictionary.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.abdurashidov.dictionary.MainActivity
import com.abdurashidov.dictionary.R
import com.abdurashidov.dictionary.databinding.ActivitySplashBinding
import com.abdurashidov.dictionary.utils.Data

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            startActivity(Intent(this, MainActivity::class.java))
        }
        handler.postDelayed(runnable, 2000)

        val animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim)
        binding.image.animation = animation

    }

    override fun onStart() {
        super.onStart()
        if (Data.splash==1){
            this.finish()
        }
    }
}