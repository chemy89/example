package kr.jeongsejong.core.local.datasource

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.jeongsejong.core.persistence.spec.EncryptedSupplement
import kr.jeongsejong.core.persistence.spec.Persistence
import javax.inject.Singleton

class LocalAccountDataSource internal constructor(
    private val persistence: Persistence
) {

    val isLoggedIn: Boolean
        get() = oAuthToken.isNotBlank()

    var oAuthToken: String
        get() = persistence.getString("oauth_token", "").orEmpty()
        set(value) {
            persistence.put("oauth_token", value)
        }

    fun clearAccessToken() {
        persistence.put("oauth_token", "")
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
