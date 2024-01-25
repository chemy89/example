package kr.jeongsejong.core.httpclient

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import kr.jeongsejong.env.Env
import okhttp3.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {

    @Provides
    @Singleton
    @SharedOkHttpClient
    fun provideOkHttpClient(
        @InfoHeaderInterceptor headerInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @InfoHeaderInterceptor
    fun providesAppInfoHeaderInterceptor(env: Env): Interceptor {
        return HeaderInterceptor(env)
    }

}
