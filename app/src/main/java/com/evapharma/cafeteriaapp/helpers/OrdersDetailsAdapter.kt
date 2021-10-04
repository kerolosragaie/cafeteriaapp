package com.evapharma.cafeteriaapp.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.models.OrderDetailsItem


class OrderDetailsItemsAdapter(private val OrderDetailsItems: MutableList<OrderDetailsItem>) :
    RecyclerView.Adapter<OrderDetailsItemDetailsViewHolder>() {

    fun updateData(newOrderDetailsItems: List<OrderDetailsItem>){
        OrderDetailsItems.clear()
        OrderDetailsItems.addAll(newOrderDetailsItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return OrderDetailsItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsItemDetailsViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
            val view: View = inflater.inflate(R.layout.order_details_item, parent, false)
            return  OrderDetailsItemDetailsViewHolder(view).apply {
                   //click listeners
                }
            }

    override fun onBindViewHolder(holder: OrderDetailsItemDetailsViewHolder, position: Int) {
        val OrderDetailsItem: OrderDetailsItem = OrderDetailsItems[position]
        holder.item_name.text = OrderDetailsItem.item_name.toString()
        holder.itemqty.text = OrderDetailsItem.qty.toString()
        holder.itemprice.text = OrderDetailsItem.price.toString()
    }

}

class OrderDetailsItemDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var item_name: TextView = view.findViewById(R.id.tv_orderdetailsitem_itemname)
    var itemqty: TextView = view.findViewById(R.id.tv_orderdetailsitem_itemqty)
    var itemprice: TextView = view.findViewById(R.id.tv_orderdetailsitem_itemprice)
}
