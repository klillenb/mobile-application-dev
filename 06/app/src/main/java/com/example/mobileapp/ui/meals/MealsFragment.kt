package com.example.mobileapp.ui.meals

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapp.GalleryActivity
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentMealsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MealsFragment : Fragment() {

    private var _binding: FragmentMealsBinding? = null
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private var imageCapture: ImageCapture? = null
    private lateinit var imgCaptureExecutor: ExecutorService
    private lateinit var safeContext: Context
    private lateinit var outputDirectory: File

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val cameraProviderResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            permissionGranted ->
        if (permissionGranted){
            startCamera()
        } else {
            Snackbar.make(binding.root, "The camera permission is required", Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mealsViewModel =
            ViewModelProvider(this).get(MealsViewModel::class.java)

        _binding = FragmentMealsBinding.inflate(inflater, container, false)

        imgCaptureExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraProviderResult.launch(android.Manifest.permission.CAMERA)

        outputDirectory = getOutputDirectory()

        val imgCaptureBtn = binding.buttonTakePic
        imgCaptureBtn.setOnClickListener{
            takePhoto()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                animateFlash()
            }
        }

        val imgGalleryBtn = binding.buttonViewGallery
        imgGalleryBtn.setOnClickListener{
            val intent = Intent(activity, GalleryActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun startCamera() {
        val previewView = binding.previewView

        // listening for data from camera
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            imageCapture = ImageCapture.Builder().build()
            val preview = Preview.Builder().build().also{
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.d(TAG, "Use case binding failed")
            }
        }, ContextCompat.getMainExecutor(safeContext))
    }

    private fun takePhoto() {
        imageCapture?.let{
            // create a storage location and filename is stamped in milliseconds
            val filename = "JPEG_${System.currentTimeMillis()}"
            val file = File(outputDirectory, filename)

            // save the image in the above created file
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object: ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Log.i(TAG, "The image has been saved in ${file.toURI()}")
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(
                            binding.root.context,
                            "Error taking photo",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(TAG, "Error taking photo: $exception")
                    }
                }
            )
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else activity?.filesDir!!
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun animateFlash() {
        binding.root.postDelayed({
            binding.root.foreground = ColorDrawable(Color.WHITE)
            binding.root.postDelayed({
                binding.root.foreground = null
            }, 50)
        }, 100)
    }

    companion object {
        val TAG = "MealsFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}