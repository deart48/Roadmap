package ru.vsu.roadmap.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.vsu.roadmap.R
import ru.vsu.roadmap.ui.ViewModelFactory
import ru.vsu.roadmap.ui.navigation.BottomRoutes
import ru.vsu.roadmap.ui.screens.CatalogScreen
import ru.vsu.roadmap.ui.screens.EditProfileScreen
import ru.vsu.roadmap.ui.screens.HomeScreen
import ru.vsu.roadmap.ui.screens.ProfileScreen
import ru.vsu.roadmap.ui.screens.RoadmapScreen
import ru.vsu.roadmap.ui.viewmodel.CatalogViewModel
import ru.vsu.roadmap.ui.viewmodel.EditProfileViewModel
import ru.vsu.roadmap.ui.viewmodel.HomeViewModel
import ru.vsu.roadmap.ui.viewmodel.ProfileViewModel
import ru.vsu.roadmap.ui.viewmodel.RoadmapViewModel

sealed class NavigationIcon {
    data class Vector(val imageVector: ImageVector) : NavigationIcon()
    data class Drawable(val resId: Int) : NavigationIcon()
}

@Composable
fun NavMenu(
    viewModelFactory: ViewModelFactory,
    onLogoutClick: () -> Unit = {},
) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = BottomRoutes.HOME
            ) {
                composable(BottomRoutes.HOME) {
                    val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
                    HomeScreen(
                        viewModel = homeViewModel,
                        onRoadmapOpened = {
                            navController.navigate(BottomRoutes.ROADMAP) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
                composable(BottomRoutes.CATALOG) {
                    val catalogViewModel: CatalogViewModel = viewModel(factory = viewModelFactory)
                    CatalogScreen(
                        viewModel = catalogViewModel,
                        onRoadmapOpened = {
                            navController.navigate(BottomRoutes.ROADMAP) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
                composable(BottomRoutes.ROADMAP) {
                    val roadmapViewModel: RoadmapViewModel = viewModel(factory = viewModelFactory)
                    val navEntry by navController.currentBackStackEntryAsState()
                    val route = navEntry?.destination?.route
                    LaunchedEffect(route) {
                        if (route == BottomRoutes.ROADMAP) {
                            roadmapViewModel.load()
                        }
                    }
                    RoadmapScreen(
                        viewModel = roadmapViewModel,
                        onGoToCatalog = {
                            navController.navigate(BottomRoutes.CATALOG) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
                composable(BottomRoutes.PROFILE) {
                    val profileViewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
                    ProfileScreen(
                        viewModel = profileViewModel,
                        onSettingsClick = { navController.navigate(BottomRoutes.EDIT_PROFILE) },
                        onRoadmapOpened = {
                            navController.navigate(BottomRoutes.ROADMAP) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
                composable(BottomRoutes.EDIT_PROFILE) {
                    val editProfileViewModel: EditProfileViewModel = viewModel(factory = viewModelFactory)
                    EditProfileScreen(
                        viewModel = editProfileViewModel,
                        onSaveClick = { navController.popBackStack() },
                        onLogoutClick = onLogoutClick
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val items = listOf(
            Triple(stringResource(R.string.nav_home), NavigationIcon.Vector(Icons.Default.Home), BottomRoutes.HOME),
            Triple(stringResource(R.string.nav_catalog), NavigationIcon.Drawable(R.drawable.catalog), BottomRoutes.CATALOG),
            Triple(stringResource(R.string.nav_roadmap), NavigationIcon.Drawable(R.drawable.roadmap), BottomRoutes.ROADMAP),
            Triple(stringResource(R.string.nav_profile), NavigationIcon.Vector(Icons.Default.AccountCircle), BottomRoutes.PROFILE)
        )

        items.forEach { (label, icon, route) ->
            NavigationBarItem(
                icon = {
                    when (icon) {
                        is NavigationIcon.Vector -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = label
                        )
                        is NavigationIcon.Drawable -> Icon(
                            painter = painterResource(id = icon.resId),
                            contentDescription = label
                        )
                    }
                },
                label = { Text(label) },
                selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                onClick = {
                    navController.navigate(route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    selectedTextColor = Color.Black,
                    unselectedIconColor = Color.Black.copy(alpha = 0.6f),
                    unselectedTextColor = Color.Black.copy(alpha = 0.6f),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
//    NavMenu()
}