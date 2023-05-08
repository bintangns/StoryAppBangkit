package com.example.storyappintermediate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.example.storyappintermediate.api.ApiConfig
import com.example.storyappintermediate.api.LoginCredentials
import com.example.storyappintermediate.api.LoginResponse
import com.example.storyappintermediate.api.StoryApi
import com.example.storyappintermediate.databinding.ActivityLoginBinding
import com.example.storyappintermediate.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout
import applyFadeInAnimations
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        preferencesHelper = PreferencesHelper(this)

        val button = listOf(
            binding.edLoginEmail,
            binding.edLoginPassword,
            binding.btnLogin,
            binding.btnOpenRegister
        )
        applyFadeInAnimations(this, button)

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            val loginCredentials = LoginCredentials(email, password)
            val call = ApiConfig.getApiService().login(loginCredentials)

            showLoading(true)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.error == false) {
                            preferencesHelper.isLoggedIn = true
                            preferencesHelper.token = loginResponse.loginResult.token
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                loginResponse?.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
        binding.btnOpenRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
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