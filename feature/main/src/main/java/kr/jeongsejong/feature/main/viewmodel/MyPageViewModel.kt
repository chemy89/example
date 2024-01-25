package kr.jeongsejong.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.jeongsejong.core.data.repository.AccountRepository
import kr.jeongsejong.core.data.repository.ImageSearchRepository
import kr.jeongsejong.core.data.vo.DocumentData
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    val oAuthToken: String
        get() = accountRepository.accessToken

    fun logout() {
        accountRepository.clearAccountInfo()
    }

}