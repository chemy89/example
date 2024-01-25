package kr.jeongsejong.feature.main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.jeongsejong.core.data.repository.AccountRepository
import kr.jeongsejong.core.data.repository.ImageSearchRepository
import kr.jeongsejong.core.data.repository.LocalSupplementRepository
import kr.jeongsejong.core.data.vo.BlockAction
import kr.jeongsejong.core.data.vo.DocumentData
import kr.jeongsejong.core.data.vo.SaveAction
import kr.jeongsejong.core.social.kakao.KakaoSdkDelegate
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val imageSearchRepository: ImageSearchRepository,
    private val localSupplementRepository: LocalSupplementRepository,
    private val kakaoSdkDelegate: KakaoSdkDelegate,
) : ViewModel() {

    sealed class DocumentDataModification {
        data class Edit(val data: DocumentData) : DocumentDataModification()
        data class Remove(val data: DocumentData) : DocumentDataModification()
    }

    private val _documentModificationEvents = MutableStateFlow<List<DocumentDataModification>>(emptyList())

    private val _documentList = MutableSharedFlow<Flow<PagingData<DocumentData>>>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val documentList = _documentList
        .map { it.first() }
        .cachedIn(viewModelScope)
        .combine(_documentModificationEvents) { pagingData, modifications ->
            modifications.fold(pagingData) { acc, event ->
                applyModificationEvents(acc, event)
            }
    }

    private val _searchKeywordFlow = MutableStateFlow("")
    val searchKeywordFlow = _searchKeywordFlow.asStateFlow()

    val isLoggedInFlow = accountRepository.isLoggedInFlow

    init {
        viewModelScope.launch {
            localSupplementRepository.saveActionFlow.collectLatest {
                _documentModificationEvents.value += DocumentDataModification.Edit(it.item)
            }
        }

        viewModelScope.launch {
            localSupplementRepository.blockActionFlow.collectLatest { action ->
                when (action) {
                    is BlockAction.Add -> _documentModificationEvents.value += DocumentDataModification.Remove(action.item)
                    is BlockAction.Remove -> {
                        _documentModificationEvents.update { list ->
                            list.filter {
                                it is DocumentDataModification.Remove && it.data.thumbnailUrl != action.item.thumbnailUrl
                            }
                        }
                    }
                }
            }
        }
    }

    fun setSearchKeyword(keyword: String) {
        _searchKeywordFlow.update { keyword }
    }

    fun searchImage() = viewModelScope.launch {
        val result = imageSearchRepository.searchImage(searchKeywordFlow.value)
        _documentList.emit(result)
    }

    fun blockItem(item: DocumentData) = viewModelScope.launch {
        val action = BlockAction.Add(item)
        localSupplementRepository.setBlockAction(action)
    }

    fun toggleSaved(item: DocumentData) = viewModelScope.launch {
        val action = if (item.isSaved) {
            SaveAction.Remove(item.copy(isSaved = false))
        } else {
            SaveAction.Add(item.copy(isSaved = true))
        }
        localSupplementRepository.setSaveAction(action)
    }

    private fun applyModificationEvents(paging: PagingData<DocumentData>, modification: DocumentDataModification): PagingData<DocumentData> {
        return when (modification) {
            is DocumentDataModification.Edit -> {
                paging.map {
                    if (modification.data.thumbnailUrl == it.thumbnailUrl) {
                        modification.data
                    } else {
                        it
                    }
                }
            }

            is DocumentDataModification.Remove -> {
                paging.filter { modification.data.thumbnailUrl != it.thumbnailUrl }
            }
        }
    }

    fun requestKakaoLogin(context: Context) = viewModelScope.launch {
        runCatching {
            requireNotNull(kakaoSdkDelegate.login(context))
        }.mapCatching { kakaoAccessToken ->
            accountRepository.oAuthToken = kakaoAccessToken
        }
    }

}