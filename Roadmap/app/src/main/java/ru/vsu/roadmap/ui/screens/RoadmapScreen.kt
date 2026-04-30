package ru.vsu.roadmap.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import ru.vsu.roadmap.R
import ru.vsu.roadmap.config.RoadmapScreenConfig
import ru.vsu.roadmap.data.model.RoadmapStepDto
import ru.vsu.roadmap.ui.viewmodel.RoadmapViewModel
import kotlin.math.cos
import kotlin.math.sin

private enum class MilestoneType { REGULAR, CURRENT }

/** Один узел на канве; [stepId] совпадает с `roadmap_steps.id` на бэкенде. */
private data class MilestoneUi(
    val stepId: Long,
    val xFrac: Float,
    val yFrac: Float,
    val type: MilestoneType,
    val title: String,
    val description: String,
)

/** Фиксированные позиции для ровно 7 шагов (порядок соответствует `order_index` на сервере). */
private val ROUTE_MILESTONE_FRACS: List<Pair<Float, Float>> = listOf(
    0.18f to 0.10f,
    0.50f to 0.10f,
    0.82f to 0.10f,
    0.88f to 0.30f,
    0.50f to 0.50f,
    0.12f to 0.67f,
    0.45f to 0.85f,
)

private const val ROAD_END_X_FRAC = 0.18f
private const val ROAD_END_Y_FRAC = 0.85f
private const val NEXT_X_FRAC = 0.45f
private const val NEXT_Y_FRAC = 0.85f

