package kr.jeongsejong.core.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kr.jeongsejong.core.local.LocalAccountDataSource
import javax.inject.Inject
import javax.inject.Singleton

interface AccountRepository {
    val isLoggedInFlow: StateFlow<Boolean>
    var accessToken: String

    fun clearAccountInfo()
}

class AccountRepositoryImpl @Inject constructor(
    private val localAccountDataSource: LocalAccountDataSource,
) : AccountRepository {
    private val _isLoggedInFlow = MutableStateFlow(localAccountDataSource.isLogin)
    override val isLoggedInFlow = _isLoggedInFlow.asStateFlow()

    override var accessToken: String
        get() = localAccountDataSource.accessToken
        set(value) {
            localAccountDataSource.accessToken = value
            _isLoggedInFlow.update { value.isNotBlank() }
        }

    override fun clearAccountInfo() {
        localAccountDataSource.clear()
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AccountRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        impl: AccountRepositoryImpl,
    ): AccountRepository
}
