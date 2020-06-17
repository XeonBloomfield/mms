package ovh.milkowski.mms_lab3

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage

class MainActivity : AppCompatActivity() {
    private val pickPhotoRequestCode = 101
    private var imageRecognition: Boolean = true

    private lateinit var analysisResult: TextView
    private lateinit var selectedImage: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup handlers
        analysisResult = findViewById(R.id.labelPreview)
        selectedImage = findViewById(R.id.uploadedImagePreview)

        // handle Select Image button click event
        val buttonSelectImage: Button = findViewById(R.id.buttonSelectImage)
        buttonSelectImage.setOnClickListener {
            pickImage()
        }

        // handle Switch to X button click event
        val buttonSwitch = findViewById<Button>(R.id.buttonSwitchText)
        buttonSwitch.setOnClickListener {
            if (imageRecognition) {
                imageRecognition = false
                buttonSwitch.setText(R.string.switch_to_image_recognition)
                analysisResult.text = getString(R.string.results)
            } else {
                imageRecognition = true
                buttonSwitch.setText(R.string.switch_to_text_recognition)
                analysisResult.text = getString(R.string.results)
            }
        }
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int, data: Intent?
    ) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                pickPhotoRequestCode -> {
                    val bitmap = getImageFromData(data)
                    bitmap?.apply {
                        selectedImage.setImageBitmap(this)
                        processImage(bitmap)
                    }
                }
            }
        }
        super.onActivityResult(
            requestCode, resultCode,
            data
        )
    }

    private fun processImage(bitmap: Bitmap) {
        val visionImg =
            FirebaseVisionImage.fromBitmap(bitmap)

        if (imageRecognition) {
            FirebaseVision.getInstance().onDeviceImageLabeler.processImage(visionImg)
                .addOnSuccessListener { tags ->
                    analysisResult.text = tags.joinToString(" ") {
                        it.text
                    }
                }
                .addOnFailureListener { ex ->
                    Log.wtf("LAB", ex)
                }
        } else {
            FirebaseVision.getInstance().onDeviceTextRecognizer.processImage(visionImg)
                .addOnSuccessListener { firebaseVisionText ->
                    if (firebaseVisionText.text.isNotEmpty()) {
                        analysisResult.text = firebaseVisionText.text
                    } else {
                        analysisResult.text = getString(R.string.no_results)
                    }
                }
                .addOnFailureListener { ex ->
                    Log.wtf("LAB", ex)
                }
        }
    }

    private fun getImageFromData(data: Intent?): Bitmap? {
        val selectedImage = data?.data
        return MediaStore.Images.Media.getBitmap(
            this.contentResolver,
            selectedImage
        )
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, pickPhotoRequestCode)
    }
}