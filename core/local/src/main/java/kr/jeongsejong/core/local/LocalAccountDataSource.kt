package kr.jeongsejong.core.local

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kr.jeongsejong.core.persistence.spec.EncryptedSupplement
import kr.jeongsejong.core.persistence.spec.Persistence
import javax.inject.Singleton

class LocalAccountDataSource internal constructor(
    private val persistence: Persistence
) {

    val isLogin: Boolean
        get() = accessToken.isNotBlank()

    var accessToken: String
        get() = persistence.getString("oauth_token", "").orEmpty()
        set(value) {
            persistence.put("oauth_token", value)
        }

    fun clear() {
        persistence.clear()
    }

}

@Module
@InstallIn(SingletonComponent::class)
object LocalAccountDataSourceModule {

    @Provides
    @Singleton
    fun provideLocalAccountDataSource(
        @EncryptedSupplement persistence: Persistence,
    ): LocalAccountDataSource {
        return LocalAccountDataSource(persistence)
    }
}
