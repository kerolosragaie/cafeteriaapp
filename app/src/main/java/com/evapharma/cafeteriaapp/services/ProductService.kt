package com.evapharma.cafeteriaapp.services


import com.evapharma.cafeteriaapp.models.ProductRequest
import com.evapharma.cafeteriaapp.models.ProductResponse
import retrofit2.Call
import retrofit2.http.*

interface ProductService {
    /**
     * Get all products
     *@param category can get all products with current category name
     * */
    @GET("Product/getAllProducts")
    fun getProducts(@Query("category") category:String?=null) : Call<List<ProductResponse>>

    /**
     * Get all products using category id
     * @param categoryId ex: currentCategory.id
     * */
    @GET("Product/getProducts")
    fun getCurrentCatProducts(@Query("categoryId") categoryId:String) : Call<MutableList<ProductResponse>>

    /**Get single product by ID*/
    @GET("Product/getProduct/{id}")
    fun getSingleProduct(@Path("id") id:Int): Call<ProductResponse>

    /**Create new Product*/
    @POST("Product/createProduct")
    fun createProduct(@Body productRequest: ProductRequest): Call<ProductResponse>

    /**Update category by id*/
    @PUT("Product/updateProduct")
    fun updateProduct(@Query("id") id:Int,@Body productRequest: ProductRequest):Call<Unit>

    /**Delete category by id*/
    @DELETE("Product/deleteProduct")
    fun deleteProduct(@Query("id") id:Int):Call<Unit>
}