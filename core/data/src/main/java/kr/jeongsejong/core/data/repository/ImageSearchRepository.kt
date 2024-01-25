package kr.jeongsejong.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kr.jeongsejong.core.data.vo.DocumentData
import kr.jeongsejong.core.data.vo.DocumentData.Companion.toDocumentData
import kr.jeongsejong.core.local.LocalAccountDataSource
import kr.jeongsejong.core.local.LocalSupplementDataSource
import kr.jeongsejong.core.network.datasource.NetworkSearchImagePagingDataSource
import javax.inject.Inject
import javax.inject.Singleton


interface ImageSearchRepository {
    fun searchImage(keyword: String): Flow<PagingData<DocumentData>>
}

class ImageSearchRepositoryImpl @Inject constructor(
    private val networkSearchImagePagingDataSource: NetworkSearchImagePagingDataSource.Factory,
    private val localSupplementDataSource: LocalSupplementDataSource
) : ImageSearchRepository {

    private val savedThumbnailList: List<String>
        get() = localSupplementDataSource.current.savedDocumentList
            .map { it.toDocumentData() }
            .map { it.thumbnailUrl }

    @Suppress("UNCHECKED_CAST")
    override fun searchImage(keyword: String): Flow<PagingData<DocumentData>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            networkSearchImagePagingDataSource.create(keyword) {
                it.toDocumentData(savedThumbnailList.contains(it.thumbnail_url))
            } as NetworkSearchImagePagingDataSource<DocumentData>
        },
    ).flow

}

@Module
@InstallIn(SingletonComponent::class)
abstract class ImageSearchRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindImageSearchRepository(
        impl: ImageSearchRepositoryImpl,
    ): ImageSearchRepository
}