package io.novafoundation.nova.common.data.storage

import android.content.SharedPreferences
import io.novafoundation.nova.common.utils.safeOffer
import io.novafoundation.nova.core.model.Language
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onCompletion

class PreferencesImpl(
    private val sharedPreferences: SharedPreferences
) : Preferences {

    /*
    SharedPreferencesImpl stores listeners in a WeakHashMap,
    meaning listener is subject to GC if it is not kept anywhere else.
    This is not a problem until a stringFlow() call is followed later by shareIn() or stateIn(),
    which cause listener to be GC-ed (TODO - research why).
    To avoid that, store strong references to listeners until corresponding flow is closed.
    */
    private val listeners = mutableSetOf<SharedPreferences.OnSharedPreferenceChangeListener>()

    companion object {

        private const val PREFS_SELECTED_LANGUAGE = "selected_language"
    }

    override fun contains(field: String) = sharedPreferences.contains(field)

    override fun putString(field: String, value: String?) {
        sharedPreferences.edit().putString(field, value).apply()
    }

    override fun getString(field: String, defaultValue: String): String {
        return sharedPreferences.getString(field, defaultValue) ?: defaultValue
    }

    override fun getString(field: String): String? {
        return sharedPreferences.getString(field, null)
    }

    override fun putBoolean(field: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(field, value).apply()
    }

    override fun getBoolean(field: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(field, defaultValue)
    }

    override fun putInt(field: String, value: Int) {
        sharedPreferences.edit().putInt(field, value).apply()
    }

    override fun getInt(field: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(field, defaultValue)
    }

    override fun putLong(field: String, value: Long) {
        sharedPreferences.edit().putLong(field, value).apply()
    }

    override fun getLong(field: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(field, defaultValue)
    }

    override fun getCurrentLanguage(): Language? {
        return if (sharedPreferences.contains(PREFS_SELECTED_LANGUAGE)) {
            Language(sharedPreferences.getString(PREFS_SELECTED_LANGUAGE, "")!!)
        } else {
            null
        }
    }

    override fun saveCurrentLanguage(languageIsoCode: String) {
        sharedPreferences.edit().putString(PREFS_SELECTED_LANGUAGE, languageIsoCode).apply()
    }

    override fun removeField(field: String) {
        sharedPreferences.edit().remove(field).apply()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun stringFlow(
        field: String,
        initialValueProducer: (suspend () -> String)?
    ): Flow<String?> = callbackFlow {
        if (contains(field)) {
            send(getString(field))
        } else {
            val initialValue = initialValueProducer?.invoke()

            putString(field, initialValue)

            send(initialValue)
        }

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == field) {
                safeOffer(getString(field))
            }
        }

        listeners.add(listener)
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            listeners.remove(listener)
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    override fun observeBoolean(key: String, initialValue: Boolean): Flow<Boolean> {
        return sharedPreferences.observeKey(key, initialValue)
    }

    override fun observeString(key: String, initialValue: String): Flow<String> {
        return sharedPreferences.observeKey(key, initialValue)
    }

    inline fun <reified T> SharedPreferences.observeKey(key: String, default: T): Flow<T> {
        val flow = MutableStateFlow(getItem(key, default))

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
            if (key == k) {
                flow.value = getItem(key, default)!!
            }
        }
        registerOnSharedPreferenceChangeListener(listener)

        return flow
            .onCompletion { unregisterOnSharedPreferenceChangeListener(listener) }
    }

    inline fun <reified T> SharedPreferences.getItem(key: String, default: T): T {
        @Suppress("UNCHECKED_CAST")
        return when (default) {
            is String -> getString(key, default) as T
            is Int -> getInt(key, default) as T
            is Long -> getLong(key, default) as T
            is Boolean -> getBoolean(key, default) as T
            is Float -> getFloat(key, default) as T
            is Set<*> -> getStringSet(key, default as Set<String>) as T
            is MutableSet<*> -> getStringSet(key, default as MutableSet<String>) as T
            else -> throw IllegalArgumentException("generic type not handle ${T::class.java.name}")
        }
    }
}
