package eachillz.dev.itv.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import eachillz.dev.itv.R
import eachillz.dev.itv.databinding.ActivityMainBinding
import eachillz.dev.itv.fragment.HomeFragment
import eachillz.dev.itv.fragment.DailyMealFragment
import eachillz.dev.itv.fragment.ProggressFragment
import eachillz.dev.itv.image.ImageUploadFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val possibleMealsFragment = HomeFragment()
        val dailyMealFragment = DailyMealFragment()
        val progressFragment = ProggressFragment()
        val settingFragment = ImageUploadFragment()

        binding.bottomNavBar.setOnItemSelectedListener { it->
            when(it.itemId){
                R.id.ic_home_daily->openFragment(dailyMealFragment)
                R.id.ic_meals->openFragment(possibleMealsFragment)
                R.id.ic_settings->openFragment(settingFragment)
//                R.id.ic_progress->openFragment(progressFragment)
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