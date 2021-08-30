package com.example.itv.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.itv.R
import com.example.itv.databinding.FragmentCameraBinding
import com.example.itv.overlayfood
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage


/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment() {



    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private lateinit var functions: FirebaseFunctions


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }


    //prevent memory leaks from occurring
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCapture.setOnClickListener{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 123)

        }
        binding.btnSave.setOnClickListener {
            Toast.makeText(context, "selected button", Toast.LENGTH_LONG).show()
            openFoodItem(context)
        }

    }

    private fun openFoodItem(context: Context?) {

        var dialog = overlayfood()
        dialog.show(childFragmentManager, "overlay")
    }

    private fun openFoodItemOverlay() {




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123){
            if(resultCode== Activity.RESULT_OK){
                val photo = data?.extras?.get("data") as Bitmap
                binding.ivImageText.setImageBitmap(photo)
                val image = FirebaseVisionImage.fromBitmap(photo)
                getImageText(image)

//                photo = scaleBitmapDown(photo, 640)
//                //convert bitmap to base64 encoded string
//                val byteArrayOutputStream = ByteArrayOutputStream()
//                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
//                val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
//                val base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

            }
        }
    }

    private fun getImageText(image: FirebaseVisionImage) {
        val textRecognizer = FirebaseVision.getInstance().cloudTextRecognizer
        textRecognizer.processImage(image)
            .addOnSuccessListener {
//                val it = it.text
//                binding.tvImageText.text = it
//                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                // ...
            }
            .addOnFailureListener {
                // Task failed with an exception
                // ...
                Toast.makeText(context, "failed", Toast.LENGTH_LONG).show()

            }
    }

    /// scale down image in order to save bandwith
    // this is an optional
    private fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var resizedWidth = maxDimension
        var resizedHeight = maxDimension
        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension
            resizedWidth =
                (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension
            resizedHeight =
                (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = maxDimension
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CameraFragment().apply {

            }
    }
}