private fun buildMilestones(
    steps: List<RoadmapStepDto>,
    currentStepId: Long?,
): List<MilestoneUi>? {
    if (steps.size != RoadmapScreenConfig.EXPECTED_STEP_COUNT) return null
    val sorted = steps.sortedBy { it.orderIndex }
    return sorted.zip(ROUTE_MILESTONE_FRACS) { step, frac ->
        MilestoneUi(
            stepId = step.id,
            xFrac = frac.first,
            yFrac = frac.second,
            type = if (step.id == currentStepId) MilestoneType.CURRENT else MilestoneType.REGULAR,
            title = step.title,
            description = step.description.orEmpty(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapScreen(
    viewModel: RoadmapViewModel,
    onGoToCatalog: () -> Unit = {},
) {
    val uiState = viewModel.uiState

    val milestones = remember(uiState.steps, uiState.currentStepId) {
        buildMilestones(uiState.steps, uiState.currentStepId)
    }

    var selected by remember { mutableStateOf<MilestoneUi?>(null) }

    selected?.let { ms ->
        val sheetState = rememberModalBottomSheetState()
        val isStudied = ms.stepId in uiState.studiedStepIds
        ModalBottomSheet(
            onDismissRequest = { selected = null },
            sheetState = sheetState,
            containerColor = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = ms.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    ),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = ms.description,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                    textAlign = TextAlign.Center,
                )
                if (ms.type == MilestoneType.REGULAR || ms.type == MilestoneType.CURRENT) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            viewModel.toggleStepCompleted(ms.stepId, isStudied)
                            selected = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White,
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                    ) {
                        Text(
                            text = if (isStudied) "Отменить" else "Изучено",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }

            uiState.needsCatalogSelection -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(R.string.roadmap_empty_title),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        ),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.roadmap_empty_subtitle),
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onGoToCatalog,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White,
                        ),
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.roadmap_go_to_catalog),
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }

            uiState.error != null && milestones == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                ) {
                    ErrorBanner(message = uiState.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = { viewModel.load() }) {
                        Text(stringResource(R.string.roadmap_retry))
                    }
                }
            }

            else -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = uiState.roadmapTitle.ifBlank { stringResource(R.string.frontend_roadmap) },
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        ),
                    )
                }

                if (uiState.stepCountMismatch) {
                    Text(
                        text = stringResource(R.string.roadmap_steps_count_error),
                        color = Color(0xFFB00020),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .background(Color(0xFFFFE8E8), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { viewModel.load() }) {
                        Text(stringResource(R.string.roadmap_retry))
                    }
                }

                uiState.error?.let { err ->
                    if (milestones != null) {
                        ErrorBanner(message = err)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                if (milestones == null || uiState.stepCountMismatch) {
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    Layout(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .drawBehind {
                                drawRoad()
                                drawArrowToNext()
                            },
                        content = {
                            milestones.forEachIndexed { index, ms ->
                                MilestoneCircle(
                                    onClick = { selected = ms },
                                    withSparkles = index == milestones.lastIndex,
                                ) {
                                    when {
                                        ms.stepId in uiState.studiedStepIds -> Icon(
                                            imageVector = Icons.Default.Done,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(28.dp),
                                        )
                                        else -> Image(
                                            painter = painterResource(R.drawable.freeiconprogramming),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp),
                                            contentScale = ContentScale.Fit,
                                        )
                                    }
                                }
                            }
                        },
                    ) { measurables, constraints ->
                        val w = if (constraints.hasBoundedWidth) constraints.maxWidth else 0
                        val h = if (constraints.hasBoundedHeight) constraints.maxHeight else 0
                        val freeChildConstraints = Constraints()

                        val placeables = measurables.map { it.measure(freeChildConstraints) }

                        layout(w, h) {
                            milestones.forEachIndexed { i, ms ->
                                val p = placeables[i]
                                val halfW = p.width / 2
                                val halfH = p.height / 2
                                val cx = (w * ms.xFrac).toInt().coerceIn(halfW, w - halfW)
                                val cy = (h * ms.yFrac).toInt().coerceIn(halfH, h - halfH)
                                p.place(cx - halfW, cy - halfH)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawRoad() {
    val w = size.width
    val h = size.height

    val tY = h * 0.10f
    val cY = h * 0.50f
    val bY = h * ROAD_END_Y_FRAC
    val lX = w * ROAD_END_X_FRAC
    val mX = w * 0.50f
    val rX = w * 0.82f

    val roadStroke = 70f
    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 14f), 0f)

    val topPath = Path().apply {
        moveTo(lX, tY)
        lineTo(rX, tY)
    }
    val sPath = Path().apply {
        moveTo(rX, tY)
        cubicTo(
            rX + w * 0.13f, tY,
            rX + w * 0.13f, cY,
            mX, cY,
        )
        cubicTo(
            mX - w * 0.45f, cY,
            mX - w * 0.45f, bY,
            lX, bY,
        )
    }

    drawPath(
        path = topPath,
        color = Color.Black,
        style = Stroke(width = roadStroke, cap = StrokeCap.Round)
    )
    drawPath(
        path = sPath,
        color = Color.Black,
        style = Stroke(width = roadStroke, cap = StrokeCap.Round)
    )
    drawPath(
        path = topPath,
        color = Color.White,
        style = Stroke(width = 4f, pathEffect = dashEffect)
    )
    drawPath(
        path = sPath,
        color = Color.White,
        style = Stroke(width = 4f, pathEffect = dashEffect)
    )
}

private fun DrawScope.drawArrowToNext() {
    val w = size.width
    val h = size.height

    val arrowY = h * NEXT_Y_FRAC
    val roadEndX = w * ROAD_END_X_FRAC
    val nextX = w * NEXT_X_FRAC

    val roadStroke = 70f
    val circleRadius = 36.dp.toPx()
    val gap = 10.dp.toPx()

    val arrowStartX = roadEndX + roadStroke / 2 + gap
    val arrowEndX = nextX - circleRadius - gap

    if (arrowEndX <= arrowStartX) return

    val strokeW = 4f
    val dashLen = 10f
    val dashGap = 6f
    val chevronSize = 10f

    var x = arrowStartX
    val lastDashEnd = arrowEndX - chevronSize - 2f
    while (x + dashLen <= lastDashEnd) {
        drawLine(
            color = Color.Black,
            start = Offset(x, arrowY),
            end = Offset(x + dashLen, arrowY),
            strokeWidth = strokeW,
            cap = StrokeCap.Round,
        )
        x += dashLen + dashGap
    }

    val tipX = arrowEndX
    drawLine(
        color = Color.Black,
        start = Offset(tipX - chevronSize, arrowY - chevronSize),
        end = Offset(tipX, arrowY),
        strokeWidth = strokeW,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = Color.Black,
        start = Offset(tipX - chevronSize, arrowY + chevronSize),
        end = Offset(tipX, arrowY),
        strokeWidth = strokeW,
        cap = StrokeCap.Round,
    )
}

@Composable
private fun MilestoneCircle(
    onClick: () -> Unit,
    withSparkles: Boolean = false,
    innerContent: @Composable () -> Unit,
) {
    val visibleDiameter = 48.dp
    val sparkleSpace = 12.dp
    val totalSize = if (withSparkles) visibleDiameter + sparkleSpace * 2 else visibleDiameter

    Box(
        modifier = Modifier.size(totalSize),
        contentAlignment = Alignment.Center,
    ) {
        if (withSparkles) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val rInner = visibleDiameter.toPx() / 2f + 4f
                val rOuter = rInner + sparkleSpace.toPx() - 6f
                for (i in 0 until 8) {
                    val angle = (i * Math.PI / 4).toFloat()
                    drawLine(
                        color = Color.Black,
                        start = Offset(cx + cos(angle) * rInner, cy + sin(angle) * rInner),
                        end = Offset(cx + cos(angle) * rOuter, cy + sin(angle) * rOuter),
                        strokeWidth = 2.5f,
                        cap = StrokeCap.Round,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .size(visibleDiameter)
                .clip(CircleShape)
                .background(Color.White)
                .border(3.dp, Color.Black, CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            innerContent()
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun RoadmapScreenPreviewPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Text("Roadmap: подключите RoadmapViewModel на устройстве")
    }
}
