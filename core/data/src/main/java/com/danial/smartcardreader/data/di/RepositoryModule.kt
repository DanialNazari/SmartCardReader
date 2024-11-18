package com.danial.smartcardreader.data.di

import com.danial.smartcardreader.data.repository.BaseRepository
import com.danial.smartcardreader.data.repository.CardListRepository
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