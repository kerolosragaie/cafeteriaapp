package com.evapharma.cafeteriaapp.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.models.Order


class OrdersAdapter(private val orders: MutableList<Order>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateData(newOrders: List<Order>){
        orders.clear()
        orders.addAll(newOrders)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return orders.size
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)


            val view: View = inflater.inflate(R.layout.order_item, parent, false)
            return  OrderViewHolder(view).apply {
                    orderview.setOnClickListener {
                        Toast.makeText(it.context, "item tapped on", Toast.LENGTH_LONG).show()
                    }
                }
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OrderViewHolder -> {
                val order: Order = orders[position]
                holder.orderID.text = order.orderID.toString()
                holder.employeeName.text = order.employeeName
                holder.employeeDepartment.text = order.employeeDepartment
            }
        }
    }

}


class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var orderview:ConstraintLayout = view.findViewById(R.id.view_home_orderview)
    var orderID: TextView = view.findViewById(R.id.tv_home_orderid)
    var employeeName: TextView = view.findViewById(R.id.tv_home_empname)
    var employeeDepartment: TextView = view.findViewById(R.id.tv_home_depname)
}

/*
class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var title: TextView = view.findViewById(R.id.tv_Titlw)
    var icon: ImageView = view.findViewById(R.id.iv_icon)
}

*/
