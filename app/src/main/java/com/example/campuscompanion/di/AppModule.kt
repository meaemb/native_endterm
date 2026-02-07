package com.example.campuscompanion.di

import android.content.Context
import com.example.campuscompanion.data.local.dao.EventDao
import com.example.campuscompanion.data.local.db.AppDatabase
import com.example.campuscompanion.data.remote.api.EventsApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.tvmaze.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideEventsApi(retrofit: Retrofit): EventsApi =
        retrofit.create(EventsApi::class.java)

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.get(context)

    @Provides
    @Singleton
    fun provideEventDao(db: AppDatabase): EventDao =
        db.eventDao()

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDb(): FirebaseDatabase =
        FirebaseDatabase.getInstance()
}
