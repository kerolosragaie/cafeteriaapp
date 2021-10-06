package com.evapharma.cafeteriaapp.helpers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.evapharma.cafeteriaapp.CATEGORY_DATA
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.activities.CategoryDetailsActivity
import com.evapharma.cafeteriaapp.activities.UpdateDeleteCategoryActivity
import com.evapharma.cafeteriaapp.models.CategoryResponse
import java.io.Serializable
import java.util.ArrayList


class CategoryAdapter(val context:Context, private var categoryList:List<CategoryResponse>) :
    RecyclerView.Adapter<CategoryAdapter.MealsViewHolder>(){

        inner class MealsViewHolder(view: View):RecyclerView.ViewHolder(view){
            val imageView: ImageView = view.findViewById(R.id.iv_meals_image)
            val categoryName: TextView = view.findViewById(R.id.tv_meals_name)
            val editImage: ImageView = view.findViewById(R.id.iv_meals_edit)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.meals_item, parent, false)
        return MealsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealsViewHolder, position: Int) {
        val singleCategory = categoryList[position]
        holder.categoryName.text = singleCategory.name

        //go to edit page:
        holder.editImage.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateDeleteCategoryActivity::class.java)
            intent.putExtra(CATEGORY_DATA, singleCategory as Serializable)
            startActivity(holder.itemView.context,intent,null)
        }

        //show loading for glide:
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(holder.imageView)
            .load(singleCategory.imageUrl)
            .placeholder(circularProgressDrawable)
            .into(holder.imageView)


        //When press on card item:
        holder.itemView.setOnClickListener {
            //go to page to view list of foods in this menu:
            val intent = Intent(holder.itemView.context, CategoryDetailsActivity::class.java)
            intent.putExtra(CATEGORY_DATA, singleCategory as Serializable)
            startActivity(holder.itemView.context,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    /**
     * For search functionality:
     * Added here to access main list which works on it
     * */
    fun updateList(filteredList: ArrayList<CategoryResponse>) {
        //ADD it to the Meals Fragment, like food list
        categoryList = filteredList
        notifyDataSetChanged()
    }

}