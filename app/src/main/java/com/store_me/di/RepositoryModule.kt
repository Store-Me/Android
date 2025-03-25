package com.store_me.di

import com.store_me.storeme.repository.naver.NaverRepository
import com.store_me.storeme.repository.naver.NaverRepositoryImpl
import com.store_me.storeme.repository.storeme.AuthRepository
import com.store_me.storeme.repository.storeme.AuthRepositoryImpl
import com.store_me.storeme.repository.storeme.CustomerRepository
import com.store_me.storeme.repository.storeme.CustomerRepositoryImpl
import com.store_me.storeme.repository.storeme.ImageRepository
import com.store_me.storeme.repository.storeme.ImageRepositoryImpl
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
    abstract fun bindNaverRepository(
        naverRepository: NaverRepositoryImpl
    ): NaverRepository

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

    @Binds
    @Singleton
    abstract fun bindCustomerRepository(
        customerRepositoryImpl: CustomerRepositoryImpl
    ): CustomerRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindImageRepository(
        imageRepositoryImpl: ImageRepositoryImpl
    ): ImageRepository
}