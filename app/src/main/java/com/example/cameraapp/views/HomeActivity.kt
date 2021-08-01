package com.example.cameraapp.views

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.cameraapp.R
import com.example.cameraapp.data.EntityTable
import com.example.cameraapp.viewmodel.EntityViewModel
import com.example.cameraapp.viewmodel.EntityViewmodelFactory
import kotlinx.android.synthetic.main.activity_capture_image.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: EntityViewModel
    private var captureImage: Uri? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        captureImage = intent?.getParcelableExtra("image")

        if (intent != null && intent.extras != null) {
            Glide.with(image)
                .load(captureImage)
                .placeholder(R.drawable.place_holder)
                .into(image)
        }

        /**
         * creating view model object
         **/
        val appObj = application as EntityApplication
        val repository = appObj.repository
        val viewModelFactory = EntityViewmodelFactory(repository)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(EntityViewModel::class.java)


        /**
         * saving image with name & time to our database
         **/
        btnSave.setOnClickListener {
            if (captureImage != null) {

                var imageName = etName.text.toString()
                val imageUri = captureImage.toString()
                val date = LocalDate.now().toString()
                val time = LocalTime.now().toString()

                if (etName.text.isEmpty()){
                    imageName = "Untitled"
                }

                val entityTable = EntityTable(imageName, imageUri, date, time)

                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.addEntity(entityTable)
                }
            }
        }


        /**
         * going homeActivity to MainActivity
         **/
        btnShow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        /**
         * button for capture image
         **/
        btnCapture.setOnClickListener {

            if (allPermissionGranted()) {
                val intent = Intent(this, CaptureImageActivity::class.java)
                startActivity(intent)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    Constant.REQUIRED_PERMISSION,
                    Constant.REQ_CODE_PERMISSION
                )
            }

        }
    }


    //Function for Change the bitmap format to Uri ->
    private fun getImageUri(bitmap: Bitmap): Uri? {
        val arrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayOutputStream)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "title", "desc")
        return Uri.parse(path)
    }


    private fun allPermissionGranted() =
        Constant.REQUIRED_PERMISSION.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.REQ_CODE_PERMISSION) {
            if (allPermissionGranted()) {
                val intent = Intent(this, CaptureImageActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Pls allow permission to access the camera",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }


}