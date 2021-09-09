package com.example.itv.image

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.google.firebase.storage.FirebaseStorage

class FirebaseStorageManager {
    private val TAG = "FirebaseStorageManager"
    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mProggressDialog: ProgressDialog


    fun uploadImage(context: Context,imageUri: Uri){
        mProggressDialog = ProgressDialog(context)
        mProggressDialog.setMessage("Please take photo")
        val uploadImage = mStorageRef.child("users/profilePic.png").putFile(imageUri)
        uploadImage.addOnSuccessListener {
            Log.e(TAG, "Image uploaded")
        }.addOnFailureListener{
            Log.e(TAG, "image failed" + it.message.toString())
        }
    }
}