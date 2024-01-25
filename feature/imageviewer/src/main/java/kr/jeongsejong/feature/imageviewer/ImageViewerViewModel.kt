package kr.jeongsejong.feature.imageviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.jeongsejong.core.data.repository.LocalSupplementRepository
import kr.jeongsejong.core.data.vo.BlockAction
import kr.jeongsejong.core.data.vo.DocumentData
import kr.jeongsejong.core.data.vo.SaveAction

@HiltViewModel(assistedFactory = ImageViewerViewModel.ViewModelFactory::class)
class ImageViewerViewModel @AssistedInject constructor(
    @Assisted private val item: DocumentData,
    private val localSupplementRepository: LocalSupplementRepository
) : ViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(item: DocumentData): ImageViewerViewModel
    }

    private val _documentItemFlow = MutableStateFlow(item)
    val documentItemFlow = _documentItemFlow.asStateFlow()

    init {
        viewModelScope.launch {
            localSupplementRepository.saveActionFlow.collectLatest { action ->
                _documentItemFlow.update {
                    it.copy(isSaved = action.item.isSaved)
                }
            }
        }
    }

    fun blockItem(item: DocumentData) = viewModelScope.launch {
        localSupplementRepository.setBlockAction(BlockAction.Add(item))
    }

    fun toggleSaved(item: DocumentData) = viewModelScope.launch {
        val action = if (item.isSaved) {
            SaveAction.Remove(item.copy(isSaved = false))
        } else {
            SaveAction.Add(item.copy(isSaved = true))
        }

        localSupplementRepository.setSaveAction(action)
    }

}