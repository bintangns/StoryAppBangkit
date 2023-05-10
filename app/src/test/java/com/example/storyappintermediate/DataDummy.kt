package com.dicoding.myunlimitedquotes

import com.example.storyappintermediate.model.Story

object DataDummy {
    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                id = i.toString(),
                name = "Name $i",
                description = "Description $i",
                photoUrl = "https://example.com/photoUrl$i.jpg",
                createdAt = "2023-05-09T00:00:00Z",
                lat = -6.200000 + i, // just example
                lon = 106.816666 + i // just example
            )
            items.add(story)
        }
        return items
    }
}