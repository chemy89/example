package kr.jeongsejong.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.jeongsejong.core.data.repository.LocalSupplementRepository
import kr.jeongsejong.core.data.vo.DocumentData
import kr.jeongsejong.core.data.vo.SaveAction
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val localSupplementRepository: LocalSupplementRepository
) : ViewModel() {

    val savedDocumentList = localSupplementRepository.savedDocumentList

    fun removeSavedItem(item: DocumentData) = viewModelScope.launch {
        val newItem = item.copy(isSaved = false)
        val action = SaveAction.Remove(newItem)

        localSupplementRepository.setSaveAction(action)
    }

}