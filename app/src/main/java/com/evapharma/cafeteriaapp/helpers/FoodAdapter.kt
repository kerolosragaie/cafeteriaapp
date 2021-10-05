package com.evapharma.cafeteriaapp.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.models.FoodItem
import id.ionbit.ionalert.IonAlert
import java.util.ArrayList

class FoodAdapter(val context: Context, private var foodList:MutableList<FoodItem>)
    : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>(){

        inner class FoodViewHolder(view: View): RecyclerView.ViewHolder(view){
            val imageView: ImageView = view.findViewById(R.id.iv_food_image)
            val foodName: TextView = view.findViewById(R.id.tv_food_name)
            val description: TextView = view.findViewById(R.id.tv_food_description)
            val rating: TextView = view.findViewById(R.id.tv_food_rating)
            val price: TextView = view.findViewById(R.id.tv_food_price)
            val deleteImage: ImageView = view.findViewById(R.id.iv_food_delete)
            val editImage: ImageView = view.findViewById(R.id.iv_food_edit)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_item, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val singleItem : FoodItem = foodList[position]

        holder.foodName.text = singleItem.name
        holder.description.text = singleItem.description
        holder.rating.text = singleItem.rate.toString()
        holder.price.text = singleItem.price.toString()

        //WHEN PRESS DELETE:
        holder.deleteImage.setOnClickListener {
            IonAlert(context, IonAlert.WARNING_TYPE)
                .setTitleText("WARNING")
                .setContentText("Are you sure you want to delete this item?")
                .setConfirmText("Yes")
                .setCancelText("No")
                .setConfirmClickListener {
                    //on press yes delete it
                }.show()
        }

        //go to edit page:
        holder.editImage.setOnClickListener {
            //TODO: make edit page and inside it make (update and delete buttons)
        }

        //show loading for glide:
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(holder.imageView)
            .load(singleItem.imageUrl)
            .placeholder(circularProgressDrawable)
            .into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return foodList.size
    }



}