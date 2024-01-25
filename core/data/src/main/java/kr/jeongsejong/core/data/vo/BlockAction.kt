package kr.jeongsejong.core.data.vo

sealed class BlockAction(val item: DocumentData) {
    class Add(item: DocumentData): BlockAction(item)
    class Remove(item: DocumentData): BlockAction(item)
}