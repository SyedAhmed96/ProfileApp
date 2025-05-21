// ProfileActivity.kt

package com.ahmed.profileapp.ui

// Imports necessary Android and binding classes.
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ahmed.profileapp.databinding.ActivityProfileBinding
import com.ahmed.profileapp.R

// Declares the Profile screen/activity.
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding // References for binding .
    private lateinit var viewModel: ProfileViewModel  // References for ViewModel.

    // Sets up binding, ViewModel, and observes navigation to EditProfile.
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

    // Placeholder for image picker (not implemented here).
    private fun showImagePickerOptions() {
        TODO("Not yet implemented")
    }
}