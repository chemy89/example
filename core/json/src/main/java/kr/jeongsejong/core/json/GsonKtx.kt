package kr.jeongsejong.core.json

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJsonList(json: String): List<T> {
    return runCatching {
        val type = object : TypeToken<List<T>>() {}.type
        this.fromJson<List<T>>(json, type)
    }.onFailure {
        Log.e("APP", it.message.orEmpty())
    }.getOrDefault(emptyList())
}
