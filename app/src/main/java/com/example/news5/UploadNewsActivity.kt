package com.example.news5

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.news5.databinding.ActivityUploadNewsBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class UploadNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadNewsBinding
    private var uriList = ArrayList<Uri>()
    private var imageUrls = ArrayList<String>()

    var imageURL: String? = null
    var uri: Uri? = null


    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                uriList.clear()

                if (data?.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        uriList.add(imageUri)
                    }
                } else if (data?.data != null) {
                    uriList.add(data.data!!)
                }

                if (uriList.isNotEmpty()) {
                    binding.uploadImage.setImageURI(uriList[0])
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                uri = data!!.data
                binding.uploadImage.setImageURI(uri)
            } else {
                Toast.makeText(this@UploadNewsActivity, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }
        binding.uploadImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }




        binding.uploadImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            photoPicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            activityResultLauncher.launch(photoPicker)
        }

        binding.saveButton.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {


        val storageReference = FirebaseStorage.getInstance().reference.child("Task Images")
            .child(uri!!.lastPathSegment!!)
        val builder = AlertDialog.Builder(this@UploadNewsActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()
        storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageURL = urlImage.toString()
            uploadData()
            dialog.dismiss()
        }.addOnFailureListener {
            dialog.dismiss()
        }










//        if (uri == null) {
//            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val storageReference = FirebaseStorage.getInstance().reference.child("News Images")
//        imageUrls.clear()
//        val builder = AlertDialog.Builder(this)
//        builder.setCancelable(false)
//        val dialog = builder.create()
//        dialog.show()
//
//        var uploadCount = 0
//        for (uri in uriList) {
//            val fileRef = storageReference.child(uri.lastPathSegment!!)
//            fileRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
//                taskSnapshot.storage.downloadUrl.addOnSuccessListener { url ->
//                    imageUrls.add(url.toString())
//                    uploadCount++
//
//                    if (uploadCount == uriList.size) {
//                        uploadData()
//                        dialog.dismiss()
//                    }
//                }
//            }.addOnFailureListener {
//                dialog.dismiss()
//                Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

//    private fun uploadData() {
//        val title = binding.uploadTitle.text.toString()
//        val desc = binding.uploadDesc.text.toString()
//        val priority = binding.uploadPriority.text.toString()
//
//        if (title.isEmpty() || desc.isEmpty() || priority.isEmpty()) {
//            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
//        val data = mapOf(
//            "title" to title,
//            "desc" to desc,
//            "priority" to priority,
//            "images" to imageUrls
//        )
//
//        FirebaseDatabase.getInstance().getReference("News").child(currentDate)
//            .setValue(data)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "Published", Toast.LENGTH_SHORT).show()
//                    finish()
//                    startActivity(Intent(this, HomeActivity::class.java))
//                }
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
//            }
//    }



    private fun uploadData(){
        val title = binding.uploadTitle.text.toString()
        val desc = binding.uploadDesc.text.toString()
        val priority = binding.uploadPriority.text.toString()
        val dataClass = DataClass(title, desc, priority, imageURL)
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
        FirebaseDatabase.getInstance().getReference("Todo List").child(currentDate)
            .setValue(dataClass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@UploadNewsActivity, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this@UploadNewsActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }


}