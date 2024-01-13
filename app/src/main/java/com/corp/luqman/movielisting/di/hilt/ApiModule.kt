package com.corp.luqman.movielisting.di.hilt

import android.content.Context
import com.corp.luqman.movielisting.data.remote.ApiService
import com.corp.luqman.movielisting.data.remote.MyAuthenticator
import com.corp.luqman.movielisting.data.remote.TokenInterceptor
import com.corp.luqman.movielisting.utils.Const
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideBaseUrl(): String{
        return Const.appUrl
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext appContext: Context) : OkHttpClient{
        val httpClient = OkHttpClient.Builder()
        httpClient.apply {
            writeTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            callTimeout(60, TimeUnit.SECONDS)
            authenticator(MyAuthenticator())
            addInterceptor(TokenInterceptor())
            addInterceptor(ChuckInterceptor(appContext))
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(logging)
        }
        return httpClient.build()
    }


    @Singleton
    @Provides
    fun provideRetrofitApi(url: String,
                           client: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService{
        return retrofit.create(ApiService::class.java)
    }
}