package com.dicoding.myunlimitedquotes.di

import android.content.Context
import com.example.storyappintermediate.api.ApiConfig
import com.example.storyappintermediate.database.StoryDatabase
import com.example.storyappintermediate.utils.PreferencesHelper
import com.example.storyappintermediate.utils.StoryRepository


object Injection {

    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        val preferencesHelper = PreferencesHelper(context)
        val token = "Bearer ${preferencesHelper.token}"
        return StoryRepository(database, apiService, token)
    }
}