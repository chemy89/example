package kr.jeongsejong.core.social.kakao

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.jeongsejong.env.Env
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume

class KakaoSdkDelegateImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val env: Env
) : KakaoSdkDelegate {

    internal sealed class KakaoLoginResult {
        data object Init : KakaoLoginResult()

        class Success(val oauthToken: OAuthToken) : KakaoLoginResult()
        class Error(val throwable: Throwable?) : KakaoLoginResult()

        data object LoginViaKakaoTalkFailed : KakaoLoginResult()
        data object UserCancelled : KakaoLoginResult()

        inline fun next(next: (KakaoLoginResult) -> KakaoLoginResult): KakaoLoginResult {
            val mutated = when (this) {
                is Error -> this
                is UserCancelled -> this
                else -> next.invoke(this)
            }
            Timber.tag("KakaoLogin").v("${this::class.java.simpleName} -> ${mutated::class.java.simpleName}")
            return mutated
        }
    }


    override fun init() {
        KakaoSdk.init(context, env.kakaoNativeAppKey)
    }

    override suspend fun login(context: Context): String? {
        val result = KakaoLoginResult.Init.next {
            val isKakaoTalkLoginAvailable = UserApiClient.instance.isKakaoTalkLoginAvailable(context)
            if (isKakaoTalkLoginAvailable) {
                loginWithKakaoTalk(context)
            } else {
                loginWithKakaoAccount(context)
            }
        }.next {
            when (it) {
                is KakaoLoginResult.LoginViaKakaoTalkFailed -> loginWithKakaoAccount(context)
                else -> it
            }
        }
        return when (result) {
            is KakaoLoginResult.Success -> result.oauthToken.accessToken
            is KakaoLoginResult.UserCancelled -> ""
            else -> null
        }
    }

    private suspend fun loginWithKakaoTalk(context: Context): KakaoLoginResult {
        return suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                val userCanceled = error is ClientError && error.reason == ClientErrorCause.Cancelled

                when {
                    continuation.isActive && userCanceled -> continuation.resume(KakaoLoginResult.UserCancelled)
                    continuation.isActive && error != null -> continuation.resume(KakaoLoginResult.LoginViaKakaoTalkFailed)
                    continuation.isActive && token != null -> continuation.resume(KakaoLoginResult.Success(token))
                    continuation.isActive && token == null -> continuation.resume(KakaoLoginResult.Error(RuntimeException("카카오톡에서 kakaoOAuthToken 획득 실패")))
                }
            }
        }
    }

    private suspend fun loginWithKakaoAccount(context: Context): KakaoLoginResult {
        return suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                val userCanceled = error is ClientError && error.reason == ClientErrorCause.Cancelled

                when {
                    continuation.isActive && userCanceled -> continuation.resume(KakaoLoginResult.UserCancelled)
                    continuation.isActive && error != null -> continuation.resume(KakaoLoginResult.Error(error))
                    continuation.isActive && token != null -> continuation.resume(KakaoLoginResult.Success(token))
                    continuation.isActive && token == null -> continuation.resume(KakaoLoginResult.Error(RuntimeException("카카오 계정에서 kakaoOAuthToken 획득 실패")))
                }
            }
        }
    }
}
