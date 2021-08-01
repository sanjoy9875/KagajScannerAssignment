package com.example.cameraapp.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
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

    private var currentAnimator: Animator? = null
    private var shortAnimationDuration: Int = 0

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

        image.setOnClickListener {
            zoomImageFromThumb(image,captureImage.toString())
        }

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

    }

    /**
     * Function for Change the bitmap format to Uri
     **/
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


    /**
     * function for zoom a photo
     **/
    private fun zoomImageFromThumb(thumbView: View, imageResId: String) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        currentAnimator?.cancel()

        // Load the high-resolution "zoomed-in" image.

        val expandedImageView: ImageView = findViewById(R.id.viewImage)

        Glide.with(expandedImageView)
            .load(imageResId)
            .into(expandedImageView)


        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBoundsInt)
        findViewById<View>(R.id.homeContainer)
            .getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
//        thumbView.alpha = 0f

        expandedImageView.visibility = View.VISIBLE

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.pivotX = 0f
        expandedImageView.pivotY = 0f

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    expandedImageView,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(
                    ObjectAnimator.ofFloat(
                        expandedImageView,
                        View.Y,
                        startBounds.top,
                        finalBounds.top
                    )
                )
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }
        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        expandedImageView.setOnClickListener {
            currentAnimator?.cancel()

            // Animate the four positioning/sizing properties in parallel,
            // back to their original values.
            currentAnimator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left)).apply {
                    with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale))
                }
                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        thumbView.alpha = 1f
                        expandedImageView.visibility = View.GONE
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        thumbView.alpha = 1f
                        expandedImageView.visibility = View.GONE
                        currentAnimator = null
                    }
                })
                start()
            }
        }
    }
}