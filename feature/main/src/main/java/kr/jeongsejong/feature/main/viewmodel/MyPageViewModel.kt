package kr.jeongsejong.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.jeongsejong.core.data.repository.AccountRepository
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    val oAuthToken: String
        get() = accountRepository.oAuthToken

    fun logout() {
        accountRepository.clearAccountInfo()
    }

}