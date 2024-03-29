package com.example.mobileapp.ui.upload

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapp.databinding.FragmentUploadBinding
import com.example.mobileapp.dto.RecipeDto
import com.example.mobileapp.model.SharedViewModel
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileNotFoundException


class UploadFragment : Fragment() {
    private lateinit var selectImageButton: Button
    private lateinit var saveRecipeButton: Button
    private lateinit var imageView: ImageView
    private lateinit var selectedImageUri: Uri
    private lateinit var name: EditText
    private lateinit var ingredients: EditText
    private lateinit var instructions: EditText
    private lateinit var description: EditText

    private var _binding: FragmentUploadBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // useful for seeing resource usage violations
        /*
        StrictMode.setVmPolicy(
            VmPolicy.Builder(StrictMode.getVmPolicy())
                .detectLeakedClosableObjects()
                .build()
        )
         */

        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        saveRecipeButton = binding.save
        selectImageButton = binding.selectImage
        imageView = binding.imageview

        name = binding.title
        ingredients = binding.ingredients
        instructions = binding.instructions
        description = binding.description

        selectImageButton.setOnClickListener {
            showImagePickerOptions()
        }

        saveRecipeButton.setOnClickListener { saveRecipeToDb() }

        return root
    }

    private fun saveRecipeToDb() {
        val uploadViewModel = ViewModelProvider(this)[sharedViewModel::class.java]
        var imageBase64: String? = ""

        // primitive form validation
        if(name.text.toString().isEmpty()) name.error = "Recipe name cannot be empty!"
        if(ingredients.text.toString().isEmpty()) ingredients.error = "Ingredients cannot be empty!"
        if(instructions.text.toString().isEmpty()) instructions.error = "Instructions cannot be empty!"
        if(description.text.toString().isEmpty()) description.error = "Description cannot be empty!"

        if(name.text.toString().isNotEmpty() && ingredients.text.toString().isNotEmpty()
            && instructions.text.toString().isNotEmpty() && description.text.toString().isNotEmpty()) {
            if(this::selectedImageUri.isInitialized) {
                try {
                    imageBase64 = encodeImageToBase64(Uri.parse(selectedImageUri.toString()))
                } catch (e: Exception) {
                    println(e)
                    Toast.makeText(context, "Something went wrong while converting image for upload!", Toast.LENGTH_SHORT).show()
                }
            }
            val newRecipeDto = RecipeDto(null,
                name.text.toString(), listOf(ingredients.text.toString()),
                instructions.text.toString(), description.text.toString(), imageBase64)

            uploadViewModel.saveData(newRecipeDto)
            uploadViewModel.getData()
        } else Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("Recycle")
    private fun encodeImageToBase64(imagePath: Uri): String {
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(requireContext().contentResolver.openFileDescriptor(imagePath, "r")?.fileDescriptor)
        } catch (e: FileNotFoundException) {
            println(e.stackTrace.toString())
        }
        if(fis != null) {
            val imageBitmap: Bitmap = BitmapFactory.decodeStream(fis)
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val bytes: ByteArray = baos.toByteArray()
            fis.close()

            return Base64.encodeToString(bytes, Base64.DEFAULT)
        }
        return ""
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
        val path = Media.insertImage(context.contentResolver, imageBitmap, "Title", null)
        return Uri.parse(path)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        name.text.clearSpans()
        ingredients.text.clearSpans()
        instructions.text.clearSpans()
        description.text.clearSpans()
        _binding = null
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

}
