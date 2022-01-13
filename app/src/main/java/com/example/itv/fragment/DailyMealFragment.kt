package com.example.itv.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itv.databinding.FragmentDailyMealBinding
import com.example.itv.overlayfood
import com.example.itv.user.UserItemDataEntry
import com.example.itv.user.UserMealAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [DailyMealFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TAG = "DailyMealFragment"
private const val PICK_PHOTO_CODE = 1234
private lateinit var photFile: File


class DailyMealFragment : Fragment() {


    private var _binding: FragmentDailyMealBinding? = null
    private val binding get() = _binding!!
    private var dateFormated = ""
    private var FILE_NAME ="photo.jpg"


    private val database = Firebase.database
    private lateinit var userRecylerView: RecyclerView
    private lateinit var userArrayList: ArrayList<UserItemDataEntry>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val date = getCurrentDateTime()
        dateFormated = date.toString("MM/dd/yyyy")
        binding.tvCurrentDate.text = dateFormated

        binding.fabSave.setOnClickListener{
            Toast.makeText(context, "selected button", Toast.LENGTH_LONG).show()
            takePhoto()

        }

        userRecylerView = binding.rvDailyFood
        userRecylerView.layoutManager = LinearLayoutManager(context)

        userArrayList = arrayListOf<UserItemDataEntry>()

        getUserFoodData()


    }

    private fun takePhoto() {
        Log.i(TAG, "open up image picker on device")
        val imagePickerIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photFile = getPhotoFile(FILE_NAME)
//        imagePickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, photFile)
         val fileProvider = FileProvider.getUriForFile(requireContext(), "com.example.itv.fragment.fileprovider", photFile)
        imagePickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        if(imagePickerIntent.resolveActivity(activity?.packageManager!!) != null){
            startActivityForResult(imagePickerIntent, PICK_PHOTO_CODE )

        }else{
            Toast.makeText(context, "Unable to open Camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
       return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PICK_PHOTO_CODE && resultCode == Activity.RESULT_OK){
           // val takenImage = data?.extras?.get("data") as Bitmap
//
            val takenImage:String = photFile.absolutePath
            openFoodItem(takenImage)
        }else{
            super.onActivityResult(requestCode, resultCode, data)

        }
    }

    private fun openFoodItem( takenImage: String) {
        val args = Bundle()
        args.putString("Image", takenImage)
        val newFragment = overlayfood()
        newFragment.arguments = args
        newFragment.show(childFragmentManager, "TAG")


//
//        val dialog = overlayfood()
//        dialog.show(childFragmentManager, "overlay")
    }


    private fun getUserFoodData(){
        val userName = Firebase.auth.currentUser
        var currentUserName = ""
        userName?.let {
            for (profile in it.providerData) {

                currentUserName = profile.email.toString()

            }
        }
        currentUserName = currentUserName.dropLast(10)

        userArrayList.clear()
        var calories = 10000
        var total = 0
        val ref = database.getReference("UserMeal")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    userArrayList.clear()
                    calories = 10000
                    total = 0

                    for(userSnapShot in snapshot.children){
                            val user = userSnapShot.getValue(UserItemDataEntry::class.java)
                        if(user?.date!!.contains(dateFormated) && user.user == currentUserName) {
                            calories -= Integer.parseInt( user.calories.toString())
                            total +=  Integer.parseInt( user.calories.toString())
                                userArrayList.add(user)
                            }

                    }
                    if(userArrayList.isEmpty()){
                        val temp = UserItemDataEntry( "Example User" ,  "Temp User","0",
                            dateFormated)
                     userArrayList.add(temp)
                    }

                    userRecylerView.adapter = UserMealAdapter(userArrayList)
                }

                binding.tvCurrentCalorieCounter.text = total.toString()
                binding.tvRemainingCaloriesCounter.text = calories.toString()

                val progress1 = ((total).toDouble() / 10000) *100
                var progress2 = 100 - progress1

                binding.progressBar.progress = progress1.toInt()
                binding.progressBar2.progress = progress2.toInt()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "failed to load data", Toast.LENGTH_LONG).show()
            }

        })
    }



    private fun getCurrentDateTime():Date {
        return Calendar.getInstance().time
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DailyMealFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}



