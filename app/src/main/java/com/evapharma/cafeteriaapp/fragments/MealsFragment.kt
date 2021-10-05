package com.evapharma.cafeteriaapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.helpers.MealsAdapter
import com.evapharma.cafeteriaapp.menusList
import com.evapharma.cafeteriaapp.models.Menu
import java.util.*


class MealsFragment: Fragment() {
    lateinit var menuAdapter:MealsAdapter
    lateinit var recyclerView: ShimmerRecyclerView
    lateinit var errorLayout:CardView
    lateinit var etSearchForMenus:EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorLayout = view.findViewById(R.id.error_meals)
        etSearchForMenus = view.findViewById(R.id.et_meals_search)
        recyclerView = view.findViewById(R.id.rv_meals_category)
        recyclerView.layoutManager = LinearLayoutManager(context)

        etSearchForMenus.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(strTyped: Editable?) {
                searchFun(strTyped.toString())
            }

        })

        //TODO: connect to Database to get list of menus: (use on resume)
        menuAdapter = MealsAdapter(view.context,menusList)
        recyclerView.adapter = menuAdapter

    }

    override fun onResume() {
        super.onResume()
        //recyclerView.showShimmerAdapter()
        //recyclerView.hideShimmerAdapter()
    }

    /**
     * A function to search in the adapter without connecting to DB
     * */
    private fun searchFun(strTyped: String) {
        val filteredList = arrayListOf<Menu>()

        for (item in menusList) {
            if (item.name.lowercase(Locale.ROOT)
                    .contains(strTyped.lowercase(Locale.ROOT))
            ) {
                filteredList.add(item)
            }
        }

        if (filteredList.size == 0) {
            errorLayout.visibility=View.VISIBLE
        } else {
            errorLayout.visibility=View.GONE
        }

        menuAdapter.updateList(filteredList)

    }

}