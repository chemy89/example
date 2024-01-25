package kr.jeongsejong.example

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kr.jeongsejong.core.social.kakao.KakaoSdkDelegate
import javax.inject.Inject

/**
 * Created by jeongsejong on 2024/01/23
 */
@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var kakaoSdkDelegate: dagger.Lazy<KakaoSdkDelegate>

    override fun onCreate() {
        super.onCreate()

        kakaoSdkDelegate.get().init()
    }

}