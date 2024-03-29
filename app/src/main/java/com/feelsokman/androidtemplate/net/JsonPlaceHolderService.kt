package com.feelsokman.androidtemplate.net

import com.feelsokman.androidtemplate.net.model.ApiTodo
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonPlaceHolderService {

    @GET("/todos/{id}")
    suspend fun getTodo(@Path(value = "id") todoId: Int): ApiTodo
}
