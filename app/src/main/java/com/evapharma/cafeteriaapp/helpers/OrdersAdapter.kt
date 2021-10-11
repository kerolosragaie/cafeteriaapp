package com.evapharma.cafeteriaapp.helpers

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.evapharma.cafeteriaapp.ORDER_DATA
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.activities.OrderDetailsActivity
import com.evapharma.cafeteriaapp.models.Order
import java.io.Serializable
import java.time.LocalDateTime


class OrdersAdapter(private val orders: List<Order>) :
    RecyclerView.Adapter<OrderViewHolder>() {

    private lateinit var localDateTime: LocalDateTime

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val singleOrder: Order = orders[position]
        holder.orderID.text = singleOrder.id.toString()
        localDateTime = LocalDateTime.parse(singleOrder.orderDate)
        holder.orderTime.text = "${localDateTime.hour}: ${localDateTime.minute}/ ${localDateTime.dayOfMonth}-${localDateTime.monthValue}-${localDateTime.year}"
        //TODO: waiting API to send back user name:
        //holder.userID.text = singleOrder.userId

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, OrderDetailsActivity::class.java)
            intent.putExtra(ORDER_DATA, singleOrder as Serializable)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }

    }

}

class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var orderID: TextView = view.findViewById(R.id.tv_home_orderid)
    var orderTime: TextView = view.findViewById(R.id.tv_home_ordertime)
    var userID: TextView = view.findViewById(R.id.tv_home_userid)
}
