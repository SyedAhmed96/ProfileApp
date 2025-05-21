// ProfileViewModel.kt

package com.ahmed.profileapp.ui

// Imports for ViewModel and LiveData.
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

// Defines the ViewModel.
class ProfileViewModel : ViewModel() {

    // Getters for profile fields from repository.
    val name = ProfileRepository.name
    val dob = ProfileRepository.dob
    val gender = ProfileRepository.gender
    val profileImageUri = ProfileRepository.profileImageUri

    var onChangeProfilePictureClick: (() -> Unit)? = null    // Lambda for handling profile image change clicks.
    val currentPhotoPath = MutableLiveData<String>()        // Stores file path for new profile images.

    // When called, this function triggers the action assigned to onChangeProfilePictureClick. Typically, this is used when the user taps to change their profile picture.
    fun handleProfilePictureClick() {
        onChangeProfilePictureClick?.invoke()
    }

    val genderOptions = listOf("Male", "Female")        // List of gender options for spinner.
    val selectedGender = MutableLiveData<String>()      // 	Currently selected gender.


    // LiveData for navigation to the edit screen.
    private val _navigateToEditProfile = MutableLiveData<Boolean>()
    val navigateToEditProfile: LiveData<Boolean> get() = _navigateToEditProfile

    // When called, this sets the _navigateToEditProfile LiveData to true, signaling the UI to navigate to the Edit Profile screen.
    fun onEditProfileClicked() {
        _navigateToEditProfile.value = true
    }

    // 	Trigger navigation to edit screen.
    fun doneNavigating() {
        _navigateToEditProfile.value = false
    }

    // 	private val _navigateBack ...
    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    // 	Saves data and triggers navigation back.
    fun onSaveProfile() {
        selectedGender.value?.let { gender.value = it }
        ProfileRepository.saveData() // Save data before navigating back
        _navigateBack.value = true
    }

    // Resets the navigation-back flag.
    fun doneNavigatingBack() {
        _navigateBack.value = false
    }
}