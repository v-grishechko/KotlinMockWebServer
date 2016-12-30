package com.kotlinmockwebserver.server_rule.interceptor

import okhttp3.Interceptor
import java.io.IOException

class HostSelectionInterceptor : Interceptor {
    @Volatile private var host: String? = null

    fun setHost(host: String) {
        this.host = host
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        val host = this.host
        if (host != null) {
            val newUrl = request.url().newBuilder()
                    .host(host)
                    .build()
            request = request.newBuilder()
                    .url(newUrl)
                    .build()
        }
        return chain.proceed(request)
    }
}
