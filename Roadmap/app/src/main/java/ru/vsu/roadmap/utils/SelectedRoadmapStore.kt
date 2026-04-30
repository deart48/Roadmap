package ru.vsu.roadmap.utils

import android.content.Context

/**
 * Локально запоминает, какую дорожную карту пользователь выбрал для вкладки «Roadmap».
 */
class SelectedRoadmapStore(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getSelectedRoadmapId(): Long? {
        val v = prefs.getLong(KEY_SELECTED_ROADMAP_ID, NO_SELECTION)
        return if (v == NO_SELECTION) null else v
    }

    fun setSelectedRoadmapId(id: Long) {
        prefs.edit().putLong(KEY_SELECTED_ROADMAP_ID, id).apply()
    }

    /** Вызывать при выходе из аккаунта. */
    fun clearSelection() {
        prefs.edit().remove(KEY_SELECTED_ROADMAP_ID).apply()
    }

    private companion object {
        const val PREFS_NAME = "roadmap_selection"
        const val KEY_SELECTED_ROADMAP_ID = "selected_roadmap_id"
        const val NO_SELECTION = -1L
    }
}
