package com.example.mobileapp.ui.upload

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.mobileapp.R
import java.io.ByteArrayOutputStream

class UploadFragment : Fragment() {
    private lateinit var selectImageButton: Button
    private lateinit var imageView: ImageView
    private lateinit var selectedImageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_upload, container, false)

        //Find the views
        selectImageButton = view.findViewById(R.id.select_image)
        imageView = view.findViewById(R.id.imageview)

        // Set a click listener for the "Select Image" button
        selectImageButton.setOnClickListener {
            showImagePickerOptions()
        }

        return view
    }

    private fun showImagePickerOptions() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select an Option")
        builder.setItems(options) { dialog, item ->
            when (item) {
                0 -> openCamera()
                1 -> openGallery()
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    val tempUri = getImageUri(requireContext(), imageBitmap)
                    selectedImageUri = tempUri
                    imageView.setImageURI(selectedImageUri)
                }
                PICK_IMAGE_REQUEST_CODE -> {
                    if (data != null) {
                        selectedImageUri = data.data!!
                        imageView.setImageURI(selectedImageUri)
                    }
                }
            }
        }
    }

    private fun getImageUri(context: Context, imageBitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, imageBitmap, "Title", null)
        return Uri.parse(path)
    }
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

}
