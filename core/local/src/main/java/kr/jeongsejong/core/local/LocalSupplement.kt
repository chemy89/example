package kr.jeongsejong.core.local


data class LocalSupplement(
    val savedDocumentList: List<LocalDocumentData>,
    val blockedDocumentList: List<LocalDocumentData>,
) {

    companion object {
        val empty = LocalSupplement(
            savedDocumentList = emptyList(),
            blockedDocumentList = emptyList(),
        )
    }
}
