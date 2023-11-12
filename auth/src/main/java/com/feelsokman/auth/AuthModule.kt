package com.feelsokman.auth

import android.accounts.AccountManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun providesAccountManager(@ApplicationContext context: Context): AccountManager {
        return AccountManager.get(context)
    }

    @Provides
    @Singleton
    fun providesAndroidAccountManager(accountManager: AccountManager): AndroidAccountManager {
        return AndroidAccountManagerImpl(accountManager)
    }
}