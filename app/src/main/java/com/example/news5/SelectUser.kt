package com.example.news5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SelectUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_select_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewerButton: Button = findViewById(R.id.viwerbtn)
        val reporterButton: Button = findViewById(R.id.reporterbtn)
        val adminButton: Button = findViewById(R.id.adminbtn)

        // Set click listeners for each button
        viewerButton.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }
        reporterButton.setOnClickListener {
            val intent = Intent(this, SigninReporter::class.java)
            startActivity(intent)
        }
        adminButton.setOnClickListener {
            val intent = Intent(this, SigninAdmin::class.java)
            startActivity(intent)
        }
    }
}