package eachillz.dev.itv

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import eachillz.dev.itv.databinding.FragmentForgotPasswordOverlayBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



/**
 * A simple [Fragment] subclass.
 * Use the [forgotPasswordOverlay.newInstance] factory method to
 * create an instance of this fragment.
 */
class forgotPasswordOverlay : DialogFragment() {
    // TODO: Rename and change types of parameters
    private val TAG ="forgotPasswordOverlay"


    private var _binding: FragmentForgotPasswordOverlayBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForgotPasswordOverlayBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnResetEmail.setOnClickListener {

            val emailAddress: String = binding.etResetEmail.text.toString()

            Firebase.auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                    }else{
                        Log.d(TAG, "Email failed")
                    }
                }
            this.dismiss()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment forgotPasswordOverlay.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            forgotPasswordOverlay().apply {

            }
    }
}