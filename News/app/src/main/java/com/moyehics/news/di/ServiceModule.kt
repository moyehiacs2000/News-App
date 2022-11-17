package com.moyehics.news.di

import com.google.firebase.firestore.FirebaseFirestore
import com.moyehics.news.network.NewsInterface
import com.moyehics.news.network.RetrofitBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {
    @Provides
    @Singleton
    fun provideNewsServiceInstance(): NewsInterface {
        return RetrofitBuilder.getRetrofitBuilder().create(NewsInterface::class.java)
    }
}