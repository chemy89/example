package kr.jeongsejong.core.local

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kr.jeongsejong.core.common.IoDispatcher
import kr.jeongsejong.core.json.fromJsonList
import kr.jeongsejong.core.persistence.spec.EncryptedSupplement
import kr.jeongsejong.core.persistence.spec.Persistence
import javax.inject.Singleton

/**
 * 내부 적재된 데이터를 Handling 합니다.
 */
class LocalSupplementDataSource internal constructor(
    private val persistence: Persistence,
    private val gson: Gson,
    private val dispatcher: CoroutineDispatcher,
) : CoroutineScope by CoroutineScope(dispatcher + SupervisorJob()) {

    private val oAuthToken
        get() = persistence.getString(KEY_OAUTH_TOKEN, "").orEmpty()

    val current: LocalSupplement
        get() = loadFromPersistence(oAuthToken)

    private val _localSupplementFlow = MutableStateFlow(current)
    val localSupplementFlow = _localSupplementFlow.asStateFlow()

    fun update(oAuthToken: String, update: (LocalSupplement) -> LocalSupplement) = launch {
        val newLocalSupplement = update(localSupplementFlow.value)
        saveToPersistence(oAuthToken, newLocalSupplement)
        updateLocalSupplementFlow(newLocalSupplement)
    }

    private fun saveToPersistence(oAuthToken: String, localSupplement: LocalSupplement) {
        persistence.put("${oAuthToken}_$KEY_SAVED_LIST", gson.toJson(localSupplement.savedDocumentList))
        persistence.put("${oAuthToken}_$KEY_BLOCKED_LIST", gson.toJson(localSupplement.blockedDocumentList))
    }

    private fun updateLocalSupplementFlow(localSupplement: LocalSupplement) {
        _localSupplementFlow.update { localSupplement }
    }

    private fun loadFromPersistence(oAuthToken: String) = LocalSupplement(
        savedDocumentList = gson.fromJsonList(persistence.getString("${oAuthToken}_$KEY_SAVED_LIST", null) ?: "[]"),
        blockedDocumentList = gson.fromJsonList(persistence.getString("${oAuthToken}_$KEY_BLOCKED_LIST", null) ?: "[]"),
    )

    companion object {
        private const val KEY_OAUTH_TOKEN = "oauth_token"
        private const val KEY_SAVED_LIST = "saved_list"
        private const val KEY_BLOCKED_LIST = "blocked_list"
    }
}

@Module
@InstallIn(SingletonComponent::class)
object LocalSupplementDataSourceModule {

    @Provides
    @Singleton
    fun provideLocalSupplementDataSource(
        @EncryptedSupplement persistence: Persistence,
        gson: Gson,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): LocalSupplementDataSource {
        return LocalSupplementDataSource(persistence, gson, dispatcher)
    }
}
