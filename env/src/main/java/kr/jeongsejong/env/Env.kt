package kr.jeongsejong.env

interface Env {
    val debug: Boolean
    val applicationId: String
    val buildType: String
    val versionCode: Int
    val versionName: String
    val apiHost: String
    val kakaoNativeAppKey: String
    val kakaoRestApiKey: String
}
