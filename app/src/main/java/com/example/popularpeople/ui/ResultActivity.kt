package com.example.popularpeople.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.popularpeople.R
import com.example.popularpeople.adapters.PersonsAdapter
import com.example.popularpeople.databinding.ActivityResultBinding
import com.example.popularpeople.network.Connection
import com.example.popularpeople.viewModel.ResultViewModel

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultViewModel
    private lateinit var adapter: PersonsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.title= "Results"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_result)

        binding.popularRecycler.layoutManager = GridLayoutManager(this,2)
        val query = intent.getStringExtra("Query")

        if (Connection().verifyAvailableNetwork(this as AppCompatActivity)){
            viewModel= ViewModelProviders.of(this)[ResultViewModel::class.java]
            viewModel.getResults(query)

            viewModel.persons.observe(this, Observer {
                if (it!=null){
                    binding.loadingSpinner.visibility= View.INVISIBLE
                    adapter= PersonsAdapter(it,this)
                    binding.popularRecycler.adapter = adapter
                }else{
                    binding.loadingSpinner.visibility= View.INVISIBLE
                    binding.emptyText.text = "No results"

                }
            })
        }else{
            binding.loadingSpinner.visibility= View.INVISIBLE
            binding.emptyText.text = getString(R.string.no_connection)
        }
    }
}
