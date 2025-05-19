package com.ahmed.profileapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class ProfileViewModel : ViewModel() {
    val name = ProfileRepository.name
    val dob = ProfileRepository.dob
    val gender = ProfileRepository.gender
    val profileImageUri = ProfileRepository.profileImageUri


    var onChangeProfilePictureClick: (() -> Unit)? = null
    val currentPhotoPath = MutableLiveData<String>()

    fun handleProfilePictureClick() {
        onChangeProfilePictureClick?.invoke()
    }

    val genderOptions = listOf("Male", "Female")
    val selectedGender = MutableLiveData<String>()

    private val _navigateToEditProfile = MutableLiveData<Boolean>()
    val navigateToEditProfile: LiveData<Boolean> get() = _navigateToEditProfile

    fun onEditProfileClicked() {
        _navigateToEditProfile.value = true
    }

    fun doneNavigating() {
        _navigateToEditProfile.value = false
    }

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    fun onSaveProfile() {
        selectedGender.value?.let { gender.value = it }
        ProfileRepository.saveData() // Save data before navigating back
        _navigateBack.value = true
    }

    fun doneNavigatingBack() {
        _navigateBack.value = false
    }
}