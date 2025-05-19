package com.ahmed.profileapp.ui

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

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: ProfileViewModel

    private val galleryPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.profileImageUri.value = it.toString()
        }
    }

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

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showImagePickerOptions()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

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

    private fun chooseFromGallery() {
        galleryPicker.launch("image/*")
    }

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