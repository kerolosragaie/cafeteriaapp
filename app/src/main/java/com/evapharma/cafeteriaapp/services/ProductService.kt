package com.evapharma.cafeteriaapp.services


import com.evapharma.cafeteriaapp.models.ProductRequest
import com.evapharma.cafeteriaapp.models.ProductResponse
import retrofit2.Call
import retrofit2.http.*

interface ProductService {
    /**
     * Get all products which are in offer
     * */
    @GET("Product/getOffers")
    fun getProductsInOffer() : Call<List<ProductResponse>>

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

    /**Update product by id*/
    @PUT("Product/updateProduct")
    fun updateProduct(@Query("id") id:Int,@Body productRequest: ProductRequest):Call<Unit>

    /**Delete product by id*/
    @DELETE("Product/deleteProduct")
    fun deleteProduct(@Query("id") id:Int) : Call<Unit>
}