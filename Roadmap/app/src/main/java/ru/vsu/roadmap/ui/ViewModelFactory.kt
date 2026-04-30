package ru.vsu.roadmap.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.vsu.roadmap.data.repository.AuthRepository
import ru.vsu.roadmap.data.repository.RoadmapRepository
import ru.vsu.roadmap.data.repository.UserRepository
import ru.vsu.roadmap.ui.viewmodel.CatalogViewModel
import ru.vsu.roadmap.ui.viewmodel.EditProfileViewModel
import ru.vsu.roadmap.ui.viewmodel.HomeViewModel
import ru.vsu.roadmap.ui.viewmodel.LoginViewModel
import ru.vsu.roadmap.ui.viewmodel.ProfileViewModel
import ru.vsu.roadmap.ui.viewmodel.RegisterViewModel
import ru.vsu.roadmap.ui.viewmodel.RoadmapViewModel
import ru.vsu.roadmap.ui.viewmodel.UserInfoViewModel
import ru.vsu.roadmap.utils.SelectedRoadmapStore

class ViewModelFactory(
    private val application: Application,
    private val authRepository: AuthRepository,
    private val roadmapRepository: RoadmapRepository,
    private val userRepository: UserRepository,
    private val selectedRoadmapStore: SelectedRoadmapStore,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(application, authRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(application, authRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(roadmapRepository, selectedRoadmapStore) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository, roadmapRepository, selectedRoadmapStore) as T
            }
            modelClass.isAssignableFrom(UserInfoViewModel::class.java) -> {
                UserInfoViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(CatalogViewModel::class.java) -> {
                CatalogViewModel(roadmapRepository, selectedRoadmapStore) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RoadmapViewModel::class.java) -> {
                RoadmapViewModel(application, roadmapRepository, selectedRoadmapStore) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
