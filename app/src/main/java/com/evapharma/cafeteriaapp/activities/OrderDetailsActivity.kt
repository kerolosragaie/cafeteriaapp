package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.evapharma.cafeteriaapp.ORDER_DATA
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.api.SessionManager
import com.evapharma.cafeteriaapp.databinding.ActivityOrderDetailsBinding
import com.evapharma.cafeteriaapp.helpers.OrderDetailsItemsAdapter
import com.evapharma.cafeteriaapp.models.*
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.evapharma.cafeteriaapp.services.OrderService
import id.ionbit.ionalert.IonAlert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailsBinding

    //to show or hide loading:
    private lateinit var loadingDialog : IonAlert

    //Current order
    private lateinit var orderService:OrderService
    private lateinit var currentOrderData: Order
    private lateinit var currentOrderItems:List<OrderItems>
    private lateinit var orderDetailsItemsAdapter:OrderDetailsItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = IonAlert(this@OrderDetailsActivity, IonAlert.PROGRESS_TYPE)
            .setSpinColor("#053776").setSpinKit("ThreeBounce")

        orderService= ApiClient(this@OrderDetailsActivity).buildService(OrderService::class.java)

        initCollapsingToolbar()
        initButtons()
        initAppBar()
        loadCurrentOrderData()
    }

    private fun initCollapsingToolbar(){
        binding.ctOrderdetails.apply{
            setExpandedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColor);
            setCollapsedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColor);
        }
    }
    private fun initButtons(){
        binding.btnOrderdetailsBack.setOnClickListener{
            finish()
        }
        binding.btnOrderdetailsOrderready.setOnClickListener {
            orderIsReady()
        }

    }
    private fun initAppBar(){
        binding.appbarOrderdetails.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val btnready:Button=binding.btnOrderdetailsOrderready
            if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                //  Collapsed
                btnready.visibility= View.INVISIBLE
            } else {
                //Expanded
                btnready.visibility= View.VISIBLE
            }
        })
    }



    //load order data on start page:
    private fun loadCurrentOrderData(){
        val bundle:Bundle? = intent.extras
        if(bundle?.containsKey(ORDER_DATA)!!){
            currentOrderData = intent.extras?.get(ORDER_DATA) as Order
        }
        binding.ctOrderdetails.title = "Order #${currentOrderData.id}"
        getCurrentOrderItems()
    }

    //set order ready on click:
    private fun orderIsReady(){
        loadingDialog.show()
        val orderRequest=OrderRequest(isReady=true)
        val requestCall: Call<Unit> = orderService.orderIsReady(currentOrderData.id,orderRequest)
        requestCall.enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    loadingDialog.dismiss()
                    IonAlert(this@OrderDetailsActivity, IonAlert.SUCCESS_TYPE)
                        .setTitleText("DONE")
                        .setConfirmClickListener {
                            finish()
                        }
                        .show()
                }else{
                    loadingDialog.dismiss()
                    val errorCode:Any = when(response.code()){
                        401 -> {
                            IonAlert(this@OrderDetailsActivity, IonAlert.ERROR_TYPE)
                                .setTitleText("ERROR")
                                .setContentText("401 unauthorized user, please login.")
                                .setConfirmClickListener {
                                    SessionManager(this@OrderDetailsActivity).deleteAccessToken()
                                    it.hide()
                                    finishAffinity()
                                    startActivity(Intent(this@OrderDetailsActivity, LoginActivity::class.java))
                                    Animatoo.animateSplit(this@OrderDetailsActivity)
                                }
                                .show()
                            return
                        }
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
                    IonAlert(this@OrderDetailsActivity, IonAlert.ERROR_TYPE)
                        .setTitleText("ERROR")
                        .setContentText(errorCode.toString())
                        .show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                loadingDialog.hide()
                IonAlert(this@OrderDetailsActivity, IonAlert.ERROR_TYPE)
                    .setTitleText("ERROR")
                    .setContentText("Something went wrong, $t")
                    .show()
            }
        })
    }

    //get current order items:
    private fun getCurrentOrderItems(){
        loadingDialog.show()
        binding.rvOrderdetails.layoutManager = LinearLayoutManager(this@OrderDetailsActivity)

        val requestCall:Call<List<OrderItems>> = orderService.getOrderItems(currentOrderData.id)
        requestCall.enqueue(object:Callback<List<OrderItems>>{
            override fun onResponse(call: Call<List<OrderItems>>, response: Response<List<OrderItems>>){
                if(response.isSuccessful){
                    currentOrderItems = response.body()!!
                    orderDetailsItemsAdapter = OrderDetailsItemsAdapter(currentOrderItems)
                    binding.rvOrderdetails.adapter = orderDetailsItemsAdapter
                    binding.tvOrderdetailsTotalprice.text = "Total price: "+sumPrice(currentOrderItems).toString()
                    loadingDialog.hide()
                }else{
                    loadingDialog.hide()
                    val errorCode:Any = when(response.code()){
                        401 -> {
                            IonAlert(this@OrderDetailsActivity, IonAlert.ERROR_TYPE)
                                .setTitleText("ERROR")
                                .setContentText("401 unauthorized user, please login.")
                                .setConfirmClickListener {
                                    SessionManager(this@OrderDetailsActivity).deleteAccessToken()
                                    it.hide()
                                    finishAffinity()
                                    startActivity(Intent(this@OrderDetailsActivity, LoginActivity::class.java))
                                    Animatoo.animateSplit(this@OrderDetailsActivity)
                                }
                                .show()
                            return
                        }
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
                    IonAlert(this@OrderDetailsActivity, IonAlert.ERROR_TYPE)
                        .setTitleText("ERROR")
                        .setContentText(errorCode.toString())
                        .show()
                }
            }

            override fun onFailure(call: Call<List<OrderItems>>, t: Throwable) {
                loadingDialog.hide()
                IonAlert(this@OrderDetailsActivity, IonAlert.ERROR_TYPE)
                    .setTitleText("ERROR")
                    .setContentText("Something went wrong, $t")
                    .show()
            }

        })
    }


    //To make summation to OrderItems
    private fun sumPrice(list:List<OrderItems>): Double {
        var total=0.0
        list.forEach {
            total+= it.product.price!! * it.quantity
        }
        return total
    }

}

