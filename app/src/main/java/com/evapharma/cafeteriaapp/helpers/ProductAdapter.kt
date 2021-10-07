package com.evapharma.cafeteriaapp.helpers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.evapharma.cafeteriaapp.PRODUCT_DATA
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.activities.UpdateDeleteProductActivity
import com.evapharma.cafeteriaapp.models.ProductResponse
import java.io.Serializable
import java.util.ArrayList

class ProductAdapter(val context: Context, private var productsList:List<ProductResponse>)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

        inner class ProductViewHolder(view: View): RecyclerView.ViewHolder(view){
            val imageView: ImageView = view.findViewById(R.id.iv_food_image)
            val foodName: TextView = view.findViewById(R.id.tv_food_name)
            val description: TextView = view.findViewById(R.id.tv_food_description)
            val rating: TextView = view.findViewById(R.id.tv_food_rating)
            val price: TextView = view.findViewById(R.id.tv_food_price)
            val editImage: ImageView = view.findViewById(R.id.iv_food_edit)
            val offerImage: ImageView = view.findViewById(R.id.iv_food_offer)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val singleProduct : ProductResponse = productsList[position]

        holder.foodName.text = singleProduct.name
        holder.description.text = singleProduct.description
        holder.rating.text = "singleProduct.rate, need to be added by DB"
        holder.price.text = "${singleProduct.price} LE"

        //go to edit page:
        holder.editImage.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateDeleteProductActivity::class.java)
            intent.putExtra(PRODUCT_DATA, singleProduct as Serializable)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }

        if(!singleProduct.inOffers!!){
            holder.offerImage.visibility=View.GONE
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
        return productsList.size
    }

    /**
     * For search functionality:
     * */
    fun updateList(filteredList: ArrayList<ProductResponse>) {
        productsList = filteredList
        notifyDataSetChanged()
    }

}