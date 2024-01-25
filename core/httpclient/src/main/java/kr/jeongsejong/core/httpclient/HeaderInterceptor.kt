package kr.jeongsejong.core.httpclient

import kr.jeongsejong.env.Env
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val env: Env,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", "KakaoAK ${env.kakaoRestApiKey}")
            .build()

        return chain.proceed(newRequest)
    }
}
