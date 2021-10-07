package com.evapharma.cafeteriaapp.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.models.ProductResponse

class OffersAdapter(val context: Context, private val offeredProducts: List<ProductResponse>)
    : RecyclerView.Adapter<OffersAdapter.ProductViewHolder>(){
    inner class ProductViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.iv_offer_image)
        val offerTitle: TextView = view.findViewById(R.id.tv_offer_title)
        //val offerTime:TextView = view.findViewById(R.id.tv_offer_timeleft)
        val editOffer:FrameLayout = view.findViewById(R.id.fl_offer_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.offer_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val singleProduct : ProductResponse = offeredProducts[position]
        holder.offerTitle.text = singleProduct.name
        holder.editOffer.setOnClickListener {
            //TODO: go to page which update/delete product
        }

        //show loading for glide:
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(holder.imageView)
            .load(singleProduct.imageUrl)
            .placeholder(circularProgressDrawable)
            .into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return offeredProducts.size
    }
}