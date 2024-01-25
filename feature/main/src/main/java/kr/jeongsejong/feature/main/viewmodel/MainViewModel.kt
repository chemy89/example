package kr.jeongsejong.feature.main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.jeongsejong.core.data.repository.AccountRepository
import kr.jeongsejong.core.data.repository.ImageSearchRepository
import kr.jeongsejong.core.data.vo.DocumentData
import kr.jeongsejong.core.social.kakao.KakaoSdkDelegate
import timber.log.Timber
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
            accountRepository.accessToken = kakaoAccessToken
        }
    }

}