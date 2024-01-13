package com.corp.luqman.movielisting.data.remote

import com.corp.luqman.movielisting.utils.Const
import okhttp3.*


class TokenInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest: Request

        newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer ${Const.apikey}")
            .build()
        return chain.proceed(newRequest)
    }

}

class MyAuthenticator() : Authenticator {
    private var request: Request? = null
    private var response: Response? = null
    override fun authenticate(route: Route?, response: Response): Request? {
        var tokenRefresh = ""


        if (!tokenRefresh.isBlank()) {
            return this.response?.request?.newBuilder()
                ?.header(
                    "Authorization",
                    "Bearer ${tokenRefresh}"
                )
                ?.build()
        } else {
            return null
        }
    }

}