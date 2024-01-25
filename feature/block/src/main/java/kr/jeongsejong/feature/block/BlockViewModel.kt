package kr.jeongsejong.feature.block

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.jeongsejong.core.data.repository.LocalSupplementRepository
import kr.jeongsejong.core.data.vo.BlockAction
import kr.jeongsejong.core.data.vo.DocumentData
import javax.inject.Inject

@HiltViewModel
class BlockViewModel @Inject constructor(
    private val localSupplementRepository: LocalSupplementRepository
) : ViewModel() {

    val blockedDocumentList = localSupplementRepository.blockedDocumentList

    fun removeBlockedItem(item: DocumentData) = viewModelScope.launch {
        val action = BlockAction.Remove(item)

        localSupplementRepository.setBlockAction(action)
    }

}