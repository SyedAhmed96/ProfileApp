// ProfileRepository.kt

package com.ahmed.profileapp.ui

//	Imports for preferences and LiveData.
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData

// Defines a singleton for profile data.
object ProfileRepository {

    //	Will store shared preferences.
    private lateinit var sharedPrefs: SharedPreferences

    // Public LiveData for profile info.
    val name = MutableLiveData<String>().apply { value = "Not set" }
    val dob = MutableLiveData<String>().apply { value = "Not set" }
    val gender = MutableLiveData<String>().apply { value = "Not set" }
    val profileImageUri = MutableLiveData<String>()

    // Initializes the repository and loads saved data.
    fun init(context: Context) {
        sharedPrefs = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
        loadData()
    }

    // 	Saves data to shared preferences.
    fun saveData() {
        sharedPrefs.edit().apply {
            putString("name", name.value)
            putString("dob", dob.value)
            putString("gender", gender.value)
            putString("profileImageUri", profileImageUri.value)
            apply()
        }
    }

    // Loads data from shared preferences to LiveData.
    private fun loadData() {
        name.value = sharedPrefs.getString("name", "Not set")
        dob.value = sharedPrefs.getString("dob", "Not set")
        gender.value = sharedPrefs.getString("gender", "Not set")
        profileImageUri.value = sharedPrefs.getString("profileImageUri", null)
    }
}