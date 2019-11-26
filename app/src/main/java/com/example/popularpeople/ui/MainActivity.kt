package com.example.popularpeople.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.popularpeople.R
import com.example.popularpeople.adapters.PersonsAdapter
import com.example.popularpeople.databinding.ActivityMainBinding
import com.example.popularpeople.network.Connection
import com.example.popularpeople.viewModel.MainPageViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainPageViewModel
    private lateinit var adapter: PersonsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.title= "Home"

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.popularRecycler.layoutManager = GridLayoutManager(this,2)

        if (Connection().verifyAvailableNetwork(this as AppCompatActivity)){
            viewModel= ViewModelProviders.of(this)[MainPageViewModel::class.java]
            viewModel.persons.observe(this, Observer {
                if (it!=null){
                    binding.loadingSpinner.visibility= View.INVISIBLE
                    adapter= PersonsAdapter(it,this)
                    binding.popularRecycler.adapter = adapter
                }else{
                    Toast.makeText(this,"empty",Toast.LENGTH_SHORT).show()
                    binding.loadingSpinner.visibility= View.INVISIBLE
                }
            })
        }else{
            binding.loadingSpinner.visibility= View.INVISIBLE
            binding.emptyText.text = getString(R.string.no_connection)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search,menu)
        val searchView = menu!!.findItem(R.id.search).actionView as SearchView
        searchView.queryHint="Find artist"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0!=null&&p0.isNotEmpty()){
                    sendToResults(p0)
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
}
