package com.feelsokman.androidtemplate.work

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.feelsokman.androidtemplate.R
import com.feelsokman.androidtemplate.domain.JsonPlaceHolderRepository
import com.feelsokman.androidtemplate.extensions.logDebug
import com.feelsokman.androidtemplate.extensions.logError
import com.feelsokman.common.coroutine.DispatcherProvider
import com.feelsokman.common.result.fold
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@HiltWorker
class ExpeditedGetTodoWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val jsonPlaceHolderRepository: JsonPlaceHolderRepository,
    private val dispatcherProvider: DispatcherProvider
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(dispatcherProvider.io) {
        delay(5000) // simulate a long running worker
        jsonPlaceHolderRepository.getTodo(2).fold(
            ifError = {
                logError { it.toString() }
                Result.failure()
            },
            ifSuccess = {
                logDebug { it.title }
                Result.success()
            }
        )
    }

    override suspend fun getForegroundInfo(): ForegroundInfo = appContext.todoForegroundInfo()

    companion object {

        const val TAG = "ExpeditedGetTodoWorker"
        fun getWorkRequest(): OneTimeWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            return OneTimeWorkRequestBuilder<ExpeditedGetTodoWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .addTag(TAG)
                .setConstraints(constraints)
                .build()
        }
    }
}

private const val NotificationId = 0
private const val NotificationChannelID = "NotificationChannel"


/**
 * Foreground information for sync on lower API levels when sync workers are being
 * run with a foreground service
 */
private fun Context.todoForegroundInfo() = ForegroundInfo(
    NotificationId,
    getTodoWorkNotification()
)

/**
 * Notification displayed on lower API levels when sync workers are being
 * run with a foreground service
 */
private fun Context.getTodoWorkNotification(): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            NotificationChannelID,
            "todo",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Description for this notification channel"
        }
        // Register the channel with the system, could do this on app onCreate too
        val notificationManager: NotificationManager? = this.getSystemService()

        notificationManager?.createNotificationChannel(channel)
    }

    return NotificationCompat.Builder(
        this,
        NotificationChannelID
    ).apply {
        setSmallIcon(R.drawable.ic_launcher_foreground)
        setContentTitle("This is a content title")
        setContentText("hello")
        priority = NotificationCompat.PRIORITY_DEFAULT
    }.build()

}
