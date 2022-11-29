package com.moyehics.news.di

import android.content.Context
import com.moyehics.news.roomdp.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {
    @Provides
    @Singleton
    fun provideArticleDatabase(@ApplicationContext appContext: Context):ArticleDatabase{
        return ArticleDatabase.getDatabase(appContext)
    }
}