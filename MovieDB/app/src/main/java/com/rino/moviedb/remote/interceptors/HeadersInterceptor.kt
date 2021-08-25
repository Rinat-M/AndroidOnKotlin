package com.rino.moviedb.remote.interceptors

import com.rino.moviedb.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeadersInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json;charset=utf-8")
            .addHeader("Authorization", "Bearer ${BuildConfig.MOVIEDB_API_KEY}")
            .build()

        return chain.proceed(request)
    }
}