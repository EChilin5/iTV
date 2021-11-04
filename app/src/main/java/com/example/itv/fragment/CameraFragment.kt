package com.example.itv.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.itv.databinding.FragmentCameraBinding
import com.example.itv.image.FirebaseStorageManager
import com.example.itv.overlayfood
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate


/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment() {

    companion object{
        const val REQUEST_FROM_CAMERA = 1001
        const val REQUEST_FROM_GALLERY = 1002
    }



    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private lateinit var functions: FirebaseFunctions

    private lateinit var barList:ArrayList<BarEntry>
    private lateinit var barDataSet: BarDataSet
    private lateinit var barData: BarData

    private val TAG = "CameraFragment"

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

        intUi()

        setUpBarChart()
//        binding.btnCapture.setOnClickListener{
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivityForResult(intent, 123)
//
//        }
//        binding.btnSave.setOnClickListener {
//            Toast.makeText(context, "selected button", Toast.LENGTH_LONG).show()
//            openFoodItem()
//        }

    }

    private fun setUpBarChart() {
        barList = ArrayList()
        barList.add(BarEntry(1f, 500f))
        barList.add(BarEntry(2f, 100f))
        barList.add(BarEntry(3f, 400f))
        barList.add(BarEntry(4f, 200f))
        barList.add(BarEntry(5f, 300f))
        barList.add(BarEntry(6f, 500f))
        barList.add(BarEntry(7f, 400f))

        barDataSet = BarDataSet(barList, "Population")
        barData = BarData(barDataSet)

        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS, 250)
        binding.barChart.data = barData

        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 15f

    }

    private fun intUi() {
//        binding.btnCapture.setOnClickListener{
//            captureImageCam()
//        }
//        binding.btnSave.setOnClickListener {
//            val imgURI = binding.btnSave.tag
//            if(imgURI == null){
//                Toast.makeText(context, "Please select Image", Toast.LENGTH_SHORT).show()
//            }else{
//                context?.let { it1 -> FirebaseStorageManager().uploadImage(it1, imgURI as Uri) }
//
//            }
//
//        }
    }

    private fun captureImageCam() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }

    private fun openFoodItem() {

        var dialog = overlayfood()
        dialog.show(childFragmentManager, "overlay")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!


            // Use Uri object instead of File to avoid storage permissions
//            binding.ivImageText.setImageURI(uri)
//            binding.btnSave.setTag(uri)
            context?.let { FirebaseStorageManager().uploadImage(it, uri) }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 123){
//            if(resultCode== Activity.RESULT_OK){
////                val photo = data?.extras?.get("data") as Bitmap
////                binding.ivImageText.setImageBitmap(photo)
////                val image = FirebaseVisionImage.fromBitmap(photo)
////                getImageText(image)
//
////                photo = scaleBitmapDown(photo, 640)
////                //convert bitmap to base64 encoded string
////                val byteArrayOutputStream = ByteArrayOutputStream()
////                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
////                val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
////                val base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
//
//                val bundle: Bundle = data?.extras!!
//                //form bundle, extract the image
//                //form bundle, extract the image
//                bundle["data"]
//                val bitmap = bundle["data"] as Bitmap?
//                //SET IMAGE IN IMAGE VIEW
//                //SET IMAGE IN IMAGE VIEW
//                binding.ivImageText.setImageBitmap(bitmap)
//                //proccess the Image
//
//                //1. create a firbaseVisionImage object from a bitmap object
//                //proccess the Image
//
//                //1. create a firbaseVisionImage object from a bitmap object
//                val image = FirebaseVisionImage.fromBitmap(bitmap!!)
//                // get an instance of firebasevISION
//                // get an instance of firebasevISION
//                val firebaseVision = FirebaseVision.getInstance()
//                //3.Create instance of the firebaseVisionfire
//                //3.Create instance of the firebaseVisionfire
//                val firebaseVisionTextRecognizer = firebaseVision.onDeviceTextRecognizer
//                //4 create task to proccess image
//                //4 create task to proccess image
//                val task = firebaseVisionTextRecognizer.processImage(image)
//
//                task.addOnSuccessListener { firebaseVisionText ->
//                    val s = firebaseVisionText.text
//                    // tvText.setText(s)
//                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
//                    Log.i("CameraFragment", s)
//
//
//
//                }
//                task.addOnFailureListener { e ->
//                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
//                    Log.i("CameraFragment" , e.message.toString())
//
//                }
//
//
//            }
//        }
//    }

    private fun getImageText(image: FirebaseVisionImage) {
        val textRecognizer = FirebaseVision.getInstance().cloudTextRecognizer
        textRecognizer.processImage(image)
            .addOnSuccessListener {
                val it = it.text
//                binding.tvImageText.text = it
               Log.i("CameraFragment", it)
                // calories
                // protein
                // fat
                // carbs
                // create a hashmap


                // ...
            }
            .addOnFailureListener {
                // Task failed with an exception
                // ...
                Toast.makeText(context, "failed" + it.message, Toast.LENGTH_LONG).show()
                Log.i("CameraFragment" , it.message.toString())
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


}