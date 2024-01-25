package kr.jeongsejong.core.network.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kr.jeongsejong.core.network.Api
import kr.jeongsejong.core.network.NetworkModule
import kr.jeongsejong.core.network.model.Documents

class NetworkSearchImagePagingDataSource<T : Any> @AssistedInject constructor(
    @NetworkModule.AppAPi private val api: Api,
    @Assisted private val keyword: String,
    @Assisted private val mapper: (Documents) -> T
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1

        return try {
            val result = api.searchImage(keyword, page, params.loadSize)
            val isReachedEnd = result.meta?.is_end ?: true
            val items = result.documents ?: emptyList()

            LoadResult.Page(
                data = items.map { mapper.invoke(it) },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (isReachedEnd) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(keyword: String, mapper: (Documents) -> Any): NetworkSearchImagePagingDataSource<Any>
    }

}
