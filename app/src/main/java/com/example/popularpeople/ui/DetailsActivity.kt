package com.example.popularpeople.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.popularpeople.R
import com.example.popularpeople.adapters.PicsAdapter
import com.example.popularpeople.databinding.ActivityDetailsBinding
import com.example.popularpeople.viewModel.DetailsViewModel
import com.example.popularpeople.viewModel.DetailsViewModelFactory
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var adapter: PicsAdapter
    private lateinit var viewModel: DetailsViewModel
    private lateinit var factory: DetailsViewModelFactory
    private var id:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_details)

        supportActionBar!!.title= "Details"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        id = intent.extras?.getInt("id")

        factory = DetailsViewModelFactory(id!!)
        viewModel = ViewModelProviders.of(this,factory)[DetailsViewModel::class.java]
        binding.picsRecycler.layoutManager = GridLayoutManager(this,4)


        viewModel.details.observe(this, Observer {
            if (it!=null){
                binding.name.text = it.name
                binding.bio.text= it.biography
                binding.birthDay.text= "Birthday:  ${it.birthday}"
                binding.bornIn.text = "Born:  ${it.place_of_birth}"
                setGender(it.gender)
                Picasso.get().load("${getString(R.string.image_path)}${it.profile_path}").placeholder(R.drawable.blankguy).into(binding.actorPic)
                if (it.biography.isNotEmpty()){
                    binding.bio.setOnClickListener {view->
                        openBio(it.biography)
                    }
                }
            }
        })

        viewModel.pics.observe(this, Observer {
            adapter= PicsAdapter(it,this)
            binding.picsRecycler.adapter = adapter
        })
    }

    private fun setGender(num:Int){
        if (num==1){
            binding.gender.text = "Gender:  Female"
        }else{
            binding.gender.text = "Gender:  Male"
        }
    }

    fun openBio(bio:String){
        val intent= Intent(this,BiographyActivity::class.java)
        intent.putExtra("Bio",bio)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (id!=null) {
            outState.putInt("Current_id", id!!)
        }
    }


}
