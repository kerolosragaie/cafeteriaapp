package com.evapharma.cafeteriaapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.evapharma.cafeteriaapp.MEALS_MENU
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.databinding.ActivityMealDetailsBinding
import com.evapharma.cafeteriaapp.helpers.FoodAdapter
import com.evapharma.cafeteriaapp.menusList
import com.evapharma.cafeteriaapp.models.FoodItem
import com.evapharma.cafeteriaapp.models.Menu
import java.util.*


class MealDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealDetailsBinding

    //init components not connected to this activity:
    private lateinit var radioButtonView: View

    //Recycler view adapter, to make search/update easy on it:
    private lateinit var foodAdapter : FoodAdapter
    private lateinit var foodList:MutableList<FoodItem>


    //init some variables for sorting option:
    //sort according to ratings
    var ratingComparator = Comparator<FoodItem> { rest1, rest2 ->

        if (rest1.rate.compareTo(rest2.rate) == 0) {
            rest1.name.compareTo(rest2.name)
        } else {
            rest1.rate.compareTo(rest2.rate)
        }
    }

    //sort according to cost(decreasing)
    var costComparator = Comparator<FoodItem> { rest1, rest2 ->
        rest1.price.compareTo(rest2.price)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //tool bar init
        initToolbarComponents()

        //get recycler view items on load:
        loadAllFoodItems()
        binding.rvMealdetailsMeals.layoutManager = LinearLayoutManager(this@MealDetailsActivity)


    }

    //to initialize tool bar components:
    private fun initToolbarComponents(){
        //back button:
        binding.ivMealdetailsBackbtn.setOnClickListener { finish() }
        //search button:
        binding.etMealdetailsSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(strTyped: Editable?) {
                searchFun(strTyped.toString())
            }

        })

        //Add new meal button
        binding.ivMealdetailsAddmenu.setOnClickListener {  }

        //Sort recycler view button:
        binding.ivMealdetailsSortmenu.setOnClickListener {
            radioButtonView = View.inflate(this@MealDetailsActivity,R.layout.btn_sort_radio,null)

            AlertDialog.Builder(this@MealDetailsActivity)
                .setTitle("Sort By?")
                .setView(radioButtonView)
                .setPositiveButton("OK") { _, _ ->
                    if (radioButtonView.findViewById<RadioButton>(R.id.radio_high_to_low).isChecked) {
                        Collections.sort(foodList, costComparator)
                        foodList.reverse()
                    }
                    if (radioButtonView.findViewById<RadioButton>(R.id.radio_low_to_high).isChecked) {
                        Collections.sort(foodList, costComparator)
                    }
                    if (radioButtonView.findViewById<RadioButton>(R.id.radio_rating).isChecked) {
                        Collections.sort(foodList, ratingComparator)
                        foodList.reverse()

                    }
                    foodAdapter.notifyDataSetChanged()
                }
                .setNegativeButton("Cancel") { _, _ ->
                }
                .create()
                .show()
        }
    }

    /**
     * A function to search in the adapter without connecting to DB
     * */
    private fun searchFun(strTyped: String) {
        val filteredList = arrayListOf<FoodItem>()

        for (item in foodList) {
            if (item.name.lowercase(Locale.ROOT)
                    .contains(strTyped.lowercase(Locale.ROOT))
            ) {
                filteredList.add(item)
            }
        }

        if (filteredList.size == 0) {
            binding.errorMealdetails.errorNothingFound.visibility=View.VISIBLE
        } else {
            binding.errorMealdetails.errorNothingFound.visibility=View.GONE
        }

        //Adapter add it here:
        updateList(filteredList)

    }

    /**
     * For search functionality:
     * Added here to access main list which works on it
     * */
    fun updateList(filteredList: ArrayList<FoodItem>) {
        foodList = filteredList
        foodAdapter.notifyDataSetChanged()
    }

    //To load all food items
    private fun loadAllFoodItems(){
        //TODO: connect to DB to get food list
        binding.rvMealdetailsMeals.showShimmerAdapter()
        val bundle:Bundle? = intent.extras
        if(bundle?.containsKey(MEALS_MENU)!!){
            foodList = intent.extras?.get(MEALS_MENU) as MutableList<FoodItem>
        }

        //after getting list from db load it in adapter
        loadRvItems()
    }

    //load recycler view items:
    private fun loadRvItems(){
        if(foodList.isNotEmpty()){
            foodAdapter = FoodAdapter(this@MealDetailsActivity,foodList)
            binding.rvMealdetailsMeals.adapter = foodAdapter
        }
        binding.rvMealdetailsMeals.hideShimmerAdapter()
    }

}