package com.danial.smartcardreader.network.di

import com.danial.smartcardreader.network.BuildConfig
import com.danial.smartcardreader.network.retrofit.ApiService
import com.danial.smartcardreader.network.retrofit.RequestInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	@Singleton
	@Provides
	fun provideOkHttpClient(): OkHttpClient {
		return OkHttpClient.Builder().apply {
			if (BuildConfig.DEBUG) {
				addInterceptor(HttpLoggingInterceptor().apply {
					level = HttpLoggingInterceptor.Level.BODY
				})
			}
			addInterceptor(RequestInterceptor())
		}.build()
	}

	@Singleton
	@Provides
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
		val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
		return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create(gson)).build()
	}

	@Singleton
	@Provides
	fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

}
