package com.varunbehl.myinventory.networkLayer

//import com.readystatesoftware.chuck.ChuckInterceptor
import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory

import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

val BASE_URL: String = "http://gsx2json.com/"

/**
 * API module compilation used by KOIN
 */
val ApiModule = module {
    single { createStatusService(get()) }
    single { createOkHttpClient(androidContext()) }
    single { createRetrofit(get()) }
}

/**
 *
 * @param context Context uses android context
 * @param sharedPreferencesUtil SharedPreferencesUtil
 * @param authTokenResponseInterceptor AuthTokenResponseInterceptor used while an api call makes 401
 * code and then fetching the auth token again while the api is in process
 *
 * @return OkHttpClient
 */
fun createOkHttpClient(
    context: Context
): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()

    clientBuilder.addInterceptor { chain ->
        var request = chain.request()
        val requestBuilder = request.newBuilder()
        val headerBuilder = Headers.Builder()
        val traceId = UUID.randomUUID().toString().replace("-", "")
        val headers = headerBuilder
            .add("Accept", "application/json")
            .add("Content-Type", "application/json")

        request = requestBuilder.build()

        requestBuilder
            .headers(headers.build())
            .method(request.method, request.body)

        chain.proceed(requestBuilder.build())

    }.build()

//    // Create the Collector
//    val chuckerCollector = ChuckerCollector(
//        context = context,
//        // Toggles visibility of the push notification
//        showNotification = false,
//        // Allows to customize the retention period of collected data
//        retentionPeriod = RetentionManager.Period.ONE_WEEK
//    )
//
//    // Create the Interceptor
//    val chuckerInterceptor = ChuckerInterceptor.Builder(context = context)
//        .collector(collector = chuckerCollector)
//        .maxContentLength(length = 250000L)
//        .redactHeaders(headerNames = setOf(""))
//        .alwaysReadResponseBody(false)
//        .build()
//
//    clientBuilder.addInterceptor(chuckerInterceptor)

    addTimeout(clientBuilder)

    clientBuilder.addInterceptor(loggingInterceptor())


    clientBuilder.retryOnConnectionFailure(false)
    return clientBuilder.build()
}


/**
 * This method add timeout for CRUD API calls
 * @param clientBuilder Builder
 */
private fun addTimeout(clientBuilder: OkHttpClient.Builder) {
    clientBuilder
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
}

/**
 * This method add logging interceptop
 * The logs are at level body and will only be added for debug mode
 * @return HttpLoggingInterceptor
 */
fun loggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return httpLoggingInterceptor
}

/**
 * This function creates retrofit for the network service to use.
 * @param okHttpClient OkHttpClient
 * @return Retrofit
 */
fun createRetrofit(okHttpClient: OkHttpClient, baseUrl: String = BASE_URL): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

/**
 * Create network api service for api calls
 * @param retrofit Retrofit
 * @return NetworkApiSource
 */
fun createStatusService(retrofit: Retrofit): NetworkApiSource =
    retrofit.create(NetworkApiSource::class.java)
