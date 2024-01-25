package kr.jeongsejong.example.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.jeongsejong.core.persistence.sharedpreferences.EncryptedSharedPreferencesPersistence
import kr.jeongsejong.core.persistence.sharedpreferences.SharedPreferencesPersistence
import kr.jeongsejong.core.persistence.spec.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    @EncryptedSupplement
    fun provideEncryptedSupplementPersistence(@ApplicationContext context: Context): Persistence {
        return EncryptedSharedPreferencesPersistence(context, "supplement")
    }

    @Provides
    @Singleton
    @PlainDefault
    fun providePlainDefaultPersistence(@ApplicationContext context: Context): Persistence {
        return SharedPreferencesPersistence(context, "plain")
    }

}
