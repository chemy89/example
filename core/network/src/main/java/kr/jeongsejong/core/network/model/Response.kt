package kr.jeongsejong.core.network.model

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("meta")
    val meta: Meta?,

    @SerializedName("documents")
    val documents: List<Documents>?,
)