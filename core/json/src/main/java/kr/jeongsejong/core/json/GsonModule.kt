package kr.jeongsejong.core.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.OffsetDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GsonModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val builder = GsonBuilder().setLenient()
        builder.registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeAdapter())
        builder.setPrettyPrinting()

        return builder.create()
    }
}
