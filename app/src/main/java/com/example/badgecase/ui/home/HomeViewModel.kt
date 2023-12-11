package com.example.badgecase.ui.home

import androidx.lifecycle.viewModelScope
import com.example.badgecase.api.BadgeListRequest
import com.example.badgecase.api.PraiseListRequest
import com.example.badgecase.di.ApiResponse
import com.example.badgecase.models.response.badgelistresponse.BadgeListResponse
import com.example.badgecase.models.response.praiselistresponse.PraiseListResponse
import com.example.badgecase.repository.IBadgeCaseRepository
import com.example.badgecase.ui.base.BaseViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

/**
 * Created by AralBenli on 8.12.2023.
 */

@HiltViewModel
class HomeViewModel @Inject constructor(val repo: IBadgeCaseRepository) : BaseViewModel() {

    private val progressStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val _progressStateFlow: StateFlow<Boolean> = progressStateFlow

    private val badgeListStateFlow: MutableSharedFlow<BadgeListResponse?> = MutableSharedFlow()
    val _badgeListStateFlow: SharedFlow<BadgeListResponse?> = badgeListStateFlow


    fun fetchBadgeList(json: BadgeListRequest) {
        viewModelScope.launch {
            val responseFLow = repo.getBadgeList(json)
            responseFLow.collectLatest {
                when (it) {
                    is ApiResponse.Progress -> {
                        progressStateFlow.value = true
                    }

                    is ApiResponse.Success -> {
                        badgeListStateFlow.emit(it.data)
                        progressStateFlow.value = false
                    }
                    is ApiResponse.Failure -> {
                        try {
                            val errorResponse = Gson().fromJson(
                                it.data!!.errorBody()!!.string(), BadgeListResponse::class.java
                            )
                            badgeListStateFlow.emit(errorResponse)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            progressStateFlow.value = false
                        }
                    }
                }
            }
        }
    }



private val praiseListStateFlow: MutableSharedFlow<PraiseListResponse?> = MutableSharedFlow()
val _praiseListStateFlow: SharedFlow<PraiseListResponse?> = praiseListStateFlow


fun fetchPraiseList(json: PraiseListRequest) {
    viewModelScope.launch {
        repo.getPraiseList(json).collectLatest {
            when (it) {
                is ApiResponse.Progress -> {
                    progressStateFlow.value = true
                }
                is ApiResponse.Success -> {
                    praiseListStateFlow.emit(it.data)
                    progressStateFlow.value = false
                }
                is ApiResponse.Failure -> {
                    try {
                        val errorResponse = Gson().fromJson(
                            it.data!!.errorBody()!!.string(), PraiseListResponse::class.java
                        )
                        praiseListStateFlow.emit(errorResponse)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        progressStateFlow.value = false
                    }
                }
            }
        }
    }
}
}