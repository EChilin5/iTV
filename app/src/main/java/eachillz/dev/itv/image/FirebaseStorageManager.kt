package eachillz.dev.itv.image

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class FirebaseStorageManager {
    private val TAG = "FirebaseStorageManager"
    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mProgressDialog: ProgressDialog


    fun uploadImage(context: Context, imageFileUri: Uri) {
        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage("Please wait, image being upload")
        mProgressDialog.show()
        val date = Date()
        val uploadTask = mStorageRef.child("posts/${date}.png").putFile(imageFileUri)
        uploadTask.addOnSuccessListener {
            Log.e(TAG, "Image Upload success")
            mProgressDialog.dismiss()
            val uploadedURL = mStorageRef.child("posts/${date}.png").downloadUrl
            uploadedURL.addOnSuccessListener {
                Log.e(TAG, "Image path: $it")
            }
            Log.e(TAG, "Uploaded $uploadedURL")
        }.addOnFailureListener {
            Log.e(TAG, "Image Upload fail")
            mProgressDialog.dismiss()
        }
    }
}