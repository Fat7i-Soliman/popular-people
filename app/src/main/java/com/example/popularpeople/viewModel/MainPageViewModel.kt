package com.example.popularpeople.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.popularpeople.models.Person
import com.example.popularpeople.models.Root
import com.example.popularpeople.network.MovieApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPageViewModel :ViewModel(){

    private val api_key = "14c57781e6109693571817381184b891"
    private var _root=MutableLiveData<Root>()
    val root : LiveData<Root>
    get() = _root

    private var _persons=MutableLiveData<List<Person>>()
    val persons : LiveData<List<Person>>
        get() = _persons

    init {
        getPeople()
    }

    private fun getPeople(){
        Log.d("MainPageViewModel","getPeople")

        MovieApi.retrofitService.getObjects(api_key ,1).enqueue(object :Callback<Root>{
            override fun onFailure(call: Call<Root>, t: Throwable) {
                Log.d("MainPageViewModel","${t.message}")
            }

            override fun onResponse(call: Call<Root>, response: Response<Root>) {
                Log.d("MainPageViewModel","onResponse")
                _root.value = response.body()
                _persons.value= _root.value?.results

            }

        })
    }
}