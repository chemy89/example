package kr.jeongsejong.example.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.jeongsejong.core.social.kakao.KakaoSdkDelegate
import kr.jeongsejong.core.social.kakao.KakaoSdkDelegateImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class KakaoSdkDelegateModule {

    @Singleton
    @Binds
    abstract fun bindKakaoSdkDelegate(impl: KakaoSdkDelegateImpl): KakaoSdkDelegate
}

