package com.danial.smartcardreader.network

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .header("apikey","K88496834788957")
            .url(originalRequest.url).build()
        Timber.d(request.toString())
        return chain.proceed(request)
    }
}