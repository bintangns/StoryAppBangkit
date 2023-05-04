package com.example.storyappintermediate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappintermediate.adapter.StoryAdapter
import com.example.storyappintermediate.api.ApiConfig
import com.example.storyappintermediate.api.GetStoriesResponse
import com.example.storyappintermediate.databinding.ActivityMainBinding
import com.example.storyappintermediate.model.Story
import com.example.storyappintermediate.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)


        if (!preferencesHelper.isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }else{
            getAllStories()
        }

        binding.fabPost.setOnClickListener {
            startActivity(Intent(this, PostActivity::class.java))
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                preferencesHelper.clear()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getAllStories() {
        val token = "Bearer ${preferencesHelper.token}"
        ApiConfig.getApiService().getAllStories(token).enqueue(object :
            Callback<GetStoriesResponse> {
            override fun onResponse(call: Call<GetStoriesResponse>, response: Response<GetStoriesResponse>) {
                if (response.isSuccessful) {
                    val stories = response.body()?.listStory ?: listOf()
                    showStories(stories)
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
                // Handle error
            }
        })
    }
    private fun showStories(stories: List<Story>) {
        val storyAdapter = StoryAdapter(stories) { story ->
            // Handle onItemClick here
            // For example, start a new activity to show the details of the selected story
        }
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = storyAdapter
    }

}