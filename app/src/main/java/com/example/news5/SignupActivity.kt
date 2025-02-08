package com.example.news5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news5.databinding.ActivitySignupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignupActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.loginBtn.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if(chechAllField()){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        auth.signOut()
                        Toast.makeText(this, "Account create success!" , Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SigninActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Log.e("error: ",it.exception.toString())
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
        if (binding.conformpsw.text.toString() == "") {
            binding.textInputLayoutConformPassword.error = "This is required field"
            binding.textInputLayoutConformPassword.errorIconDrawable = null
            return false
        }
        if(binding.etPassword.text.toString() != binding.conformpsw.text.toString()){
            binding.textInputLayoutPassword.error = "Password do not match"
            return false
        }
        return true
    }
}