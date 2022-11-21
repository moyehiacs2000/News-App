package com.moyehics.news.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moyehics.news.data.repository.authentication.AuthenticationRepository
import com.moyehics.news.data.repository.authentication.AuthenticationRepositoryImp
import com.moyehics.news.data.repository.news.NewRepository
import com.moyehics.news.data.repository.news.NewRepositoryImp
import com.moyehics.news.network.NewsInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        database: FirebaseFirestore,
        auth : FirebaseAuth
    ): AuthenticationRepository {
        return AuthenticationRepositoryImp(auth,database)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
         api: NewsInterface
    ): NewRepository {
        return NewRepositoryImp(api)
    }
}