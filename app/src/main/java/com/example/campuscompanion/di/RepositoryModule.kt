package com.example.campuscompanion.di

import com.example.campuscompanion.data.repository.EventsRepositoryImpl
import com.example.campuscompanion.data.repository.UserRepositoryImpl
import com.example.campuscompanion.domain.repository.EventsRepository
import com.example.campuscompanion.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEventsRepository(impl: EventsRepositoryImpl): EventsRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
