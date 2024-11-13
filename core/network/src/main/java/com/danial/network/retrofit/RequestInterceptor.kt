package com.danial.network.retrofit

import com.danial.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .header("apikey", BuildConfig.OCR_API_KEY)
            .url(originalRequest.url).build()
        Timber.d(request.toString())
        return chain.proceed(request)
    }
}