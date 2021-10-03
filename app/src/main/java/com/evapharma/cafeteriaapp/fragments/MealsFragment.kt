package com.evapharma.cafeteriaapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.helpers.MealsAdapter
import com.evapharma.cafeteriaapp.menusList


class MealsFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuAdapter = MealsAdapter(view.context,menusList)
        val recycler = view.findViewById<ShimmerRecyclerView>(R.id.rv_meals)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = menuAdapter
    }

}