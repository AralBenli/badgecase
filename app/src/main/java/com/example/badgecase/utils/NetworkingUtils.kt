package com.example.badgecase.utils

import com.example.badgecase.di.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.lang.Error

/**
 * Created by AralBenli on 8.12.2023.
 */

fun<T> result(call: suspend() -> Response<T>) : Flow<ApiResponse<T?>> = flow {
    emit(ApiResponse.loading(true))

    try {
        val c = call()
        c.let {
            if (c.isSuccessful){
                emit(ApiResponse.success(c.body()))
            }else{
                c.errorBody()?.let {
                    it.close()
                    emit(ApiResponse.failure(Error(it.toString()), Response.error(400,c.errorBody())))

                }

            }
        }
    }catch(t:Throwable){
        t.printStackTrace()
        emit(ApiResponse.failure(t,null))
    }
}


