package com.example.itv.image

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.itv.ApiSampleViewModel
import com.example.itv.R
import com.example.itv.SampleApiViewModelFactory
import com.example.itv.StarterActivity
import com.example.itv.databinding.FragmentImageUploadBinding
import com.example.itv.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception
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

    private val TAG = "TestFragment"
    private var _binding: FragmentImageUploadBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: ApiSampleViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentImageUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSub.setOnClickListener {
           // callApi()
            Log.i(TAG, "User wants to logout")
            FirebaseAuth.getInstance().signOut()
            val intent  = Intent(context, StarterActivity::class.java)
            context?.startActivity(intent)

        }

    }

    private fun callApi() {

        val repository = Repository()
        val viewModelFactory = SampleApiViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ApiSampleViewModel::class.java)
        viewModel.getPost()

        try {
            viewModel.myResponse.observe(viewLifecycleOwner, {
                    response ->
                if(response.isSuccessful) {
                    Toast.makeText(context, response.body()?.id.toString(), Toast.LENGTH_LONG).show()
                    Log.d(TAG, response.body()?.myUserID.toString())
                    Log.d(TAG, response.body()?.id.toString())
                    Log.d(TAG, response.body()?.title.toString())
                    Log.d(TAG, response.body()?.body.toString())
                }

            })
        }catch (e:Exception){
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
            Log.d(TAG, e.message.toString())

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