package com.example.storyappintermediate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.storyappintermediate.api.ApiConfig
import com.example.storyappintermediate.api.DetailStoryResponse
import com.example.storyappintermediate.api.StoryApi
import com.example.storyappintermediate.databinding.ActivityDetailBinding
import com.example.storyappintermediate.model.Story
import com.example.storyappintermediate.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_STORY_ID = "extra_story_id"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var storyApi: StoryApi
    private var storyId: String? = null
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyId = intent.getStringExtra(EXTRA_STORY_ID)

        preferencesHelper = PreferencesHelper(this)

        storyApi = ApiConfig.getApiService()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        loadStoryDetail()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadStoryDetail() {
        val token = "Bearer ${preferencesHelper.token}"
        storyId?.let { id ->
            storyApi.getStoryDetail(id, token).enqueue(object : Callback<DetailStoryResponse> {
                override fun onResponse(call: Call<DetailStoryResponse>, response: Response<DetailStoryResponse>) {
                    if (response.isSuccessful) {
                        val story = response.body()?.story
                        update(story)
                    } else {
                        Toast.makeText(this@DetailActivity, "Failed to load story detail", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                    Toast.makeText(this@DetailActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    private fun update(story: Story?) {
        story?.let {
            binding.tvDetailName.text = it.name
            binding.tvDetailDescription.text = it.description

            Glide.with(this)
                .load(it.photoUrl)
                .into(binding.ivDetailPhoto)
        }
    }
}