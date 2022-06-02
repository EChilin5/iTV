package eachillz.dev.itv.overlay

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import eachillz.dev.itv.databinding.FragmentAddWeightOverlayBinding
import eachillz.dev.itv.model.WeightWatcherModal
import eachillz.dev.itv.user.User
import java.util.*

private const val TAG = "AddWeightOverlay"
class AddWeightOverlay : DialogFragment() {

    private  var _binding: FragmentAddWeightOverlayBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestoreDB :FirebaseFirestore

    private lateinit var btnSubmit:Button
    private lateinit var etWeight : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddWeightOverlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestoreDB = FirebaseFirestore.getInstance()
        btnSubmit = binding.btnSubmitWeight
        etWeight = binding.etNewWeight

        btnSubmit.setOnClickListener {
            addNewWeight(etWeight.text.toString().trim())
        }
    }

    private fun addNewWeight(weight:String){
        var email = getEmail()
        val user = User("", email )
        var newWeight = WeightWatcherModal("", weight.toInt(), Date(), user )
        firestoreDB.collection("weightWatcher").add(newWeight).addOnCompleteListener {
            if(it.isSuccessful){
                Log.e(TAG, "added new weight")
                this.dismiss()
            }
        }
    }

    private fun getEmail():String{
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser?.email.toString()
    }
}