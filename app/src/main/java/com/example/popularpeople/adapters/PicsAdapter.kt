package com.example.popularpeople.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.popularpeople.R
import com.example.popularpeople.models.Pics
import com.example.popularpeople.ui.FullscreenActivity
import com.squareup.picasso.Picasso

class PicsAdapter() : RecyclerView.Adapter<PicViewHolder>(){

    private val path = "http://image.tmdb.org/t/p/w300/"
    var list: List<Pics>? = null
    private lateinit var context: Context

    constructor(list: List<Pics>,context: Context):this(){
        this.list= list
        this.context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicViewHolder {
    val view= LayoutInflater.from(parent.context).inflate(R.layout.single_image,parent,false)
    return PicViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: PicViewHolder, position: Int) {
        val pic = list!![position]
        val fullPath = path+pic.file_path
        Picasso.get().load(fullPath).placeholder(R.drawable.blankguy).into(holder.image)
        holder.itemView.setOnClickListener {
            openPic(pic.file_path)
        }
    }

    private fun openPic(url:String){
        val intent = Intent(context,FullscreenActivity::class.java)
        intent.putExtra("url",url)
        context.startActivity(intent)
    }
}

class PicViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    var image = itemView.findViewById<ImageView>(R.id.single_image)
}
