package com.example.news5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.news5.databinding.ActivityDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle = intent.extras
        if (bundle != null) {
            val title = bundle.getString("Title")
            val description = bundle.getString("Description")
            val priority = bundle.getString("Priority")
            val imageUrl = bundle.getString("Image")

            binding.detailTitle.text = title
            binding.detailDesc.text = description
            binding.detailPriority.text = priority
            Glide.with(this).load(imageUrl).into(binding.detailImage)
        }

        binding.toolbar.setOnClickListener {
            finish()
        }
    }

}
