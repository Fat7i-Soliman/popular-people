package com.example.popularpeople.network

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import com.example.popularpeople.models.ObjectPics
import com.example.popularpeople.models.PersonDetails
import com.example.popularpeople.models.Pics
import com.example.popularpeople.models.Root
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val url = "https://api.themoviedb.org/3/"
private const val api_key = "14c57781e6109693571817381184b891"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(url)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface ApiService{

    @GET("person/popular")
    fun getObjects(@Query("api_key") key:String,@Query("page") page:Int ): Call<Root>

    @GET("person/{id}")
    fun getDetails(@Path("id") id:Int, @Query("api_key") key:String): Call<PersonDetails>

    @GET("person/{id}/images")
    fun getPhotos(@Path("id") id:Int, @Query("api_key") key:String): Call<ObjectPics>

    @GET("search/person")
    fun getSearch( @Query("api_key") key:String , @Query("query") txt:String,@Query("page") page:Int) : Call<Root>
}

object MovieApi{
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

class Connection{
    fun verifyAvailableNetwork(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}