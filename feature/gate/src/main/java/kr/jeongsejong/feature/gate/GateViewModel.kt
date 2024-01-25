package kr.jeongsejong.feature.gate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.jeongsejong.core.data.repository.AccountRepository
import kr.jeongsejong.core.social.kakao.KakaoSdkDelegate
import javax.inject.Inject

@HiltViewModel
class GateViewModel @Inject constructor(
    private val kakaoSdkDelegate: KakaoSdkDelegate,
    private val accountRepository: AccountRepository
) : ViewModel() {

    enum class LoginState {
        NONE, SUCCESS, ERROR, USER_CANCELED
    }

    private val _loginStateState = MutableStateFlow(LoginState.NONE)
    val loginState = _loginStateState.asStateFlow()

    fun requestKakaoLogin(context: Context) = viewModelScope.launch {
        runCatching {
            requireNotNull(kakaoSdkDelegate.login(context))
        }.mapCatching { kakaoAccessToken ->
            if(kakaoAccessToken.isNotBlank()) {
                accountRepository.oAuthToken = kakaoAccessToken
                LoginState.SUCCESS
            } else {
                LoginState.USER_CANCELED
            }
        }.onSuccess {
            _loginStateState.emit(it)
        }.onFailure {
            _loginStateState.emit(LoginState.ERROR)
        }
    }

}