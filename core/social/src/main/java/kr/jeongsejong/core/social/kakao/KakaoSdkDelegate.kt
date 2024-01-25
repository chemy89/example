package kr.jeongsejong.core.social.kakao

import android.content.Context

interface KakaoSdkDelegate {

    fun init()

    suspend fun login(context: Context): String?
}
