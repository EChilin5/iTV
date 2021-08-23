package com.example.itv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.itv.databinding.ActivityMainBinding
import com.example.itv.fragment.CameraFragment
import com.example.itv.fragment.HomeFragment
import com.example.itv.fragment.DailyMealFragment
import com.example.itv.image.ImageUploadFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val cameraFragment = CameraFragment()
        val settingFragment = DailyMealFragment()
        val imageFragment = ImageUploadFragment()

        binding.bottomNavBar.setOnItemSelectedListener { it->
            when(it.itemId){
                R.id.ic_home->openFragment(homeFragment)
                R.id.ic_Info->openFragment(settingFragment)
                R.id.ic_camera->openFragment(cameraFragment)
                R.id.ic_imageTutorial->openFragment(imageFragment)
            }
            true
        }

        openFragment(homeFragment)
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_menu, fragment)
            commit()
        }
    }
}