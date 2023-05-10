package com.example.storyappintermediate.story

import StoryAdapter
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.myunlimitedquotes.DataDummy
import com.dicoding.myunlimitedquotes.MainDispatcherRule
import com.dicoding.myunlimitedquotes.getOrAwaitValue
import com.example.storyappintermediate.model.Story
import com.example.storyappintermediate.utils.StoryRepository
import com.example.storyappintermediate.viewmodel.StoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()


    @Mock
    private lateinit var storyRepository: StoryRepository


    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {

        val dummyQuote = DataDummy.generateDummyStoryResponse()
        val data: PagingData<Story> = QuotePagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<Story>>()
        expectedQuote.value = data
        Mockito.`when`(storyRepository.getStory()).thenReturn(expectedQuote)

        val storyViewModel = StoryViewModel(storyRepository)
        val actualStory: PagingData<Story> = storyViewModel.story.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.StoryComparator,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyQuote.size, differ.snapshot().size)
        Assert.assertEquals(dummyQuote[0], differ.snapshot()[0])

    }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<Story>>()
        expectedQuote.value = data
        Mockito.`when`(storyRepository.getStory()).thenReturn(expectedQuote)

        val mainViewModel = StoryViewModel(storyRepository)
        val actualQuote: PagingData<Story> = mainViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.StoryComparator,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class QuotePagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }


}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}


