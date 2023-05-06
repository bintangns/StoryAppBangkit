package com.example.storyappintermediate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import applyFadeInAnimations
import com.example.storyappintermediate.api.ApiConfig
import com.example.storyappintermediate.api.RegisterCredentials
import com.example.storyappintermediate.api.RegisterResponse
import com.example.storyappintermediate.api.StoryApi
import com.example.storyappintermediate.databinding.ActivityRegisterBinding
import com.example.storyappintermediate.model.User
import com.example.storyappintermediate.utils.PreferencesHelper
import com.example.storyappintermediate.utils.createTextWatcher
import com.example.storyappintermediate.utils.isValidEmail
import com.example.storyappintermediate.utils.isValidPassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var storyApi: StoryApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyApi = ApiConfig.getApiService()

        val button = listOf(
            binding.edRegisterName,
            binding.edRegisterEmail,
            binding.edRegisterPassword,
            binding.btnRegister
        )
        applyFadeInAnimations(this, button)

        binding.btnRegister.isEnabled = false

        binding.edRegisterPassword.addTextChangedListener(createTextWatcher { s, _, _, _ ->
            binding.btnRegister.isEnabled = isValidPassword(s.toString())
        })



        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            val user = RegisterCredentials(name, email, password)
            val call = storyApi.register(user)

            if (!isValidEmail(email)) {
                binding.edRegisterEmail.error = "Please enter a valid email address"
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                binding.edRegisterPassword.error = "Password must be at least 8 characters"
                return@setOnClickListener
            }



            call.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        if (registerResponse?.error == false) {
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, registerResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}