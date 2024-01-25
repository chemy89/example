package kr.jeongsejong.core.persistence.spec

import android.content.SharedPreferences
import androidx.core.content.edit

interface Persistence : SharedPreferences {

    val name: String

    fun put(key: String, value: Boolean) {
        this.edit(true) { putBoolean(key, value) }
    }

    fun put(key: String, value: String) {
        this.edit(true) { putString(key, value) }
    }

    fun put(key: String, value: Float) {
        this.edit(true) { putFloat(key, value) }
    }

    fun put(key: String, value: Long) {
        this.edit(true) { putLong(key, value) }
    }

    fun put(key: String, value: Int) {
        this.edit(true) { putInt(key, value) }
    }

    fun remove(key: String) {
        this.edit(true) { this.remove(key) }
    }

    fun clear() {
        this.edit(true) { this.clear() }
    }

    companion object {

        fun create(name: String, sharedPreferences: SharedPreferences): Persistence = object :
            Persistence {

            override val name: String
                get() = name

            override fun getAll(): MutableMap<String, *> = sharedPreferences.all

            override fun getString(key: String?, defValue: String?): String? = sharedPreferences.getString(key, defValue)

            override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? = sharedPreferences.getStringSet(key, defValues)

            override fun getInt(key: String?, defValue: Int): Int = sharedPreferences.getInt(key, defValue)

            override fun getLong(key: String?, defValue: Long): Long = sharedPreferences.getLong(key, defValue)

            override fun getFloat(key: String?, defValue: Float): Float = sharedPreferences.getFloat(key, defValue)

            override fun getBoolean(key: String?, defValue: Boolean): Boolean = sharedPreferences.getBoolean(key, defValue)

            override fun contains(key: String?): Boolean = sharedPreferences.contains(key)

            override fun edit(): SharedPreferences.Editor = sharedPreferences.edit()

            override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
                sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            }

            override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }
    }
}
