package ru.vsu.roadmap.config

/**
 * Экран дорожной карты зафиксирован под **ровно 7 этапов**: координаты UI и логика узлов рассчитаны на 7 шагов.
 * Какую карту показывать, задаёт [ru.vsu.roadmap.utils.SelectedRoadmapStore] после выбора в каталоге.
 */
object RoadmapScreenConfig {
    /** Договорённость клиента и контента: не больше и не меньше. */
    const val EXPECTED_STEP_COUNT = 7
}
