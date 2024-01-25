package kr.jeongsejong.feature.main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.jeongsejong.core.data.repository.AccountRepository
import kr.jeongsejong.core.social.kakao.KakaoSdkDelegate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val kakaoSdkDelegate: KakaoSdkDelegate,
) : ViewModel() {

    val isLoggedInFlow = accountRepository.isLoggedInFlow

    fun requestKakaoLogin(context: Context) = viewModelScope.launch {
        runCatching {
            requireNotNull(kakaoSdkDelegate.login(context))
        }.mapCatching { kakaoAccessToken ->
            accountRepository.oAuthToken = kakaoAccessToken
        }
    }

}