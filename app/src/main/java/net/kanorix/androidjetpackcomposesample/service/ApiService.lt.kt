package net.kanorix.androidjetpackcomposesample.service

import net.kanorix.androidjetpackcomposesample.data.PatchRequest
import net.kanorix.androidjetpackcomposesample.data.PostRequest
import net.kanorix.androidjetpackcomposesample.data.PutRequest
import net.kanorix.androidjetpackcomposesample.data.ResponseData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    @GET("/get")
    suspend fun get(@Query("value1") value1: String, @Query("value2") value2: String): Response<ResponseData>

    @POST("/post")
    suspend fun post(@Body body: PostRequest): Response<Any>

    @PATCH("/patch")
    suspend fun patch(@Body body: PatchRequest): Response<Any>

    @PUT("/put")
    suspend fun put(@Body body: PutRequest): Response<Any>

    @DELETE("/delete")
    suspend fun delete(): Response<Any>
}