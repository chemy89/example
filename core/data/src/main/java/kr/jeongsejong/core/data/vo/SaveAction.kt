package kr.jeongsejong.core.data.vo

sealed class SaveAction(val item: DocumentData) {
    class Add(item: DocumentData): SaveAction(item)
    class Remove(item: DocumentData): SaveAction(item)
}