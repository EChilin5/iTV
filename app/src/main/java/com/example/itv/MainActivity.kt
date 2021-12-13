package com.example.itv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.itv.databinding.ActivityMainBinding
import com.example.itv.fragment.HomeFragment
import com.example.itv.fragment.DailyMealFragment
import com.example.itv.image.ImageUploadFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val possibleMealsFragment = HomeFragment()
        val dailyMealFragment = DailyMealFragment()
        val settingFragment = ImageUploadFragment()

        binding.bottomNavBar.setOnItemSelectedListener { it->
            when(it.itemId){
                R.id.ic_home_daily->openFragment(dailyMealFragment)
                R.id.ic_meals->openFragment(possibleMealsFragment)
                R.id.ic_settings->openFragment(settingFragment)
            }
            true
        }

        openFragment(dailyMealFragment)
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_menu, fragment)
            commit()
        }
    }
}