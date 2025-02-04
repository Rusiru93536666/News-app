package com.example.news5

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        // Get the button reference
        val addButton: Button = findViewById(R.id.addbtn)

        // Set an OnClickListener to open AddNewsActivity
        addButton.setOnClickListener {
            val intent = Intent(this, AddNewsActivity::class.java)
            startActivity(intent)
        }
    }
}
