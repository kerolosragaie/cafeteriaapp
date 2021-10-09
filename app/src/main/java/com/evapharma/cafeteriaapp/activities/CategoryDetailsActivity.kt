package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.evapharma.cafeteriaapp.CATEGORY_DATA
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.databinding.ActivityCategoryDetailsBinding
import com.evapharma.cafeteriaapp.helpers.ProductAdapter
import com.evapharma.cafeteriaapp.models.CategoryResponse
import com.evapharma.cafeteriaapp.models.ProductResponse
import com.evapharma.cafeteriaapp.services.ProductService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.*


class CategoryDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryDetailsBinding

    //Current category
    private lateinit var currentCatResponse: CategoryResponse

    //init components not connected to this activity:
    private lateinit var radioButtonView: View

    //Recycler view adapter, to make search/update easy on it:
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productsList: MutableList<ProductResponse>


    //init some variables for sorting option:
    //sort according to cost(decreasing)
    var costComparator = Comparator<ProductResponse> { rest1, rest2 ->
        rest1.price!!.compareTo(rest2.price!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //tool bar init
        initToolbarComponents()

        //get current category data from last page:
        loadCurrentCatData()

        //set up recycler view
        binding.rvMealdetailsMeals.layoutManager = LinearLayoutManager(this@CategoryDetailsActivity)
    }

    override fun onResume() {
        super.onResume()
        loadCategoryProducts()
    }

    //to initialize tool bar components:
    private fun initToolbarComponents() {
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
        binding.ivMealdetailsAddmenu.setOnClickListener {
            val intent = Intent(this@CategoryDetailsActivity, AddProductActivity::class.java)
            intent.putExtra(CATEGORY_DATA, currentCatResponse as Serializable)
            startActivity(intent)
        }

        //Sort recycler view button:
        binding.ivMealdetailsSortmenu.setOnClickListener {
            radioButtonView =
                View.inflate(this@CategoryDetailsActivity, R.layout.btn_sort_radio, null)
            AlertDialog.Builder(this@CategoryDetailsActivity)
                .setTitle("Sort By?")
                .setView(radioButtonView)
                .setPositiveButton("OK") { _, _ ->
                    if (radioButtonView.findViewById<RadioButton>(R.id.radio_high_to_low).isChecked) {
                        Collections.sort(productsList, costComparator)
                        productsList.reverse()
                        productAdapter.notifyDataSetChanged()
                    }
                    if (radioButtonView.findViewById<RadioButton>(R.id.radio_low_to_high).isChecked) {
                        Collections.sort(productsList, costComparator)
                        productAdapter.notifyDataSetChanged()
                    }
                    /*if (radioButtonView.findViewById<RadioButton>(R.id.radio_rating).isChecked) {
                        Collections.sort(productsList, ratingComparator)
                        productsList.reverse()

                    }*/

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
        val filteredList = arrayListOf<ProductResponse>()

        for (item in productsList) {
            if (item.name!!.lowercase(Locale.ROOT)
                    .contains(strTyped.lowercase(Locale.ROOT))
            ) {
                filteredList.add(item)
            }
        }

        if (filteredList.size == 0) {
            binding.errorSearch.errorNothingFound.visibility = View.VISIBLE
        } else {
            binding.errorSearch.errorNothingFound.visibility = View.GONE
        }

        //Adapter add it here:
        productAdapter.updateList(filteredList)

    }


    /**Handle API response errors*/
    private fun showErrorLayout(title:String,message:String){
        if(binding.errorLayout.errorLayout.visibility==View.GONE){
            binding.errorLayout.errorLayout.visibility=View.VISIBLE
        }
        binding.errorLayout.tvErrorTitle.text = title
        binding.errorLayout.tvErrorMessage.text = message

        binding.errorLayout.btnErrorRetry.setOnClickListener {
            loadCategoryProducts()
        }

    }

    //Get current category data from last page:
    private fun loadCurrentCatData(){
        val bundle:Bundle? = intent.extras
        if(bundle?.containsKey(CATEGORY_DATA)!!){
            currentCatResponse = intent.extras?.get(CATEGORY_DATA) as CategoryResponse
        }
    }

    /**
     * To load all category products from API
     * */
    private fun loadCategoryProducts() {
        binding.errorLayout.errorLayout.visibility=View.GONE
        binding.rvMealdetailsMeals.showShimmerAdapter()

        //Connect to db send current Category ID and get response List<ProductsResponse>
        val productService:ProductService = ApiClient(this@CategoryDetailsActivity).buildService(ProductService::class.java)
        val requestCall: Call<MutableList<ProductResponse>> = productService.getCurrentCatProducts(currentCatResponse.id.toString())
        requestCall.enqueue(object: Callback<MutableList<ProductResponse>> {
            override fun onResponse(call: Call<MutableList<ProductResponse>>,response: Response<MutableList<ProductResponse>>) {
                if(response.isSuccessful){
                    productsList = response.body()!!
                    productAdapter = ProductAdapter(this@CategoryDetailsActivity,productsList)
                    binding.rvMealdetailsMeals.adapter = productAdapter

                    if(productsList.size==0){
                        binding.rvMealdetailsMeals.hideShimmerAdapter()
                        showErrorLayout("WE ARE SORRY","No available products right now!")
                    }
                    binding.rvMealdetailsMeals.hideShimmerAdapter()
                }else{
                    binding.rvMealdetailsMeals.hideShimmerAdapter()
                    val errorCode:String = when(response.code()){
                        404 -> {
                            "404 not found"
                        }
                        500 -> {
                            "500 server broken"
                        }
                        else ->{
                            "Unknown error!"
                        }
                    }
                    showErrorLayout("ERROR",errorCode)
                }
            }

            override fun onFailure(call: Call<MutableList<ProductResponse>>, t: Throwable) {
                binding.rvMealdetailsMeals.hideShimmerAdapter()
                showErrorLayout("Oops...","Network failure, please try again\n $t")
            }

        })

    }

}