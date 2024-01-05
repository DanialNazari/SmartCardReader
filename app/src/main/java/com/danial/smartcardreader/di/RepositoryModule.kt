package com.danial.smartcardreader.di

import com.danial.smartcardreader.repository.BaseRepository
import com.danial.smartcardreader.repository.CardListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    internal abstract fun bindCardListRepository(cardListRepository: CardListRepository) : BaseRepository
}