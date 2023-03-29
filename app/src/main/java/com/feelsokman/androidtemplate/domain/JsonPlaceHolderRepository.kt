package com.feelsokman.androidtemplate.domain

import com.feelsokman.androidtemplate.core.coroutine.DispatcherProvider
import com.feelsokman.androidtemplate.domain.model.DomainTodo
import com.feelsokman.androidtemplate.net.JsonPlaceHolderService
import com.feelsokman.androidtemplate.result.Result
import com.feelsokman.androidtemplate.result.attempt
import com.feelsokman.androidtemplate.result.error.ErrorMapper
import com.feelsokman.androidtemplate.result.error.GenericError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class JsonPlaceHolderRepository @Inject constructor(
    private val jsonPlaceHolderService: JsonPlaceHolderService,
    private val dispatcherProvider: DispatcherProvider
) {
    suspend fun getTodo(todoId: Int): Result<GenericError, DomainTodo> =
        withContext(dispatcherProvider.io) {
            attempt(ErrorMapper::map) {
                val apiTodo = jsonPlaceHolderService.getTodo(todoId)
                DomainTodo(apiTodo.id!!, apiTodo.title!!, apiTodo.completed!!)
            }
        }
}
