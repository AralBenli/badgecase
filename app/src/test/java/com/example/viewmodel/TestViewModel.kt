package com.example.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.badgecase.api.BadgeListRequest
import com.example.badgecase.api.PraiseListRequest
import com.example.badgecase.di.ApiResponse
import com.example.badgecase.models.response.badgelistresponse.BadgeListResponse
import com.example.badgecase.models.response.badgelistresponse.BadgeListResult
import com.example.badgecase.models.response.praiselistresponse.*
import com.example.badgecase.ui.home.HomeViewModel
import com.example.repository.TestRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.any
import org.mockito.Mockito.doReturn
import org.mockito.MockitoAnnotations
import retrofit2.Response


/**
 * Created by AralBenli on 8.12.2023.
 */

@ExperimentalCoroutinesApi
class TestViewModel {

    private lateinit var viewModel: HomeViewModel
    lateinit var jsonBadge: BadgeListRequest
    lateinit var jsonPraise: PraiseListRequest

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var testRepository: TestRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(testRepository)

        jsonBadge = BadgeListRequest("badgeList")
        jsonPraise = PraiseListRequest("praiseList")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    val person = RelatedPerson("2", "author")
    val personList = listOf(person)
    val badge = Badge(1, "3")
    val badgeList = listOf(badge)
    val author = Author("1", "Speacialist")
    val authorList = listOf(author)

    val expectedBadgeResultItemSucess = listOf(BadgeListResult("icon", 3, 3, "Değer Katan"))
    val expectedBadgeResultItemFail = listOf(BadgeListResult("icon", 4, 3, "Değer Katan"))
    val fakeBadgeListResponseSuccess = BadgeListResponse(expectedBadgeResultItemSucess)
    val fakeBadgeListResponseFail = BadgeListResponse(expectedBadgeResultItemFail)


    val expectedPraiseResultItemSuccess = listOf(
        PraiseResultItem(
            "1", authorList, badgeList, personList, "message", "12.12.2023", "4",
            "4.0", "1", "Specialist", "Speacialist"
        )
    )
    val expectedPraiseResultItemFail = listOf(
        PraiseResultItem(
            "2", authorList, badgeList, personList, "message", "12.12.2023", "4",
            "4.0", "2", "Specialist", "Speacialist"
        )
    )
    val fakePraiseListResponseSuccess = PraiseListResponse(expectedPraiseResultItemSuccess)
    val fakePraiseListResponseFail = PraiseListResponse(expectedPraiseResultItemFail)


    @Test
    fun `fetchBadgeList emits correct list`() = runTest(testDispatcher) {
        assertNotNull(jsonBadge)

        val job = launch {
            doReturn(flow { emit(ApiResponse.Success(fakeBadgeListResponseSuccess)) })
                .`when`(testRepository)
                .getBadgeList(jsonBadge)

            val startTime = System.currentTimeMillis()
            viewModel.fetchBadgeList(jsonBadge)
            val endTime = System.currentTimeMillis()

            viewModel._badgeListStateFlow.collectLatest {
                println(it?.badgeList)
                assertEquals(fakeBadgeListResponseSuccess, it)
            }

            println("Time taken for fetchBadgeList: ${endTime - startTime}")

            delay(1000)
        }

        coroutineContext.cancelChildren()

        job.join()
    }

    @Test
    fun `fetchBadgeList emits fails list`() = runTest(testDispatcher) {
        assertNotNull(jsonBadge)

        val job = launch {
            doReturn(flow { emit(ApiResponse.Success(fakeBadgeListResponseFail)) })
                .`when`(testRepository)
                .getBadgeList(jsonBadge)

            val startTime = System.currentTimeMillis()
            viewModel.fetchBadgeList(jsonBadge)
            val endTime = System.currentTimeMillis()

            viewModel._badgeListStateFlow.collectLatest {
                //println(it?.badgeList)

                println(fakeBadgeListResponseFail)
                println(it)
                assertNotEquals(expectedBadgeResultItemSucess, it)
            }

            println("Time taken for fetchBadgeList: ${endTime - startTime}")

            delay(1000)
        }

        coroutineContext.cancelChildren()

        job.join()
    }


    @Test
    fun `fetchPraiseList emits list`() = runTest(testDispatcher) {
        assertNotNull(jsonPraise)

        val job = launch {
            doReturn(flow { emit(ApiResponse.Success(fakePraiseListResponseSuccess)) })
                .`when`(testRepository)
                .getPraiseList(jsonPraise)

            val startTime = System.currentTimeMillis()
            viewModel.fetchPraiseList(jsonPraise)
            val endTime = System.currentTimeMillis()

            viewModel._praiseListStateFlow.collectLatest {
                println(it?.praiseResultItem)
                assertEquals(fakePraiseListResponseSuccess, it)
            }

            println("Time taken for praiseList: ${endTime - startTime}")

            delay(1000)

        }

        coroutineContext.cancelChildren()

        job.join()

    }


    @Test
    fun `fetchPraiseList emits fails list`() = runTest(testDispatcher) {
        assertNotNull(jsonPraise)

        val job = launch {
            doReturn(flow { emit(ApiResponse.Success(fakePraiseListResponseFail)) })
                .`when`(testRepository)
                .getPraiseList(jsonPraise)

            val startTime = System.currentTimeMillis()
            viewModel.fetchPraiseList(jsonPraise)
            val endTime = System.currentTimeMillis()

            viewModel._praiseListStateFlow.collectLatest {
                println(it?.praiseResultItem)

                println(fakePraiseListResponseFail)
                println(it)
                assertNotEquals(fakePraiseListResponseSuccess, it)
            }

            println("Time taken for praiseList: ${endTime - startTime}")

            delay(1000)

        }

        coroutineContext.cancelChildren()

        job.join()

    }


}


















