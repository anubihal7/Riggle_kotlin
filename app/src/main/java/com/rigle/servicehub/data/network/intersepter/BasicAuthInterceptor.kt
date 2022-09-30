package com.rigle.servicehub.data.network.intersepter

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor(
    user: String, password: String
) : Interceptor {

    private var credentials = Credentials.basic(user, password)

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        //builder.header("Authorization", credentials)
        builder.header("x-app-name", "play")
//x-app-name: service_hub
        return chain.proceed(builder.build())
    }
}