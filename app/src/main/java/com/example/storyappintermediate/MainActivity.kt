package com.example.storyappintermediate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
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
            showLoading(true)
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
                    showLoading(false)
                } else {
                    showLoading(true)
                }
            }

            override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
                showLoading(true)
            }
        })
    }
    private fun showStories(stories: List<Story>) {
        val storyAdapter = StoryAdapter(stories) { story, sharedView ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_STORY_ID, story.id)

            val transitionName = ViewCompat.getTransitionName(sharedView)
            if (transitionName != null) {
                val sharedElement = androidx.core.util.Pair.create(sharedView, transitionName)

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElement)
                startActivity(intent, options.toBundle())
            } else {
                startActivity(intent)
            }
        }
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = storyAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}