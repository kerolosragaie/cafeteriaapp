package com.evapharma.cafeteriaapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.helpers.OrdersAdapter
import com.evapharma.cafeteriaapp.orders


class HomeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tv_orders_ordercount).apply{
            text=text.toString()+" "+orders.size.toString()
        }
        val adapter = OrdersAdapter(orders)
        val recycler = view.findViewById<RecyclerView>(R.id.rv_orders_orders)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

}