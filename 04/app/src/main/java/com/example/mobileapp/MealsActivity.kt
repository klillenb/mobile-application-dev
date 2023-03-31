package com.example.mobileapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import com.example.mobileapp.databinding.ActivityMainBinding

class MealsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var capRequest: CaptureRequest.Builder
    lateinit var handler: Handler
    lateinit var handlerThread: HandlerThread
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var cameraCaptureSession: CameraCaptureSession
    lateinit var cameraDevice: CameraDevice
    lateinit var captureRequest: CaptureRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPermissions()
        setContentView(R.layout.fragment_meals)

        // Camera start
        textureView = findViewById(R.id.textureView)
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler((handlerThread).looper)

        textureView.surfaceTextureListener = object: TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                TODO("Not yet implemented")
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

            }

        }

        // Camera end
    }

    private fun getPermissions(){
        var permissionList = mutableListOf<String>()

        if(checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            permissionList.add(android.Manifest.permission.CAMERA)

        if(permissionList.size > 0){
            requestPermissions(permissionList.toTypedArray(), 101)
        }
    }

    @SuppressLint("MissingPermission")
    fun openCamera(){
        cameraManager.openCamera(cameraManager.cameraIdList[0], object: CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera

                var capRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                var surface = Surface(textureView.surfaceTexture)
                capRequest.addTarget(surface)

                cameraDevice.createCaptureSession(listOf(surface), object: CameraCaptureSession.StateCallback(){
                    override fun onConfigured(session: CameraCaptureSession) {
                        cameraCaptureSession = session
                        cameraCaptureSession.setRepeatingRequest(capRequest.build(), null, null)
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        TODO("Not yet implemented")
                    }

                }, handler)
            }

            override fun onDisconnected(camera: CameraDevice) {
                TODO("Not yet implemented")
            }

            override fun onError(camera: CameraDevice, error: Int) {
                TODO("Not yet implemented")
            }

        }, handler)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach {
            if(it != PackageManager.PERMISSION_GRANTED) getPermissions()
        }
    }
}