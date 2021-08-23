package com.example.itv.image

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.itv.R
import com.example.itv.databinding.FragmentImageUploadBinding
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import java.net.URI
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageUploadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageUploadFragment : Fragment() {

    private var _binding: FragmentImageUploadBinding? = null
    private val binding get() = _binding!!
    lateinit var imageUri : Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentImageUploadBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var btnSelectImage:Button = binding.btnSelectImage;
        var btnUploadImage:Button = binding.btnUploadImage;

        btnSelectImage.setOnClickListener {
            selectImage()
        }

        btnUploadImage.setOnClickListener {
            uploadImage();
        }

    }

    private fun uploadImage() {
        val progrssDialog = ProgressDialog(context);
        progrssDialog.setMessage("Uploading File...");

        progrssDialog.setCancelable(false)
        progrssDialog.show()

        
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(imageUri).addOnSuccessListener {

            binding.imageView.setImageURI(null)
            Toast.makeText(context, "Success uploaded", Toast.LENGTH_LONG).show()

            if(progrssDialog.isShowing) progrssDialog.dismiss()

        }.addOnFailureListener {
            if(progrssDialog.isShowing) progrssDialog.dismiss()
            Toast.makeText(context, "Failed to uploaded   " + it, Toast.LENGTH_LONG).show()

        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data )

        if(requestCode === 100 && resultCode == Activity.RESULT_OK){
            imageUri = data?.data!!
            binding.imageView.setImageURI(imageUri)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ImageUploadFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImageUploadFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}