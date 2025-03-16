package com.kapirti.social_chat_food_video.core.datastore.di

import android.content.Context
import com.kapirti.social_chat_food_video.core.datastore.CameraTypeRepository
import com.kapirti.social_chat_food_video.core.datastore.CountryRepository
import com.kapirti.social_chat_food_video.core.datastore.EditTypeRepository
import com.kapirti.social_chat_food_video.core.datastore.IsReviewDataStore
import com.kapirti.social_chat_food_video.core.datastore.LearnLanguageRepository
import com.kapirti.social_chat_food_video.core.datastore.OnBoardingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideOnBoardingRepository(
        @ApplicationContext context: Context
    ) = OnBoardingRepository(context = context)

    @Provides
    @Singleton
    fun provideCountryRepository(
        @ApplicationContext context: Context
    ) = CountryRepository(context = context)

    @Provides
    @Singleton
    fun provideEditTypeRepository(
        @ApplicationContext context: Context
    ) = EditTypeRepository(context)

    @Provides
    @Singleton
    fun provideLPLanguageRepository(
        @ApplicationContext context: Context
    ) = LearnLanguageRepository(context)

    @Provides
    @Singleton
    fun provideIsReviewRepository(
        @ApplicationContext context: Context
    ) = IsReviewDataStore(context)


    @Provides
    @Singleton
    fun provideCameraTypeRepository(
        @ApplicationContext context: Context
    ) = CameraTypeRepository(context)
}
