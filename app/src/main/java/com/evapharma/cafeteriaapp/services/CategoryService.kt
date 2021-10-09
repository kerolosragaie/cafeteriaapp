package com.evapharma.cafeteriaapp.services

import com.evapharma.cafeteriaapp.models.CategoryRequest
import com.evapharma.cafeteriaapp.models.CategoryResponse
import retrofit2.Call
import retrofit2.http.*

interface CategoryService {

    /**Get all categories*/
    @GET("Category/getCategories")
    @Streaming
    fun getCategories() : Call<List<CategoryResponse>>

    /*
    @GET("Category/getCategories")
    fun getCategories() : Call<List<CategoryResponse>>

    /**Get single category by ID*/
    @GET("Category/getCategory")
    fun getSingleCategory(@Query("id") id:Int):Call<CategoryResponse>*/

    /**Create new category*/
    @POST("Category/createCategory")
    fun createCategory(@Body categoryRequest: CategoryRequest):Call<CategoryResponse>

    /**Update category
     * @param id of category needed to be updated
     * @param categoryRequest category request model
     * */
    @PUT("Category/updateCategory")
    fun updateCategory(@Query("id") id:Int, @Body categoryRequest: CategoryRequest):Call<CategoryResponse>

    /**Delete category by id*/
    @DELETE("Category/deleteCategory")
    fun deleteCategory(@Query("id") id:Int) : Call<Unit>
}