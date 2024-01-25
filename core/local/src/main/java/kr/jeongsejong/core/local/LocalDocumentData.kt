package kr.jeongsejong.core.local

data class LocalDocumentData(
    val collection: String,
    val thumbnailUrl: String,
    val imageUrl: String,
    val displaySiteName: String,
    val docUrl: String,
    val datetime: String,
    val isSaved: Boolean,
)