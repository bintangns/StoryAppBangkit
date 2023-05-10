package com.example.storyappintermediate.utils

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyappintermediate.api.StoryApi
import com.example.storyappintermediate.database.StoryDatabase
import com.example.storyappintermediate.model.Story

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: StoryApi, private val token: String) {
    fun getStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }
}
