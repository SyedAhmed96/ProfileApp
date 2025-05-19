package com.ahmed.profileapp.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData

object ProfileRepository {
    private lateinit var sharedPrefs: SharedPreferences

    val name = MutableLiveData<String>().apply { value = "Not set" }
    val dob = MutableLiveData<String>().apply { value = "Not set" }
    val gender = MutableLiveData<String>().apply { value = "Not set" }
    val profileImageUri = MutableLiveData<String>()

    fun init(context: Context) {
        sharedPrefs = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
        loadData()
    }

    fun saveData() {
        sharedPrefs.edit().apply {
            putString("name", name.value)
            putString("dob", dob.value)
            putString("gender", gender.value)
            putString("profileImageUri", profileImageUri.value)
            apply()
        }
    }

    private fun loadData() {
        name.value = sharedPrefs.getString("name", "Not set")
        dob.value = sharedPrefs.getString("dob", "Not set")
        gender.value = sharedPrefs.getString("gender", "Not set")
        profileImageUri.value = sharedPrefs.getString("profileImageUri", null)
    }
}