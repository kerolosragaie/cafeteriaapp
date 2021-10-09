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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.helpers.OrdersAdapter
import com.evapharma.cafeteriaapp.models.Order
import com.evapharma.cafeteriaapp.services.OrderService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener{

    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvRefresher:SwipeRefreshLayout
    private lateinit var totalNumOrders:TextView
    private lateinit var ordersList:List<Order>
    private lateinit var errorLayout: ConstraintLayout
    private lateinit var fragContext: Context


    override fun onResume() {
        super.onResume()
        onLoadingSwipeRefresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragContext = container!!.context
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorLayout = view.findViewById(R.id.error_layout)
        rvRefresher = view.findViewById(R.id.rv_orders_refresher)
        rvRefresher.setOnRefreshListener(this@HomeFragment)
        recyclerView = view.findViewById(R.id.rv_orders_orders)
        totalNumOrders = view.findViewById(R.id.tv_orders_ordercount)
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onRefresh() {
        callAPI()
    }

    private fun onLoadingSwipeRefresh(){
        rvRefresher.post { callAPI() }
    }

    //to call api:
    private fun callAPI(){
        rvRefresher.isRefreshing=true
        errorLayout.visibility=View.GONE

        val orderService:OrderService = ApiClient(fragContext).buildService(OrderService::class.java)
        val requestCall:Call<List<Order>> = orderService.getAllOrders()

        requestCall.enqueue(object :Callback<List<Order>>{
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if(response.isSuccessful){
                    rvRefresher.isRefreshing=false
                    ordersList = response.body()!!
                    ordersAdapter = OrdersAdapter(ordersList)
                    recyclerView.adapter = ordersAdapter
                    if(ordersList.isEmpty()){
                        showErrorLayout("WE ARE SORRY","No available orders right now!")
                    }
                    totalNumOrders.text = "TOTAL ORDERS: "+ordersList.size.toString()
                } else {
                    rvRefresher.isRefreshing=false
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

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                totalNumOrders.text = "TOTAL ORDERS: 0"
                rvRefresher.isRefreshing=false
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
            onLoadingSwipeRefresh()
        }
    }

}