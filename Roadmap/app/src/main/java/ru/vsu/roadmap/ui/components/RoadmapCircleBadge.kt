package ru.vsu.roadmap.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vsu.roadmap.R

/**
 * Круг как на дорожной карте (белый фон, чёрная обводка), внутри — [R.drawable.freeiconprogramming].
 */
@Composable
fun RoadmapCircleBadge(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    innerPadding: Dp = 6.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White)
            .border(3.dp, Color.Black, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.freeiconprogramming),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .clip(CircleShape),
            contentScale = ContentScale.Fit,
        )
    }
}
