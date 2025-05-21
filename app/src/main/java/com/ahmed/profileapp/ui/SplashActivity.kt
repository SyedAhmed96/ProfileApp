//SplashActivity.kt


package com.ahmed.profileapp.ui

// Imports for Activity, binding, and handlers.
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ahmed.profileapp.databinding.ActivitySplashBinding

// Defines the splash screen activity.
class SplashActivity : AppCompatActivity() {

    // Reference for splash binding.
    private lateinit var binding: ActivitySplashBinding

    // Shows splash screen, waits 2.5 seconds, then opens ProfileActivity.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }, 2500)
    }
}
