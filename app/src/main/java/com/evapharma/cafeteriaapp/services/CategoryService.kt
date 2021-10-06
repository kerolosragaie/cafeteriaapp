package com.evapharma.cafeteriaapp.services

import com.evapharma.cafeteriaapp.models.CategoryRequest
import com.evapharma.cafeteriaapp.models.CategoryResponse
import retrofit2.Call
import retrofit2.http.*

interface CategoryService {

    /**Get all categories*/
    @GET("Category/getCategories")
    fun getCategories() : Call<List<CategoryResponse>>

    /**Get single category by ID*/
    @GET("Category/getCategory/{id}")
    fun getSingleCategory(@Path("id") id:Int):Call<CategoryResponse>

    /**Create new category*/
    @POST("Category/createCategory")
    fun createCategory(@Body categoryRequest: CategoryRequest):Call<CategoryResponse>

    /**Update category by id*/
    @PUT("Category/updateCategory/{id}")
    fun updateCategory(@Path("id") id:Int):Call<CategoryResponse>

    /**Delete category by id*/
    @DELETE("Category/deleteCategory/{id}")
    fun deleteCategory(@Path("id") id:Int)
}