package com.evapharma.cafeteriaapp.helpers

import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.view.menu.MenuAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.models.Menu
import id.ionbit.ionalert.IonAlert
import java.util.ArrayList


class MealsAdapter(val context:Context,private var menuList:MutableList<Menu>) :
    RecyclerView.Adapter<MealsAdapter.MealsViewHolder>(){

        inner class MealsViewHolder(view: View):RecyclerView.ViewHolder(view){
            val imageView: ImageView = view.findViewById(R.id.iv_meals_image)
            val menuName: TextView = view.findViewById(R.id.tv_meals_name)
            val description: TextView = view.findViewById(R.id.tv_meals_description)
            val deleteImage: ImageView = view.findViewById(R.id.iv_meals_delete)
            val editImage: ImageView = view.findViewById(R.id.iv_meals_edit)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.meals_item, parent, false)
        return MealsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealsViewHolder, position: Int) {
        val singleMenu = menuList[position]
        holder.menuName.text = singleMenu.name
        holder.description.text = singleMenu.description

        //WHEN PRESS DELETE:
        holder.deleteImage.setOnClickListener {
            IonAlert(context, IonAlert.WARNING_TYPE)
                .setTitleText("WARNING")
                .setContentText("Are you sure you want to delete this menu?")
                .setConfirmText("Yes")
                .setCancelText("No")
                .setConfirmClickListener {
                    //on press yes delete it
                }
                .show()
        }

        //go to edit page:
        holder.editImage.setOnClickListener {
            //TODO: make edit page:
        }

        //show loading for glide:
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(holder.imageView)
            .load(singleMenu.imageUrl)
            .placeholder(circularProgressDrawable)
            .into(holder.imageView)
        //When press on card item:
        holder.itemView.setOnClickListener {
            //go to page to view list of foods in this menu:
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    /**
     * For search functionality:
     * Added here to access main list which works on it
     * */
    fun updateList(filteredList: ArrayList<Menu>,adapter: MealsAdapter) {
        menuList = filteredList
        adapter.notifyDataSetChanged()
    }

}