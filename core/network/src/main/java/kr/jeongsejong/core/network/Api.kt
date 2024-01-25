package kr.jeongsejong.core.network

import kr.jeongsejong.core.network.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by jeongsejong on 2024/01/04
 */
interface Api {

    @GET("search/image")
    suspend fun searchImage(
        @Query("query") keyword: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response

}