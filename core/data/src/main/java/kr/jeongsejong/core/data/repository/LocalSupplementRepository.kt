package kr.jeongsejong.core.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kr.jeongsejong.core.common.MainDispatcher
import kr.jeongsejong.core.data.vo.BlockAction
import kr.jeongsejong.core.data.vo.DocumentData
import kr.jeongsejong.core.data.vo.DocumentData.Companion.toDocumentData
import kr.jeongsejong.core.data.vo.SaveAction
import kr.jeongsejong.core.local.LocalAccountDataSource
import kr.jeongsejong.core.local.LocalSupplementDataSource
import kr.jeongsejong.core.local.LocalDocumentData
import javax.inject.Inject
import javax.inject.Singleton

interface LocalSupplementRepository {

    val savedDocumentList: StateFlow<List<DocumentData>>
    val blockedDocumentList: StateFlow<List<DocumentData>>

    val saveActionFlow: SharedFlow<SaveAction>
    val blockActionFlow: SharedFlow<BlockAction>

    suspend fun setSaveAction(action: SaveAction)
    suspend fun setBlockAction(action: BlockAction)
}

class LocalSupplementRepositoryImpl @Inject constructor(
    private val localSupplementDataSource: LocalSupplementDataSource,
    private val localAccountDataSource: LocalAccountDataSource,
    @MainDispatcher dispatcher: CoroutineDispatcher,
): CoroutineScope by CoroutineScope(dispatcher + SupervisorJob()), LocalSupplementRepository {

    override val savedDocumentList = localSupplementDataSource.localSupplementFlow
        .map {
            it.savedDocumentList.map { item -> item.toDocumentData() }
        }.stateIn(
            scope = CoroutineScope(dispatcher),
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    override val blockedDocumentList = localSupplementDataSource.localSupplementFlow
        .map {
            it.blockedDocumentList.map { item -> item.toDocumentData() }
        }.stateIn(
            scope = CoroutineScope(dispatcher),
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _saveActionFlow = MutableSharedFlow<SaveAction>()
    override val saveActionFlow = _saveActionFlow.asSharedFlow()

    private val _blockActionFlow = MutableSharedFlow<BlockAction>()
    override val blockActionFlow = _blockActionFlow.asSharedFlow()

    private val oAuthToken: String
        get() = localAccountDataSource.accessToken

    init {
        launch {
            _saveActionFlow.collectLatest {
                when (it) {
                    is SaveAction.Add -> addSavedData(it.item)
                    is SaveAction.Remove -> removeSavedData(it.item)
                }
            }
        }

        launch {
            _blockActionFlow.collectLatest {
                when (it) {
                    is BlockAction.Add -> addBlockedData(it.item)
                    is BlockAction.Remove -> removeBlockedData(it.item)
                }
            }
        }
    }

    override suspend fun setSaveAction(action: SaveAction) {
        _saveActionFlow.emit(action)
    }

    override suspend fun setBlockAction(action: BlockAction) {
        _blockActionFlow.emit(action)
    }

    private fun addSavedData(item: DocumentData) {
        localSupplementDataSource.update(oAuthToken) {
            val newItem = LocalDocumentData(
                collection = item.collection,
                thumbnailUrl = item.thumbnailUrl,
                imageUrl = item.imageUrl,
                displaySiteName = item.displaySiteName,
                docUrl = item.docUrl,
                datetime = item.datetime,
                isSaved = true,
            )
            val newList = it.savedDocumentList + newItem

            it.copy(savedDocumentList = newList)
        }
    }

    private fun removeSavedData(item: DocumentData) {
        localSupplementDataSource.update(oAuthToken) {
            val newList = it.savedDocumentList.filter { savedItem -> savedItem.thumbnailUrl != item.thumbnailUrl }
            it.copy(savedDocumentList = newList)
        }
    }

    private fun addBlockedData(item: DocumentData) {
        localSupplementDataSource.update(oAuthToken) {
            val newItem = LocalDocumentData(
                collection = item.collection,
                thumbnailUrl = item.thumbnailUrl,
                imageUrl = item.imageUrl,
                displaySiteName = item.displaySiteName,
                docUrl = item.docUrl,
                datetime = item.datetime,
                isSaved = false,
            )
            val newSavedDocumentList = it.savedDocumentList.filter { savedItem -> savedItem.thumbnailUrl != item.thumbnailUrl }
            val newBlockedDocumentList = it.blockedDocumentList + newItem

            it.copy(
                savedDocumentList = newSavedDocumentList,
                blockedDocumentList = newBlockedDocumentList
            )
        }
    }

    private fun removeBlockedData(item: DocumentData) {
        localSupplementDataSource.update(oAuthToken) {
            val newList = it.blockedDocumentList.filter { savedItem -> savedItem.thumbnailUrl != item.thumbnailUrl }
            it.copy(blockedDocumentList = newList)
        }
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalSupplementRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLocalSupplementRepository(
        impl: LocalSupplementRepositoryImpl,
    ): LocalSupplementRepository
}