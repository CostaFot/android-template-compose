package com.feelsokman.androidtemplate.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.feelsokman.androidtemplate.core.coroutine.DispatcherProvider
import com.feelsokman.androidtemplate.extensions.logDebug
import com.feelsokman.androidtemplate.extensions.logError
import com.feelsokman.androidtemplate.net.JsonPlaceHolderService
import com.feelsokman.androidtemplate.result.attempt
import com.feelsokman.androidtemplate.result.fold
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext

@HiltWorker
class GetTodoWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val jsonPlaceHolderClient: JsonPlaceHolderService,
    private val dispatcherProvider: DispatcherProvider
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(dispatcherProvider.io) {
        attempt {
            jsonPlaceHolderClient.getTodo(2)
        }.fold(
            ifSuccess = {
                logDebug { it.title }
                return@withContext Result.success()
            },
            ifError = {
                logError { it.toString() }
                return@withContext Result.failure()
            }
        )
    }

    companion object {

        fun getWorkRequest(): OneTimeWorkRequest {

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(true)
                .build()

            return OneTimeWorkRequestBuilder<GetTodoWorker>()
                .setConstraints(constraints)
                .build()
        }
    }
}

