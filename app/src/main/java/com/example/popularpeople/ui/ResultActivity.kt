package com.example.popularpeople.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.popularpeople.R
import com.example.popularpeople.adapters.PersonsAdapter
import com.example.popularpeople.databinding.ActivityResultBinding
import com.example.popularpeople.network.Connection
import com.example.popularpeople.viewModel.ResultViewModel

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultViewModel
    private lateinit var adapter: PersonsAdapter
    private lateinit var layoutManager: GridLayoutManager
    private var loading = true
    var pastVisiblesItems:Int =0
    var visibleItemCount :Int=0
    var totalItemCount :Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.title= "Results"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_result)

        layoutManager=GridLayoutManager(this,2)
        binding.popularRecycler.layoutManager = layoutManager

        val query = intent.getStringExtra("Query")

        if (Connection().verifyAvailableNetwork(this as AppCompatActivity)){
            viewModel= ViewModelProviders.of(this)[ResultViewModel::class.java]
            viewModel.getResults(query)

            viewModel._persons.observe(this, Observer {
                if (it.isNotEmpty()&&it!=null){
                    binding.loadingSpinner.visibility= View.INVISIBLE
                    adapter= PersonsAdapter(it,this)
                    binding.popularRecycler.adapter = adapter
                }else{
                    binding.loadingSpinner.visibility= View.INVISIBLE
                    binding.emptyText.text = "No results"

                }
            })

            // adding more people in list
            viewModel.newPersons.observe(this, Observer {
                if (it.isNotEmpty()&&it!=null){
                    adapter.addMore(it)
                }

                })
        }else{
            binding.loadingSpinner.visibility= View.INVISIBLE
            binding.emptyText.text = getString(R.string.no_connection)
        }


        // infinity scrolling
        loadMore()
    }



    private fun loadMore(){

        binding.popularRecycler.addOnScrollListener(object:RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    loading = true
                    Log.d("ResultViewModel", "$loading")
                }


            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    visibleItemCount= layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisiblesItems= layoutManager.findFirstVisibleItemPosition()

                    if (loading){
                        if (loading&&(visibleItemCount+pastVisiblesItems)>=totalItemCount){
                            loading=false
                            Log.d("ResultViewModel", "at bottom")

                            // get next page
                            viewModel.nextPage()
                        }
                    }
                }

        })
    }
}
