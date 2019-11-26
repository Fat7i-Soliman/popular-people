package com.example.popularpeople.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.popularpeople.models.ObjectPics
import com.example.popularpeople.models.PersonDetails
import com.example.popularpeople.models.Pics
import com.example.popularpeople.network.MovieApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(val id:Int) : ViewModel(){

    private val api_key = "14c57781e6109693571817381184b891"
    private var _details= MutableLiveData<PersonDetails>()
    val details : LiveData<PersonDetails>
        get() = _details

    private var _object= MutableLiveData<ObjectPics>()

    private var _pics=MutableLiveData<List<Pics>>()
    val pics : LiveData<List<Pics>>
        get() = _pics

    init {
        getDetails()
        getPics()
    }

    private fun getPics() {
        MovieApi.retrofitService.getPhotos(id,api_key).enqueue(object: Callback<ObjectPics>{
            override fun onFailure(call: Call<ObjectPics>, t: Throwable) {
                Log.d("DetailsViewModel","${t.message}")

            }

            override fun onResponse(call: Call<ObjectPics>, response: Response<ObjectPics>) {
                _object.value=response.body()
                _pics.value=_object.value!!.profiles
                Log.d("DetailsViewModel","onResponse of pics")

            }

        })
    }

    private fun getDetails(){
        MovieApi.retrofitService.getDetails(id,api_key).enqueue(object: Callback<PersonDetails>{
            override fun onFailure(call: Call<PersonDetails>, t: Throwable) {
                Log.d("DetailsViewModel","${t.message}")

            }

            override fun onResponse(call: Call<PersonDetails>, response: Response<PersonDetails>) {
                Log.d("DetailsViewModel","onResponse of detail")

                _details.value = response.body()
            }

        })
    }
}