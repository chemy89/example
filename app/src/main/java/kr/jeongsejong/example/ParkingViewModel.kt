package kr.jeongsejong.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kr.jeongsejong.core.data.repository.AccountRepository
import kr.jeongsejong.core.data.repository.ImageSearchRepository
import kr.jeongsejong.core.data.vo.DocumentData
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParkingViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    val isLoggedIn: Boolean
        get() = accountRepository.isLoggedInFlow.value

}