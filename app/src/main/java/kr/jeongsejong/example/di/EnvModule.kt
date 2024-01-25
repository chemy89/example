package kr.jeongsejong.example.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.jeongsejong.env.Env
import kr.jeongsejong.example.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EnvModule {

    @Binds
    @Singleton
    abstract fun bindEnv(envImpl: EnvImpl): Env
}

class EnvImpl @Inject constructor() : Env {

    override val debug: Boolean = BuildConfig.DEBUG

    override val applicationId: String = BuildConfig.APPLICATION_ID

    override val buildType: String = BuildConfig.BUILD_TYPE

    override val versionCode: Int = BuildConfig.VERSION_CODE

    override val versionName: String = BuildConfig.VERSION_NAME

    override val apiHost: String = BuildConfig.API_HOST

    override val kakaoNativeAppKey: String = BuildConfig.KAKAO_NATIVE_APP_KEY

    override val kakaoRestApiKey: String = BuildConfig.KAKAO_REST_API_KEY

}
