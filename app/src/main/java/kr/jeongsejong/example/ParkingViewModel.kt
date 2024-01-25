package kr.jeongsejong.example

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.jeongsejong.core.data.repository.AccountRepository
import javax.inject.Inject

@HiltViewModel
class ParkingViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    val isLoggedIn: Boolean
        get() = accountRepository.isLoggedInFlow.value

}