package ru.vsu.roadmap.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER_ID = "user_id"
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    fun clearUserId() {
        prefs.edit().remove(KEY_USER_ID).apply()
    }

    /** Полная очистка данных сессии при выходе. */
    fun clearSession() {
        prefs.edit().remove(KEY_TOKEN).remove(KEY_USER_ID).apply()
    }
    
    fun saveUserId(id: Long) {
        prefs.edit().putLong(KEY_USER_ID, id).apply()
    }
    
    fun getUserId(): Long {
        return prefs.getLong(KEY_USER_ID, -1)
    }
}
