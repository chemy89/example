package kr.jeongsejong.core.data.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.jeongsejong.core.local.LocalDocumentData
import kr.jeongsejong.core.network.model.Documents

@Parcelize
data class DocumentData(
    val collection: String,
    val thumbnailUrl: String,
    val imageUrl: String,
    val displaySiteName: String,
    val docUrl: String,
    val datetime: String,
    val isSaved: Boolean,
): Parcelable {
    companion object {
        val empty = DocumentData(
            collection = "noluisse",
            thumbnailUrl = "https://duckduckgo.com/?q=equidem",
            imageUrl = "https://www.google.com/#q=ultricies",
            displaySiteName = "Barney Bowen",
            docUrl = "https://www.google.com/#q=sumo",
            datetime = "ut",
            isSaved = false
        )

        fun Documents.toDocumentData(isSaved: Boolean? = false) = DocumentData(
            collection = this.collection,
            thumbnailUrl = this.thumbnail_url,
            imageUrl = this.image_url,
            displaySiteName = this.display_sitename,
            docUrl = this.doc_url,
            datetime = this.datetime,
            isSaved = isSaved ?: false
        )

        fun LocalDocumentData.toDocumentData() = DocumentData(
            collection = this.collection,
            thumbnailUrl = this.thumbnailUrl,
            imageUrl = this.imageUrl,
            displaySiteName = this.displaySiteName,
            docUrl = this.docUrl,
            datetime = this.datetime,
            isSaved = this.isSaved
        )

//
//        fun List<UserData>.groupByConsonant(): Map<String, List<UserData>> {
//            return this
//                .sortedBy { it.name.lowercase() } // 정렬
//                .groupBy { it.name.lowercase()[0].toString() } // 초성으로 그룹화
//        }
//
//        fun Map<String, List<UserData>>.toUserItemList(): List<UserItem> {
//            return buildList {
//                this@toUserItemList.forEach { (key, value) ->
//                    add(UserItem.Header(key))
//                    addAll(value.map { userData -> UserItem.Contents(userData) })
//                }
//            }
//        }
    }
}