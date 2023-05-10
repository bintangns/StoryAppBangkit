package com.example.storyappintermediate

import StoryAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappintermediate.api.ApiConfig
import com.example.storyappintermediate.api.GetStoriesResponse
import com.example.storyappintermediate.database.StoryDatabase
import com.example.storyappintermediate.databinding.ActivityMainBinding
import com.example.storyappintermediate.model.Story
import com.example.storyappintermediate.utils.PreferencesHelper
import com.example.storyappintermediate.utils.StoryRepository
import com.example.storyappintermediate.viewmodel.StoryViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: StoryViewModel
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)



        if (!preferencesHelper.isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            viewModel = ViewModelProvider(this, StoryViewModel.ViewModelFactory(this)).get(StoryViewModel::class.java)
            storyAdapter = StoryAdapter { story ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY_ID, story.id)
                startActivity(intent)
            }
            binding.rvStories.layoutManager = LinearLayoutManager(this)
            binding.rvStories.adapter = storyAdapter

            showLoading(true)
            getData()

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
        return when (item.itemId)
        {
            R.id.action_map -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            R.id.action_logout -> {
                preferencesHelper.clear()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun getData() {
        showLoading(false)
        viewModel.story.observe(this, { pagingData ->
            lifecycleScope.launch {
                storyAdapter.submitData(pagingData)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}