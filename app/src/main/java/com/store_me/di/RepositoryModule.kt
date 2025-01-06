package com.store_me.di

import com.store_me.storeme.repository.storeme.OwnerRepository
import com.store_me.storeme.repository.storeme.OwnerRepositoryImpl
import com.store_me.storeme.repository.storeme.UserRepository
import com.store_me.storeme.repository.storeme.UserRepositoryImpl
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
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindOwnerRepository(
        ownerRepositoryImpl: OwnerRepositoryImpl
    ): OwnerRepository
}