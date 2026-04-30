package ru.vsu.roadmap.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.roadmap.R
import ru.vsu.roadmap.data.model.RoadmapDto
import ru.vsu.roadmap.ui.components.DefaultUserAvatar
import ru.vsu.roadmap.ui.components.RoadmapCircleBadge
import ru.vsu.roadmap.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onSettingsClick: () -> Unit = {},
    onRoadmapOpened: () -> Unit = {},
) {
    // Reload data every time the screen is displayed
    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    val profile = viewModel.profile

    var selectedRoadmap by remember { mutableStateOf<RoadmapDto?>(null) }
    val sheetState = rememberModalBottomSheetState()

    if (selectedRoadmap != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedRoadmap = null },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val rm = selectedRoadmap!!
                Text(
                    text = rm.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                RoadmapCircleBadge(size = 80.dp)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = rm.description ?: "",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                    textAlign = TextAlign.Center
                )

                if (rm.id == viewModel.activeRoadmap?.id && viewModel.activeProgress != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(
                            R.string.profile_roadmap_progress_hint,
                            viewModel.activeProgress!!.progressPercent,
                        ),
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val steps = viewModel.currentSteps
                if (steps.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.home_roadmap_steps_header),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.align(Alignment.Start)
                    )
                    steps.forEachIndexed { index, step ->
                        Text(
                            text = "${index + 1}. ${step.title}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.Start).padding(vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                val alreadyStarted = viewModel.startedRoadmapIds.contains(rm.id)
                Button(
                    onClick = {
                        viewModel.selectRoadmapForViewer(rm.id) {
                            selectedRoadmap = null
                            onRoadmapOpened()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = if (alreadyStarted) {
                            stringResource(R.string.catalog_continue)
                        } else {
                            stringResource(R.string.catalog_start)
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar with Settings
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.Black
                )
            }
        }

        if (viewModel.isLoading) {
             CircularProgressIndicator(color = Color.Black)
        } else if (profile != null) {
            // Avatar (фиксированный для всех пользователей)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                DefaultUserAvatar(
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = profile.name,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name and Surname
            Text(
                text = "${profile.name ?: ""} ${profile.surname ?: ""}".trim().ifEmpty { "No Name" },
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )

            // Role / About
            Text(
                text = profile.about ?: "No Info",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray
                )
            )
        } else {
            // Error or Empty state
             Text(text = "Failed to load profile", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Current Progress Section
        if (viewModel.activeRoadmap != null && viewModel.activeProgress != null) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.current_progress_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                val currentProgressTitle = viewModel.activeRoadmap!!.title
                val progress = viewModel.activeProgress!!
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable {
                            viewModel.activeRoadmap?.let { ar ->
                                selectedRoadmap = ar
                                viewModel.loadSteps(ar.id)
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, Color.Black),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = currentProgressTitle,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )

                        // Progress Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.LightGray.copy(alpha = 0.3f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress.progressPercent / 100f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.Black)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${progress.progressPercent}%",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            )
                            Text(
                                text = "Last updated: ${progress.updatedAt.substringBefore("T")}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.Gray
                                )
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Favorite Maps Section
        if (viewModel.favorites.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.favorite_maps_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Display up to 2 favorites
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    viewModel.favorites.take(2).forEach { fav ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp)
                                .clickable {
                                    selectedRoadmap = fav
                                    viewModel.loadSteps(fav.id)
                                },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(2.dp, Color.Black),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = fav.title,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    ),
                                    maxLines = 2
                                )
                            }
                        }
                    }
                    // If only 1 favorite, add a spacer to keep alignment
                    if (viewModel.favorites.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
//    ProfileScreen()
}