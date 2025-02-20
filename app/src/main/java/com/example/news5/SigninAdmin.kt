package com.example.news5

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news5.databinding.ActivitySigninAdminBinding
import com.example.news5.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SigninAdmin : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivitySigninAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        auth = Firebase.auth

        binding.signinbtn.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if(chechAllField()){
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if (it.isSuccessful){
//                            successfully signin
                        val intent = Intent(this, HomeAdmin::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        binding.textInputLayoutEmail.error = "Login failed"
                    }
                }
            }
        }

    }

    private fun chechAllField(): Boolean{
        var email = binding.etEmail.text.toString()
        if (binding.etEmail.text.toString() == ""){
            binding.textInputLayoutEmail.error = "This is required field"
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.textInputLayoutEmail.error = "Check email format"
            return false
        }
        if (binding.etPassword.text.toString() == "") {
            binding.textInputLayoutPassword.error = "This is required field"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        if (binding.etPassword.length() <= 6) {
            binding.textInputLayoutPassword.error = "Passwrd ahould be at least 8 characters long"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_back) {
            onBackPressed() // Navigate back
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}