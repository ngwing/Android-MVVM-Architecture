package com.anthony.foodmap.core.dagger.modules

import com.anthony.foodmap.data.api.ApiService
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.FormBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesClient(): OkHttpClient? {
        okHttpClient = OkHttpClient.Builder()
                .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor {
                    val request = it.request()
                    val newRequest = if (request.method() == "POST") {
                        val builder = request.newBuilder()
                        val requestBody = FormBody.Builder().apply {
                            add("client_id", "0XFEKWQMQWF2LJ14WPAOIPUV5QMDYLLHOMXRWRKC4EUAHEGJ")
                            add("client_secret", "FXHZSPBOIRKJFKNBNUFBJZDN02GU3QBGEYLJEX0OQPZ2OHPI")
                            add("v", "20200112")
                            add("intent", "browse")
                            add("categoryId", "4d4b7105d754a06374d81259")
                        }
                                .build()
                        builder
                                .post(requestBody)
                                .build()
                    } else {
                        val originalHttpUrl = request.url()
                        val url = originalHttpUrl.newBuilder().apply {
                            addQueryParameter("client_id", "0XFEKWQMQWF2LJ14WPAOIPUV5QMDYLLHOMXRWRKC4EUAHEGJ")
                            addQueryParameter("client_secret", "FXHZSPBOIRKJFKNBNUFBJZDN02GU3QBGEYLJEX0OQPZ2OHPI")
                            addQueryParameter("v", "20200112")
                            addQueryParameter("intent", "browse")
                            addQueryParameter("categoryId", "4d4b7105d754a06374d81259")
                        }
                                .build()
                        val requestBuilder = request.newBuilder()
                                .url(url)
                        requestBuilder.build()
                    }


                    return@addInterceptor it.proceed(newRequest)
                }.build()
        return okHttpClient
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(providesClient())
                .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    companion object {
        private const val BASE_URL = "https://api.foursquare.com/v2/venues/"
        private const val REQUEST_TIMEOUT = 10
        private var okHttpClient: OkHttpClient? = null
    }
}