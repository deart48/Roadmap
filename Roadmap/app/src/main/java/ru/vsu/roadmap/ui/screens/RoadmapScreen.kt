package ru.vsu.roadmap.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.roadmap.R

@Composable
fun RoadmapScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Back Action */ }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }
            Text(
                text = stringResource(R.string.frontend_roadmap),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Drawing the winding road
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    moveTo(size.width * 0.2f, size.height * 0.1f)
                    lineTo(size.width * 0.8f, size.height * 0.1f)
                    
                    cubicTo(
                        size.width * 1.1f, size.height * 0.1f,
                        size.width * 1.1f, size.height * 0.4f,
                        size.width * 0.8f, size.height * 0.4f
                    )
                    
                    lineTo(size.width * 0.2f, size.height * 0.4f)
                    
                    cubicTo(
                        size.width * -0.1f, size.height * 0.4f,
                        size.width * -0.1f, size.height * 0.7f,
                        size.width * 0.2f, size.height * 0.7f
                    )
                    
                    lineTo(size.width * 0.5f, size.height * 0.7f)
                }

                // Draw the main road
                drawPath(
                    path = path,
                    color = Color.Black,
                    style = Stroke(width = 60f, pathEffect = null)
                )
                
                // Draw the dashed line in the middle of the road
                drawPath(
                    path = path,
                    color = Color.White,
                    style = Stroke(
                        width = 4f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
                    )
                )
            }

            // Milestone 1 (Done)
            Milestone(
                modifier = Modifier.align(Alignment.TopStart).padding(start = 60.dp, top = 50.dp),
                status = MilestoneStatus.DONE
            )
            // Milestone 2 (Done)
            Milestone(
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 50.dp),
                status = MilestoneStatus.DONE
            )
            // Milestone 3 (Done)
            Milestone(
                modifier = Modifier.align(Alignment.TopEnd).padding(end = 60.dp, top = 50.dp),
                status = MilestoneStatus.DONE
            )

            // Milestone 4 (Current)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 80.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Milestone(status = MilestoneStatus.CURRENT)
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.html_css_basics),
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Milestone 5 (Next)
            Milestone(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 180.dp),
                status = MilestoneStatus.NEXT
            )

            // Milestone 6 (Future)
            Milestone(
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 60.dp, bottom = 180.dp),
                status = MilestoneStatus.FUTURE
            )
        }

        // Bottom Button
        Button(
            onClick = { /* Continue Action */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = BorderStroke(2.dp, Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(R.string.continue_button),
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

enum class MilestoneStatus { DONE, CURRENT, NEXT, FUTURE }

@Composable
fun Milestone(modifier: Modifier = Modifier, status: MilestoneStatus) {
    Box(
        modifier = modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(2.dp, Color.Black, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when (status) {
            MilestoneStatus.DONE -> Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
            MilestoneStatus.CURRENT -> Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Black)
            MilestoneStatus.NEXT -> {} // Empty circle
            MilestoneStatus.FUTURE -> Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoadmapScreenPreview() {
    RoadmapScreen()
}
