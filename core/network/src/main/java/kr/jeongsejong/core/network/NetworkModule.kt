package kr.jeongsejong.core.network

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import kr.jeongsejong.core.httpclient.SharedOkHttpClient
import kr.jeongsejong.env.Env
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AppAPi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AppRetrofit

    @Provides
    @Singleton
    @AppAPi
    fun provideApi(@AppRetrofit retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    @Singleton
    @AppRetrofit
    fun provideRetrofit(
        @SharedOkHttpClient httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        env: Env,
    ): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(env.apiHost)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(json: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(json)
    }
}
