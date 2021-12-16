package com.ayush.livesearch

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    final val BASE_URL = "https://daipsi.com/Android_App_Daipsi/demo.php/"

    var retorfit:Retrofit?=null

    fun getApiClient():Retrofit{
        if(retorfit==null){
            retorfit=Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retorfit as Retrofit
    }
}