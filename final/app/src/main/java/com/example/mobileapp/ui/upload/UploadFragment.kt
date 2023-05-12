package com.example.mobileapp.ui.upload

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.mobileapp.R

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
            openGallery()
        }

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            imageView.setImageURI(selectedImageUri)
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 1
    }

}
