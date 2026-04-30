-- Дополнение дорожной карты id=1 (Android Developer) до ровно 7 шагов.
-- Выполни, если база уже создана со старым сидом (было только 3 шага у roadmap_id=1).

INSERT INTO roadmap_steps (roadmap_id, title, description, image_url, order_index)
SELECT 1, 'Architecture & State', 'MVVM, ViewModel, UI state.', NULL, 4
WHERE NOT EXISTS (SELECT 1 FROM roadmap_steps WHERE roadmap_id = 1 AND order_index = 4);

INSERT INTO roadmap_steps (roadmap_id, title, description, image_url, order_index)
SELECT 1, 'Data Layer', 'Room, repositories, offline-first basics.', NULL, 5
WHERE NOT EXISTS (SELECT 1 FROM roadmap_steps WHERE roadmap_id = 1 AND order_index = 5);

INSERT INTO roadmap_steps (roadmap_id, title, description, image_url, order_index)
SELECT 1, 'Networking', 'Retrofit, OkHttp, serialization.', NULL, 6
WHERE NOT EXISTS (SELECT 1 FROM roadmap_steps WHERE roadmap_id = 1 AND order_index = 6);

INSERT INTO roadmap_steps (roadmap_id, title, description, image_url, order_index)
SELECT 1, 'Next: Fullstack', 'Следующий этап: бэкенд, БД и интеграция с клиентом.', NULL, 7
WHERE NOT EXISTS (SELECT 1 FROM roadmap_steps WHERE roadmap_id = 1 AND order_index = 7);
