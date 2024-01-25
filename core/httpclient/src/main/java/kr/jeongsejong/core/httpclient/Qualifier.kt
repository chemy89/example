package kr.jeongsejong.core.httpclient

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SharedOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InfoHeaderInterceptor