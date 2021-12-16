package com.ayush.livesearch

import com.digitalhain.daipsisearch.Activities.DataModal
import com.digitalhain.daipsisearch.Activities.Subject
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface ApiInterface {
    @GET("demo.php")
    fun getQuestions(@Query("key") keyword:String,@Query("course") course:String):Call<List<Subject>>

    @POST("demo.php")
    fun sendQuestion(@Query("course") course: String,@Query("question") question:String,@Query("askedby") askedby:String,callback: Callback<Response>)

    @FormUrlEncoded
    @POST("demo.php")
    fun  //on below line we are creating a method to post our data.
            createPost(@Body dataModal: DataModal?): Call<DataModal?>?
}