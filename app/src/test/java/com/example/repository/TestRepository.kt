package com.example.repository

import com.example.badgecase.api.BadgeListRequest
import com.example.badgecase.api.PraiseListRequest
import com.example.badgecase.di.ApiResponse
import com.example.badgecase.models.response.badgelistresponse.BadgeListResponse
import com.example.badgecase.models.response.badgelistresponse.BadgeListResult
import com.example.badgecase.models.response.praiselistresponse.*
import com.example.badgecase.repository.IBadgeCaseRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.withContext
import okhttp3.RequestBody

/**
 * Created by AralBenli on 8.12.2023.
 */
class TestRepository : IBadgeCaseRepository {

    val person = RelatedPerson("2", "author")
    val personList = listOf(person)
    val badge = Badge(1, "3")
    val badgeList = listOf(badge)
    val author = Author("1", "Speacialist")
    val authorList = listOf(author)
    val expectedPraiseResultItem = listOf(PraiseResultItem("1", authorList, badgeList, personList, "message", "12.12.2023", "4",
        "4.0", "1", "Specialist", "Speacialist"
    ))
    val fakePraiseListResponse = PraiseListResponse(expectedPraiseResultItem)




    val expectedBadgeResultItem = listOf(BadgeListResult("icon", 3, 3, "Değer Katan"))
    val fakeBadgeListResponse = BadgeListResponse(expectedBadgeResultItem)


    override fun getBadgeList(request: BadgeListRequest): Flow<ApiResponse<BadgeListResponse?>> {
        val completion = CompletableDeferred<Unit>()

        return flow {
            withContext(Dispatchers.IO) {
                println("Start getBadgeList: ${System.currentTimeMillis()}")
                emit(ApiResponse.Success(fakeBadgeListResponse))
                println("End getBadgeList: ${System.currentTimeMillis()}")
            }
        }.onCompletion {
            completion.complete(Unit)
        }
    }


    override fun getPraiseList(request: PraiseListRequest): Flow<ApiResponse<PraiseListResponse?>> {
        return flow {
            withContext(Dispatchers.IO) {
                emit(ApiResponse.Success(fakePraiseListResponse))
            }
        }
    }
}