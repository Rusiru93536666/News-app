package com.example.news5

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.news5.databinding.ActivityDetailBinding
import com.example.news5.databinding.ActivityEditBinding
import com.google.firebase.database.*

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var databaseReference: DatabaseReference
    private var imageUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setOnClickListener {
            finish()
        }

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Todo List") // Use your actual reference

        val bundle = intent.extras
        if (bundle != null) {

            // Find views by ID
            val uploadImage = findViewById<ImageView>(R.id.uploadImage)
            val uploadTitle = findViewById<EditText>(R.id.uploadTitle)
            val uploadPriority = findViewById<EditText>(R.id.uploadPriority)
            val uploadDesc = findViewById<EditText>(R.id.uploadDesc)
            val editButton = findViewById<Button>(R.id.EditButton)

            // Get data from the bundle
            val title = bundle.getString("Title")
            val description = bundle.getString("Description")
            val priority = bundle.getString("Priority")
            imageUrl = bundle.getString("Image") ?: "" // Save imageUrl for future use

            uploadTitle.setText(title)
            uploadPriority.setText(priority)
            uploadDesc.setText(description)

            // Load image with Glide
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(imageUrl)
                    .into(uploadImage)
            }

            // Set an onClickListener for the EditButton
            editButton.setOnClickListener {
                val updatedTitle = uploadTitle.text.toString().trim()
                val updatedPriority = uploadPriority.text.toString().trim()
                val updatedDesc = uploadDesc.text.toString().trim()

                // Check if any field is empty
                if (updatedTitle.isEmpty() || updatedPriority.isEmpty() || updatedDesc.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                } else {
                    // Query Firebase database using title to find the matching record
                    val query = databaseReference.orderByChild("dataTitle").equalTo(updatedTitle)

                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Data with matching title found, now update it
                                for (snapshot in dataSnapshot.children) {
                                    val updatedData = hashMapOf<String, Any>(
                                        "dataTitle" to updatedTitle,
                                        "dataDesc" to updatedDesc,
                                        "dataPriority" to updatedPriority,
                                        "dataImage" to imageUrl

                                    )

                                    // Update the data
                                    snapshot.ref.updateChildren(updatedData)
                                        .addOnSuccessListener {
                                            Toast.makeText(this@EditActivity, "Data updated successfully", Toast.LENGTH_SHORT).show()
                                            finish() // Close the activity after successful update
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(this@EditActivity, "Failed to update data", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            } else {
                                Toast.makeText(this@EditActivity, "No item found with the given title", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@EditActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }

        binding.deleteBtn.setOnClickListener {
            val title = binding.uploadTitle.text.toString()
            if (title.isNotEmpty()) {
                deleteItemFromFirebaseByTitle(title)
            }
        }
    }

    private fun deleteItemFromFirebaseByTitle(title: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Todo List")

        databaseRef.orderByChild("dataTitle").equalTo(title).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        // Delete each matching item
                        child.ref.removeValue()
                    }
                    Toast.makeText(this@EditActivity, "Item deleted successfully", Toast.LENGTH_SHORT).show()
                    finish() // Close activity after deletion
                } else {
                    Toast.makeText(this@EditActivity, "Item not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
