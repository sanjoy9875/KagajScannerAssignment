package com.example.cameraapp.views

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.cameraapp.R
import com.example.cameraapp.data.EntityTable
import com.example.cameraapp.viewmodel.EntityViewModel
import com.example.cameraapp.viewmodel.EntityViewmodelFactory
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime


class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: EntityViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /**
         * creating view model object
         **/
        val appObj = application as EntityApplication
        val repository = appObj.repository
        val viewModelFactory = EntityViewmodelFactory(repository)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(EntityViewModel::class.java)

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

        }
    }


    //Function for Change the bitmap format to Uri ->
    private fun getImageUri(bitmap: Bitmap): Uri? {
        val arrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayOutputStream)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "title", "desc")
        return Uri.parse(path)
    }
}