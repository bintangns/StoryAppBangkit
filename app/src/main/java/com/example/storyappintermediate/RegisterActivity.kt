package com.example.storyappintermediate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.FrameLayout
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

        val textWatcher = createTextWatcher { _, _, _, _ ->
            updateRegisterState()
        }

        binding.edRegisterEmail.addTextChangedListener(textWatcher)
        binding.edRegisterPassword.addTextChangedListener(textWatcher)



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
            showLoading(true)



            call.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    showLoading(false)
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

    private fun updateRegisterState() {
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()
        binding.btnRegister.isEnabled = isValidEmail(email) && isValidPassword(password)
        updateButtonText(email, password, name)
    }

    private fun updateButtonText(email: String, password: String, name: String) {
        when {
            name.isEmpty()||email.isEmpty() || password.isEmpty() -> {
                binding.btnRegister.text = "Mohon Isi Form"
            }
            !isValidEmail(email) -> {
                binding.btnRegister.text = "Email tidak valid"
            }
            !isValidPassword(password) -> {
                binding.btnRegister.text = "Password Minimal 8 Karakter"
            }
            else -> {
                binding.btnRegister.text = "Register"
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        val loadingOverlay = findViewById<FrameLayout>(R.id.loading_overlay)
        if (isLoading) {
            loadingOverlay.visibility = View.VISIBLE
        } else {
            loadingOverlay.visibility = View.GONE
        }
    }
}