package com.ahmed.profileapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ahmed.profileapp.databinding.ActivityProfileBinding
import com.ahmed.profileapp.R

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProfileRepository.init(this) // Initialize SharedPreferences

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToEditProfile.observe(this) { shouldNavigate ->
            if (shouldNavigate == true) {
                startActivity(Intent(this, EditProfileActivity::class.java))
                viewModel.doneNavigating()
            }
        }
    }

    private fun showImagePickerOptions() {
        TODO("Not yet implemented")
    }
}