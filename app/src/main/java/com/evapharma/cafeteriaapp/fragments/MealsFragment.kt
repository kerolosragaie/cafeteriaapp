package com.evapharma.cafeteriaapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.activities.AddCategoryActivity
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.helpers.CategoryAdapter
import com.evapharma.cafeteriaapp.models.CategoryResponse
import com.evapharma.cafeteriaapp.services.CategoryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.stream.Stream
import kotlin.streams.asSequence


class MealsFragment: Fragment(){
    lateinit var categoryAdapter:CategoryAdapter
    lateinit var recyclerView: ShimmerRecyclerView
    private lateinit var categoryList:List<CategoryResponse>
    private lateinit var errorSearch:View
    private lateinit var errorLayout: ConstraintLayout
    lateinit var etSearchForMenus:EditText
    private lateinit var fragContext:Context
    private lateinit var addNewCatImage:ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragContext = container!!.context
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meals, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorSearch = view.findViewById(R.id.error_search_category)
        errorLayout = view.findViewById(R.id.error_layout)
        etSearchForMenus = view.findViewById(R.id.et_meals_search)
        recyclerView = view.findViewById(R.id.rv_meals_category)
        recyclerView.layoutManager = LinearLayoutManager(context)
        addNewCatImage = view.findViewById(R.id.iv_meals_addmenu)

        etSearchForMenus.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(strTyped: Editable?) {
                searchFun(strTyped.toString())
            }

        })

        addNewCatImage.setOnClickListener {
            startActivity(Intent(view.context,AddCategoryActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        callAPI()
    }



    private fun callAPI(){
        recyclerView.showShimmerAdapter()
        errorLayout.visibility=View.GONE

        val categoryService:CategoryService = ApiClient(fragContext).buildService(CategoryService::class.java)
        val requestCall: Call<List<CategoryResponse>> = categoryService.getCategories()

        requestCall.enqueue(object: Callback<List<CategoryResponse>> {
            override fun onResponse(call: Call<List<CategoryResponse>>, response: Response<List<CategoryResponse>>) {
                if(response.isSuccessful){

                    categoryList = response.body()!!
                    categoryAdapter = CategoryAdapter(fragContext,categoryList)
                    recyclerView.adapter=categoryAdapter
                    recyclerView.hideShimmerAdapter()
                    if(categoryList.isEmpty()){
                        recyclerView.hideShimmerAdapter()
                        showErrorLayout("WE ARE SORRY","No available categories right now!")
                    }
                }else{
                    recyclerView.hideShimmerAdapter()
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

            override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
                recyclerView.hideShimmerAdapter()
                showErrorLayout("Oops...","Network failure, please try again\n $t")
            }

        })

    }

    /**Handle API response errors*/
    private fun showErrorLayout(title:String,message:String){
        if(errorLayout.visibility==View.GONE){
            errorLayout.visibility= View.VISIBLE
        }
        errorLayout.findViewById<TextView>(R.id.tv_error_title).text=title
        errorLayout.findViewById<TextView>(R.id.tv_error_message).text=message
        errorLayout.findViewById<Button>(R.id.btn_error_retry).setOnClickListener {
            callAPI()
        }
    }

    /**
     * A function to search in the adapter without connecting to DB
     * */
    private fun searchFun(strTyped: String) {
        val filteredList = arrayListOf<CategoryResponse>()

        for (item in categoryList) {
            if (item.name!!.lowercase(Locale.ROOT).contains(strTyped.lowercase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }

        if (filteredList.size == 0) {
            errorSearch.visibility=View.VISIBLE
        } else {
            errorSearch.visibility=View.GONE
        }

        categoryAdapter.updateList(filteredList)

    }

}