package com.example.popularpeople.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.popularpeople.R
import com.example.popularpeople.adapters.PersonsAdapter
import com.example.popularpeople.databinding.ActivityMainBinding
import com.example.popularpeople.network.Connection
import com.example.popularpeople.viewModel.MainPageViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainPageViewModel
    private lateinit var adapter: PersonsAdapter
    private lateinit var layoutManager: GridLayoutManager
    private var loading = true
    var pastVisiblesItems:Int =0
    var visibleItemCount :Int=0
    var totalItemCount :Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.title= "Home"

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        layoutManager=GridLayoutManager(this,2)
        binding.popularRecycler.layoutManager = layoutManager

        if (Connection().verifyAvailableNetwork(this as AppCompatActivity)){
            viewModel= ViewModelProviders.of(this)[MainPageViewModel::class.java]
            viewModel.persons.observe(this, Observer {
                if (it!=null){
                    binding.loadingSpinner.visibility= View.INVISIBLE
                    adapter= PersonsAdapter(it ,this)
                    binding.popularRecycler.adapter = adapter
                }else{
                    Toast.makeText(this,"empty",Toast.LENGTH_SHORT).show()
                    binding.loadingSpinner.visibility= View.INVISIBLE
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

        loadMore()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search,menu)
        val searchView = menu!!.findItem(R.id.search).actionView as SearchView
        searchView.queryHint="Find artist"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0!=null&&p0.isNotEmpty()){
                    sendToResults(p0)
                }else{
                    Toast.makeText(applicationContext,"type a name !!",Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    fun sendToResults(query:String){
        val intent = Intent(this,ResultActivity::class.java)
        intent.putExtra("Query",query)
        startActivity(intent)
    }

    private fun loadMore(){

        binding.popularRecycler.addOnScrollListener(object: RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
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
