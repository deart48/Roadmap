package ru.vsu.roadmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.vsu.roadmap.data.api.ApiClient
import ru.vsu.roadmap.data.repository.AuthRepository
import ru.vsu.roadmap.data.repository.RoadmapRepository
import ru.vsu.roadmap.data.repository.UserRepository
import ru.vsu.roadmap.ui.RoadmapApp
import ru.vsu.roadmap.ui.ViewModelFactory
import ru.vsu.roadmap.ui.theme.RoadmapTheme
import ru.vsu.roadmap.utils.SelectedRoadmapStore
import ru.vsu.roadmap.utils.TokenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Dependency Injection (Manual)
        val tokenManager = TokenManager(this)
        val selectedRoadmapStore = SelectedRoadmapStore(this)
        val api = ApiClient.api
        val authRepository = AuthRepository(api, tokenManager)
        val roadmapRepository = RoadmapRepository(api, tokenManager)
        val userRepository = UserRepository(api, tokenManager)
        
        val viewModelFactory = ViewModelFactory(
            application = application,
            authRepository = authRepository,
            roadmapRepository = roadmapRepository,
            userRepository = userRepository,
            selectedRoadmapStore = selectedRoadmapStore,
        )
        
        enableEdgeToEdge()
        setContent {
            RoadmapTheme {
                RoadmapApp(
                    viewModelFactory = viewModelFactory,
                    selectedRoadmapStore = selectedRoadmapStore,
                    authRepository = authRepository,
                )
            }
        }
    }
}
