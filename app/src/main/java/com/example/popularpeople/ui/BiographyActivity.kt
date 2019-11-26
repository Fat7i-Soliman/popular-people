package com.example.popularpeople.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.popularpeople.R

class BiographyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biography)
        val bio = intent.extras?.getString("Bio")
        val textView = findViewById<TextView>(R.id.biography)

        textView.text = bio

        supportActionBar!!.title= "Biography"
    }
}
