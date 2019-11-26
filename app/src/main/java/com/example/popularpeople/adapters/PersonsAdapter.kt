package com.example.popularpeople.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.popularpeople.R
import com.example.popularpeople.models.Person
import com.example.popularpeople.ui.DetailsActivity
import com.squareup.picasso.Picasso

class PersonsAdapter() : RecyclerView.Adapter<PersonViewHolder>(){
    private lateinit var  path :String
    var list: MutableList<Person>? = null
    private lateinit var context: Context

    constructor(list: MutableList<Person>?,context: Context):this(){
        this.list= list
        this.context=context
        path=context.getString(R.string.image_path)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.single_person,parent,false)
        return PersonViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        if (list!=null){
            val person = list!![position]
            holder.name.text = person.name

            Picasso.get().load("$path${person.profile_path}").placeholder(R.drawable.blankguy).into(holder.image)
            holder.itemView.setOnClickListener {
                sentIntent(person.id)
            }
        }
    }

    private fun sentIntent(id: Int) {
        val intent = Intent(context,DetailsActivity::class.java)
        intent.putExtra("id",id)
        context.startActivity(intent)
    }

    fun addMore(newlist:MutableList<Person>){
        list!!.addAll(newlist)
        notifyDataSetChanged()
    }
}

class PersonViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    var name = itemView.findViewById<TextView>(R.id.name)
    val image = itemView.findViewById<ImageView>(R.id.personImage)
}
