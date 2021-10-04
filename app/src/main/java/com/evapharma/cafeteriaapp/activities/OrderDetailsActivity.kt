package com.evapharma.cafeteriaapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.helpers.OrderDetailsItemsAdapter
import com.evapharma.cafeteriaapp.helpers.OrdersAdapter
import com.evapharma.cafeteriaapp.models.OrderDetailsItem
import com.evapharma.cafeteriaapp.shortToast
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.evapharma.cafeteriaapp.orderDetailsList

class OrderDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        findViewById<CollapsingToolbarLayout>(R.id.ct_orderdetails).apply{

            setExpandedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColor);
            setCollapsedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColor);
        }

        findViewById<ImageView>(R.id.btn_orderdetails_back).setOnClickListener{
             finish()
            }
        val adapter = OrderDetailsItemsAdapter(orderDetailsList)
        val recycler = findViewById<RecyclerView>(R.id.rv_orderdetails)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter


        var tv_total:TextView=findViewById<TextView>(R.id.tv_orderdetails_totalprice)
        tv_total.text= tv_total.text.toString() +"  "+ sumPrice(orderDetailsList).toString()

        }


    fun sumPrice(list:List<OrderDetailsItem>): Double {
        var total=0.0
        list.forEach {
            total+=it.price
        }
        return total
    }


}

