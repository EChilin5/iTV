package eachillz.dev.itv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import eachillz.dev.itv.databinding.FragmentOverlayfoodBinding
import eachillz.dev.itv.user.UserItemDataEntry
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapFactory

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File


private const val TAG = "DialogFragment"
class overlayfood : DialogFragment() {


    private var _binding: FragmentOverlayfoodBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var storageReference: StorageReference

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOverlayfoodBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //name, calories, carbs, protein
//        val mArgs = arguments
//        val myValue = mArgs!!.getString("keyUsed to send it...")
        storageReference = FirebaseStorage.getInstance().reference


        val imgPath = requireArguments().getString("Image")
        val bitmap = BitmapFactory.decodeFile((imgPath))
        binding.tvSaveImage.setImageBitmap(bitmap)

        val date = getCurrentDateTime()
        val dateInString = date.toString("MM/dd/yyyy")


        binding.btnPost.setOnClickListener {
            val name = binding.etName.text.toString()
            val calories = binding.etCalories.text.toString()
//            val carbs = binding.etCarbs.text.toString()
//            val protein = binding.etProtein.text.toString()
            if(name != "" && calories.toIntOrNull() != null) {
                uploadImage(name, calories, dateInString, imgPath)
                Toast.makeText(context, "Post is Saved", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, "Incorrect Information was provided", Toast.LENGTH_LONG).show()

            }
            this.dismiss()
        }
    }

    private fun uploadImage(name: String?, calories: String?,  dateInString: String,imgPath: String?) {
        val photoReference =
            storageReference.child("images/"+imgPath.toString())
        val file = Uri.fromFile(File(imgPath.toString()))

        photoReference.putFile( file)
            .continueWithTask { photoUploadTask ->
                Log.i(TAG, "upload bytes: ${photoUploadTask.result.bytesTransferred}")
                // retrieve image url of upload image
                photoReference.downloadUrl

            }.continueWithTask { downloadUrlTask ->

                    val userName = Firebase.auth.currentUser
                    var currentUserName = ""
                    userName?.let {
                        for (profile in it.providerData) {
                            // Id of the provider (ex: google.com)
                            currentUserName = profile.email.toString()
                        }
                    }
                    currentUserName = currentUserName.dropLast(10)
                    val dateNow = Calendar.getInstance().time
                    database = Firebase.database.reference
                    val user = UserItemDataEntry(currentUserName,name, calories, dateInString,downloadUrlTask.result.toString())
                    database.child("UserMeal").child(currentUserName+dateNow).setValue(user)

            }.addOnCompleteListener { postCreationTask ->
//                btnSubmitPost.isEnabled = true

                if (!postCreationTask.isSuccessful) {
//                    Toast.makeText(context, "UPLOADED failed", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "failed", postCreationTask.exception)
                }

            }
    }

//    private fun saveUserItem(name: String?, calories: String?,  dateInString: String, imgPath: String?): Task<Any> {
//        try {
//            val userName = Firebase.auth.currentUser
//            var currentUserName = ""
//            userName?.let {
//                for (profile in it.providerData) {
//                    // Id of the provider (ex: google.com)
//                    currentUserName = profile.email.toString()
//                }
//            }
//            currentUserName = currentUserName.dropLast(10)
//            val dateNow = Calendar.getInstance().time
//            database = Firebase.database.reference
//            val user = UserItemDataEntry(currentUserName,name, calories, dateInString,imgPath)
//            database.child("UserMeal").child(currentUserName+dateNow).setValue(user)
//        }catch (e: Exception){
//            Toast.makeText(context, "failed to write to db " + e.localizedMessage, Toast.LENGTH_LONG).show()
//        }
//    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }



    companion object {

        @JvmStatic
        fun newInstance() =
            overlayfood().apply {
                arguments = Bundle().apply {

                }
            }
    }
}