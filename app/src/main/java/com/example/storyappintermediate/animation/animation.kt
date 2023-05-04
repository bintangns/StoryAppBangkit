package com.example.storyappintermediate

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.example.storyappintermediate.databinding.ActivityLoginBinding

fun applyFadeInAnimations(context: Context, binding: ActivityLoginBinding) {
    val fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)

    val emailField = binding.edLoginEmail
    val passwordField = binding.edLoginPassword
    val loginButton = binding.btnLogin
    val registerButton = binding.btnOpenRegister

    emailField.visibility = View.INVISIBLE
    passwordField.visibility = View.INVISIBLE
    loginButton.visibility = View.INVISIBLE
    registerButton.visibility = View.INVISIBLE

    val animationDuration = 700L
    fadeInAnimation.duration = animationDuration

    val delayBetweenFields = 300L
    emailField.postDelayed({
        emailField.visibility = View.VISIBLE
        emailField.startAnimation(fadeInAnimation)
    }, delayBetweenFields * 1)

    passwordField.postDelayed({
        passwordField.visibility = View.VISIBLE
        passwordField.startAnimation(fadeInAnimation)
    }, delayBetweenFields * 2)

    loginButton.postDelayed({
        loginButton.visibility = View.VISIBLE
        loginButton.startAnimation(fadeInAnimation)
    }, delayBetweenFields * 3)

    registerButton.postDelayed({
        registerButton.visibility = View.VISIBLE
        registerButton.startAnimation(fadeInAnimation)
    }, delayBetweenFields * 4)
}