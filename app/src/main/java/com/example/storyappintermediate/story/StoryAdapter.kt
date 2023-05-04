package com.example.storyappintermediate.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyappintermediate.DetailActivity
import com.example.storyappintermediate.databinding.ItemStoryBinding
import com.example.storyappintermediate.model.Story

class StoryAdapter(private val stories: List<Story>, private val onItemClickListener: (Story) -> Unit) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount() = stories.size

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.tvItemName.text = story.name

            // Use Glide to load the image
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)

            // Set onClickListener
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY_ID, story.id)
                itemView.context.startActivity(intent)
            }
        }
    }
}