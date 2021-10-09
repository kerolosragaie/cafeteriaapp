package com.evapharma.cafeteriaapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.helpers.OffersAdapter
import com.evapharma.cafeteriaapp.models.ProductResponse
import com.evapharma.cafeteriaapp.services.ProductService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OffersFragment : Fragment() {
    lateinit var offerAdapter: OffersAdapter
    private lateinit var offersList:List<ProductResponse>
    lateinit var recyclerView: ShimmerRecyclerView
    private lateinit var errorLayout: ConstraintLayout
    private lateinit var fragContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragContext = container!!.context
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorLayout = view.findViewById(R.id.error_layout)
        recyclerView = view.findViewById(R.id.rv_offers)
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onResume() {
        super.onResume()
        callOffersAPI()
    }

    private fun callOffersAPI(){
        recyclerView.showShimmerAdapter()
        errorLayout.visibility=View.GONE

        val productService:ProductService = ApiClient(fragContext).buildService(ProductService::class.java)
        val requestCall : Call<List<ProductResponse>> = productService.getProductsInOffer()
        requestCall.enqueue(object : Callback<List<ProductResponse>> {
            override fun onResponse( call: Call<List<ProductResponse>>, response: Response<List<ProductResponse>>) {
                if(response.isSuccessful){
                    offersList = response.body()!!
                    offerAdapter = OffersAdapter(fragContext,offersList)
                    recyclerView.adapter=offerAdapter
                    recyclerView.hideShimmerAdapter()
                    if(offersList.isEmpty()){
                        recyclerView.hideShimmerAdapter()
                        showErrorLayout("WE ARE SORRY","No available offers right now!")
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

            override fun onFailure(call: Call<List<ProductResponse>>, t: Throwable) {
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
            callOffersAPI()
        }
    }

}