// EditProfileActivity.kt

package com.ahmed.profileapp.ui

// Brings in all needed Android, binding, and utility classes.
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ahmed.profileapp.databinding.ActivityEditProfileBinding
import java.io.File
import com.ahmed.profileapp.R
import java.text.SimpleDateFormat
import java.util.*

// Declares the EditProfile screen/activity.
class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding //	Will hold references to layout binding .
    private lateinit var viewModel: ProfileViewModel // References for ViewModel.


    //	Handles result from gallery image picker and updates ViewModel with image URI.
    private val galleryPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.profileImageUri.value = it.toString()
        }
    }

    // Handles result from taking a picture with the camera and updates ViewModel with image URI.
    private val cameraCapture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            viewModel.currentPhotoPath.value?.let { path ->
                val file = File(path)
                val uri = FileProvider.getUriForFile(
                    this,
                    "${packageName}.provider",
                    file
                )
                viewModel.profileImageUri.value = uri.toString()
            }
        }
    }

    // Handles permission request results for camera use.
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showImagePickerOptions()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Android’s entry point for the activity. Sets up data binding, ViewModel, initial values, click handlers, and back navigation.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProfileRepository.init(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        viewModel.name.value = ProfileRepository.name.value
        viewModel.dob.value = ProfileRepository.dob.value
        viewModel.gender.value?.let { viewModel.selectedGender.value = it }

        viewModel.onChangeProfilePictureClick = {
            showImagePickerOptions()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateBack.observe(this) { shouldNavigate ->
            if (shouldNavigate) {
                finish()
                viewModel.doneNavigatingBack()
            }
        }
    }

    // Shows dialog for picking image from gallery or taking a new photo, checks permissions.
    private fun showImagePickerOptions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
            AlertDialog.Builder(this)
                .setTitle("Choose Profile Picture")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> takePhotoFromCamera()
                        1 -> chooseFromGallery()
                        2 -> Unit // Do nothing for Cancel
                    }
                }
                .show()
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Prepares file and launches camera intent.
    private fun takePhotoFromCamera() {
        val photoFile = createImageFile()
        val photoURI = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            photoFile
        )
        viewModel.currentPhotoPath.value = photoURI.toString()
        cameraCapture.launch(photoURI)
    }

    // Launches gallery picker intent.
    private fun chooseFromGallery() {
        galleryPicker.launch("image/*")
    }

    // Creates a temp file to store a photo from camera.
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
}