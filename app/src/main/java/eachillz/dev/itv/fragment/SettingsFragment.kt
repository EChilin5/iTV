package eachillz.dev.itv.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eachillz.dev.itv.activity.StarterActivity
import eachillz.dev.itv.databinding.FragmentUserSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import eachillz.dev.itv.overlay.forgotPasswordOverlay

private const val TAG = "TestFragment"
class SettingsFragment : Fragment() {

    private var _binding: FragmentUserSettingsBinding? = null
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignout.setOnClickListener {
           // callApi()
            Log.i(TAG, "User wants to logout")
            FirebaseAuth.getInstance().signOut()
            val intent  = Intent(context, StarterActivity::class.java)
            context?.startActivity(intent)
            activity?.finish()

        }

        binding.tvResetPassword.setOnClickListener {
            resetPassword()
        }

    }


    private fun resetPassword() {

        val dialog = forgotPasswordOverlay()
        val fm = parentFragmentManager
        dialog.show(fm, "name")
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}