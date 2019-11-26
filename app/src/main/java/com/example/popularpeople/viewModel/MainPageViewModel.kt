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

    private var _persons=MutableLiveData<MutableList<Person>>()
    val persons : LiveData<MutableList<Person>>
        get() = _persons

    var newPersons= MutableLiveData<MutableList<Person>>()
    private var _page= MutableLiveData<Int>()
    private var _totalPages= MutableLiveData<Int>()

    init {
        getPeople()
    }

    private fun getPeople(){
        Log.d("MainPageViewModel","getPeople")
        _page.value = 1
        MovieApi.retrofitService.getObjects(api_key ,_page.value!!).enqueue(object :Callback<Root>{
            override fun onFailure(call: Call<Root>, t: Throwable) {
                Log.d("MainPageViewModel","${t.message}")
            }

            override fun onResponse(call: Call<Root>, response: Response<Root>) {
                _root.value = response.body()
                _totalPages.value= _root.value!!.total_pages
                _persons.value= _root.value?.results

            }

        })
    }

    fun nextPage(){
        _page.value =_page.value!!.plus(1)

        if (_page.value!! <= _totalPages.value!!) {
            MovieApi.retrofitService.getObjects(api_key, _page.value!!).enqueue(object : Callback<Root> {
                override fun onFailure(call: Call<Root>, t: Throwable) {
                    Log.d("MainPageViewModel", "${t.message}")
                }

                override fun onResponse(call: Call<Root>, response: Response<Root>) {

                    val body = response.body()
                    newPersons.value = body!!.results

                }

            })
        }
    }
}