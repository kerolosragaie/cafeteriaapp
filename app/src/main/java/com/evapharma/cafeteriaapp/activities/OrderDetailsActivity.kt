package com.evapharma.cafeteriaapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.databinding.ActivityOrderDetailsBinding
import com.evapharma.cafeteriaapp.helpers.OrderDetailsItemsAdapter
import com.evapharma.cafeteriaapp.models.OrderDetailsItem
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.evapharma.cafeteriaapp.orderDetailsList


class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)




            initCollapsingToolbar()
            initButtons()
            initAppBar()
            initRecyclerView()
            initTotalPrice()
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
    private fun initRecyclerView(){
        val adapter = OrderDetailsItemsAdapter(orderDetailsList)
        val recycler = binding.rvOrderdetails
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun initTotalPrice(){
        val tvtotal:TextView=binding.tvOrderdetailsTotalprice
        tvtotal.text= tvtotal.text.toString() +"  "+ sumPrice(orderDetailsList).toString()
    }

    private fun sumPrice(list:List<OrderDetailsItem>): Double {
        var total=0.0
        list.forEach {
            total+=it.price
        }
        return total
    }


}

