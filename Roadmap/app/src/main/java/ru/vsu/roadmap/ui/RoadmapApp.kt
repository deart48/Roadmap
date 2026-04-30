package ru.vsu.roadmap.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.vsu.roadmap.ui.components.NavMenu
import ru.vsu.roadmap.ui.navigation.Routes
import ru.vsu.roadmap.data.repository.AuthRepository
import ru.vsu.roadmap.utils.SelectedRoadmapStore
import ru.vsu.roadmap.ui.screens.ForgotPasswordScreen
import ru.vsu.roadmap.ui.screens.LoginScreen
import ru.vsu.roadmap.ui.screens.RegisterScreen
import ru.vsu.roadmap.ui.screens.SplashScreen
import ru.vsu.roadmap.ui.screens.UserInfoScreen
import ru.vsu.roadmap.ui.screens.VerificationCodeScreen
import ru.vsu.roadmap.ui.screens.WelcomeScreen
import ru.vsu.roadmap.ui.viewmodel.LoginViewModel
import ru.vsu.roadmap.ui.viewmodel.RegisterViewModel
import ru.vsu.roadmap.ui.viewmodel.UserInfoViewModel

@Composable
fun RoadmapApp(
    viewModelFactory: ViewModelFactory,
    selectedRoadmapStore: SelectedRoadmapStore,
    authRepository: AuthRepository,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(onSplashFinished = {
                if (authRepository.isLoggedIn()) {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                } else {
                    navController.navigate(Routes.WELCOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            })
        }
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Routes.LOGIN) },
                onRegisterClick = { navController.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.LOGIN) {
            val loginViewModel: LoginViewModel = viewModel(factory = viewModelFactory)
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                },
                onCreateAccountClick = { navController.navigate(Routes.REGISTER) },
                onForgotPasswordClick = { navController.navigate(Routes.FOGOTPASS) }
            )
        }
        composable(Routes.REGISTER) {
            val registerViewModel: RegisterViewModel = viewModel(factory = viewModelFactory)
            RegisterScreen(
                viewModel = registerViewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.USER_INFO) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.USER_INFO) {
            val userInfoViewModel: UserInfoViewModel = viewModel(factory = viewModelFactory)
            UserInfoScreen(
                viewModel = userInfoViewModel,
                onNextClick = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.USER_INFO) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.FOGOTPASS) {
            ForgotPasswordScreen(
                onSendCodeClick = { email ->
                    navController.navigate("${Routes.VERIFICATION}/$email")
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("${Routes.VERIFICATION}/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerificationCodeScreen(
                email = email,
                onVerifyClick = { code ->
                    // Logic to verify code would go here
                    // For now, simulate success by going to Login
                     navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.MAIN) {
            NavMenu(
                viewModelFactory = viewModelFactory,
                onLogoutClick = {
                    authRepository.logout()
                    selectedRoadmapStore.clearSelection()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                }
            )
        }
    }
}
