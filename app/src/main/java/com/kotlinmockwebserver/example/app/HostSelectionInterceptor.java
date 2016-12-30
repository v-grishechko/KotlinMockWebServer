package com.kotlinmockwebserver.example.app;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public final class HostSelectionInterceptor implements Interceptor {
    private volatile HttpUrl url;

    public void setHost(HttpUrl url) {
        this.url = url;
    }

    @Override public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = this.url;
        if (url != null) {
            request = request.newBuilder()
                    .url(url)
                    .build();
        }
        return chain.proceed(request);
    }
}