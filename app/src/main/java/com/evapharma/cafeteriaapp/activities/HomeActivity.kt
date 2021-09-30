package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.databinding.ActivityHomeBinding
import com.evapharma.cafeteriaapp.fragments.*

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    //Determine fragments:
    private val homeFragment = HomeFragment()
    private val mealsFragment = MealsFragment()
    private val offersFragment = OffersFragment()

    //For navigation drawer:
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setting up the top action bar:
        setSupportActionBar(binding.tbHome)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //For navigation drawer:
        setUpDrawer()

        //Setting the functionality of bottom nav bar:
        setSelectedFragOnCreate()
        replaceFragment(homeFragment)
    }

    //To setup drawer:
    private fun setUpDrawer(){
        toggle = ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_drawer_menu)

        //For every button on navigation view:
        binding.nvHomeDrawer.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.item_drawer_myaccount ->{
                }
                R.id.item_drawer_usersaccounts ->{

                }
                R.id.item_drawer_logout ->{
                    startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                    Animatoo.animateCard(this@HomeActivity)
                    finish()
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //For bottom navigation bar:
    /**
     * To set R.id.item_home enabled onCreate Activity
     * */
    private fun setSelectedFragOnCreate(){
        binding.bottomNavBar.setItemSelected(R.id.item_bottomnav_home)
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
               R.id.item_bottomnav_home ->{
                   replaceFragment(homeFragment)
                   binding.bottomNavBar.setItemSelected(it)
               }
               R.id.item_bottomnav_meals ->{
                   replaceFragment(mealsFragment)
                   binding.bottomNavBar.setItemSelected(it)
               }
               R.id.item_bottomnav_offers ->{
                   replaceFragment(offersFragment)
                   binding.bottomNavBar.setItemSelected(it)
               }
           }
       }
   }

}