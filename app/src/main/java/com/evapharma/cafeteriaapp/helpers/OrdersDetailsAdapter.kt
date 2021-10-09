package com.evapharma.cafeteriaapp.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.models.OrderItems


class OrderDetailsItemsAdapter(private val orderDetailsItems: List<OrderItems>) :
    RecyclerView.Adapter<OrderDetailsItemDetailsViewHolder>() {


    override fun getItemCount(): Int {
        return orderDetailsItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsItemDetailsViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
            val view: View = inflater.inflate(R.layout.order_details_item, parent, false)
            return  OrderDetailsItemDetailsViewHolder(view)
            }

    override fun onBindViewHolder(holder: OrderDetailsItemDetailsViewHolder, position: Int) {
        val singleOrderItemsDetails: OrderItems = orderDetailsItems[position]
        holder.itemName.text = singleOrderItemsDetails.product.name.toString()
        holder.itemQty.text = singleOrderItemsDetails.quantity.toString()
        holder.itemPrice.text = singleOrderItemsDetails.product.price.toString()
    }

}

class OrderDetailsItemDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var itemName: TextView = view.findViewById(R.id.tv_orderdetailsitem_itemname)
    var itemQty: TextView = view.findViewById(R.id.tv_orderdetailsitem_itemqty)
    var itemPrice: TextView = view.findViewById(R.id.tv_orderdetailsitem_itemprice)
}
