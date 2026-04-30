package ru.vsu.roadmap.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.roadmap.R
import ru.vsu.roadmap.data.model.RoadmapDto
import ru.vsu.roadmap.ui.components.RoadmapCircleBadge
import ru.vsu.roadmap.ui.viewmodel.CatalogViewModel

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel? = null,
    onRoadmapOpened: () -> Unit = {},
) {
    var isListView by remember { mutableStateOf(true) }

    // If VM is null (e.g. preview), we can't do much or use mock.
    // In production, NavMenu provides the VM.
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
//        OutlinedTextField(
//            value = "",
//            onValueChange = {},
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = { Text(text = stringResource(R.string.search_catalog_hint)) },
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = null,
//                    tint = Color.Black
//                )
//            },
//            shape = RoundedCornerShape(24.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedContainerColor = Color.White,
//                unfocusedContainerColor = Color.White,
//                disabledContainerColor = Color.White,
//                focusedBorderColor = Color.Black,
//                unfocusedBorderColor = Color.Black,
//                focusedTextColor = Color.Black,
//                unfocusedTextColor = Color.Black
//            ),
//            singleLine = true
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))

        // Toggle
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(48.dp)
//                .background(Color.White, RoundedCornerShape(24.dp))
//                .BoxBorder(2.dp, Color.Black, RoundedCornerShape(24.dp)),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // List Toggle
//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxHeight()
//                    .background(
//                        if (isListView) Color.Black else Color.Transparent,
//                        RoundedCornerShape(24.dp)
//                    )
//                    .clickable { isListView = true },
//                contentAlignment = Alignment.Center
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(
//                        text = stringResource(R.string.tab_list),
//                        color = if (isListView) Color.White else Color.Black,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.List,
//                        contentDescription = null,
//                        tint = if (isListView) Color.White else Color.Black
//                    )
//                }
//            }
//
//            // Group Toggle
//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxHeight()
//                    .background(
//                        if (!isListView) Color.Black else Color.Transparent,
//                        RoundedCornerShape(24.dp)
//                    )
//                    .clickable { isListView = false },
//                contentAlignment = Alignment.Center
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        imageVector = Icons.Default.Edit,
//                        contentDescription = null,
//                        tint = if (!isListView) Color.White else Color.Black
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = stringResource(R.string.tab_group),
//                        color = if (!isListView) Color.White else Color.Black,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel?.isLoading == true) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Black)
            }
        } else {
            // Always show List View
            CatalogListView(
                roadmaps = viewModel?.roadmaps ?: emptyList(),
                favoriteIds = viewModel?.favoriteIds ?: emptySet(),
                startedRoadmapIds = viewModel?.startedRoadmapIds ?: emptySet(),
                steps = viewModel?.currentSteps ?: emptyList(),
                onToggleFavorite = { viewModel?.toggleFavorite(it) },
                onItemClick = { viewModel?.loadSteps(it.id) },
                onPrimaryRoadmapAction = { roadmap, _, onDone ->
                    viewModel?.selectRoadmapForViewer(roadmap.id, onDone)
                },
                onRoadmapChosen = onRoadmapOpened,
            )
        }
    }
}

@Composable
fun Modifier.BoxBorder(width: androidx.compose.ui.unit.Dp, color: Color, shape: androidx.compose.ui.graphics.Shape): Modifier {
    return this.then(Modifier.border(width, color, shape))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogListView(
    roadmaps: List<RoadmapDto>,
    favoriteIds: Set<Long> = emptySet(),
    startedRoadmapIds: Set<Long> = emptySet(),
    steps: List<ru.vsu.roadmap.data.model.RoadmapStepDto> = emptyList(),
    onToggleFavorite: (RoadmapDto) -> Unit = {},
    onItemClick: (RoadmapDto) -> Unit = {},
    onPrimaryRoadmapAction: (RoadmapDto, Boolean, () -> Unit) -> Unit =
        { _, _, onDone -> onDone() },
    onRoadmapChosen: () -> Unit = {},
) {
    var selectedItem by remember { mutableStateOf<RoadmapDto?>(null) }
    val sheetState = rememberModalBottomSheetState()

    if (selectedItem != null) {
        // Trigger fetch steps when item selected (handled by callback in parent)
        // Wait, Compose recomposition might delay steps update. 
        // Ideally pass steps here.
        
        ModalBottomSheet(
            onDismissRequest = { selectedItem = null },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = selectedItem!!.title,
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
                    text = selectedItem!!.description ?: "",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Steps List
                if (steps.isNotEmpty()) {
                    Text(
                        text = "Steps:",
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

                val item = selectedItem!!
                val alreadyStarted = startedRoadmapIds.contains(item.id)
                Button(
                    onClick = {
                        onPrimaryRoadmapAction(item, alreadyStarted) {
                            selectedItem = null
                            onRoadmapChosen()
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

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(roadmaps, key = { it.id }) { item ->
            val isFavorite = favoriteIds.contains(item.id)
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clickable { 
                        selectedItem = item 
                        onItemClick(item)
                    },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(2.dp, Color.Black)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoadmapCircleBadge(size = 48.dp)

                    Spacer(modifier = Modifier.width(16.dp))

                    // Text
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Text(
                            text = item.description ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Gray
                            ),
                            maxLines = 1
                        )
                    }

                    // Favorite Button
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.Add,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Black,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                onToggleFavorite(item)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun CatalogGroupView() {
    val items = listOf(
        CatalogGroupItemData(stringResource(R.string.group_it), Icons.Default.Edit, true),
        CatalogGroupItemData(stringResource(R.string.group_medicine), Icons.Default.Edit, false),
        CatalogGroupItemData(stringResource(R.string.group_design), Icons.Default.Edit, false),
        CatalogGroupItemData(stringResource(R.string.group_business), Icons.Default.Edit, true)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(items, span = { item -> 
            GridItemSpan(if (item.isWide) 2 else 1) 
        }) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (item.isWide) 80.dp else 160.dp)
                    .clickable { /* TODO */ },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(2.dp, Color.Black)
            ) {
                if (item.isWide) {
                    // Wide Layout
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        if (item.title.contains("IT")) { // Specific handling for IT icon
                             Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = Color.Black
                            )
                        } else {
                            // Business Chart
                             Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = Color.Black
                            )
                        }
                       
                        if (item.title.contains("IT") || item.title.contains("Бизнес")) {
                             Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    }
                } else {
                    // Square Layout
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

data class CatalogGroupItemData(val title: String, val icon: ImageVector, val isWide: Boolean)

@Preview(showBackground = true)
@Composable
fun CatalogScreenPreview() {
    CatalogScreen()
}