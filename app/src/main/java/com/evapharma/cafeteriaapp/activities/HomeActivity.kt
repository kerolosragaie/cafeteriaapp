package com.evapharma.cafeteriaapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.databinding.ActivityHomeBinding
import com.evapharma.cafeteriaapp.fragments.HomeFragment
import com.evapharma.cafeteriaapp.fragments.MealsFragment
import com.evapharma.cafeteriaapp.fragments.OffersFragment
import com.evapharma.cafeteriaapp.fragments.ProfileFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    //Determine fragments:
    private val homeFragment = HomeFragment()
    private val mealsFragment = MealsFragment()
    private val offersFragment = OffersFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO: Add Drawer menu for top action bar:
        //Setting up the top action bar:
        setSupportActionBar(binding.tbHome)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //Setting the functionality of bottom nav bar:
        setSelectedFragOnCreate()
        replaceFragment(homeFragment)
    }


    /**
     * To set R.id.item_home enabled onCreate Activity
     * */
    private fun setSelectedFragOnCreate(){
        binding.bottomNavBar.setItemSelected(R.id.item_home)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_home_container,homeFragment).commit()
    }

    /**
     * When item pressed, fragment changes
     * */
   private fun replaceFragment(fragment: Fragment){
       //To switch between fragments:
       if(fragment!=null){
           val transaction = supportFragmentManager.beginTransaction()
           transaction.replace(R.id.fragment_home_container,fragment).commit()
       }

       binding.bottomNavBar.setOnItemSelectedListener {
           when(it){
               R.id.item_home ->{
                   replaceFragment(homeFragment)
                   binding.bottomNavBar.setItemSelected(it)
               }
               R.id.item_meals ->{
                   replaceFragment(mealsFragment)
                   binding.bottomNavBar.setItemSelected(it)
               }
               R.id.item_offers ->{
                   replaceFragment(offersFragment)
                   binding.bottomNavBar.setItemSelected(it)
               }
               R.id.item_profile ->{
                   replaceFragment(profileFragment)
                   binding.bottomNavBar.setItemSelected(it)
               }
           }
       }
   }

}