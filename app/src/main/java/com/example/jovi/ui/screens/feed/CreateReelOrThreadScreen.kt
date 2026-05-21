package com.example.jovi.ui.screens.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

@Composable
fun CreateReelOrThreadScreen(
    onDismiss: () -> Unit,
    onPost: () -> Unit,
) {
    var selectedTab by remember { mutableStateOf("Reel") }
    var selectedFilter by remember { mutableStateOf("OFFICE") }
    val tabs = listOf("Reel", "Thread")
    val filters = listOf("NORMAL", "OFFICE", "WARM")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // Camera viewfinder placeholder
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ProfileAvatar("ME", size = 120.dp, bgColor = BackgroundColor.copy(0.1f), textColor = BackgroundColor)
        }

        // Top controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = BackgroundColor)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                tabs.forEach { tab ->
                    Surface(
                        onClick = { selectedTab = tab },
                        shape = RoundedCornerShape(50),
                        color = if (tab == selectedTab) PrimaryColor else BackgroundColor.copy(0.3f)
                    ) {
                        Text(
                            tab,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (tab == selectedTab) SecondaryColor else BackgroundColor,
                            fontWeight = if (tab == selectedTab) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
            Surface(onClick = onPost, shape = RoundedCornerShape(8.dp), color = PrimaryColor) {
                Text("Publicar", modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp), style = MaterialTheme.typography.labelLarge, color = SecondaryColor)
            }
        }

        // Right controls
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CameraControl(icon = Icons.Outlined.FlashOn)
            CameraControl(icon = Icons.Outlined.PhotoCamera)
            CameraControl(icon = Icons.Outlined.Timer)
            CameraControl(icon = Icons.Outlined.Tune)
        }

        // Bottom controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(BackgroundColor.copy(alpha = 0.15f))
                .padding(bottom = 32.dp, top = 16.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                filters.forEach { filter ->
                    val isSelected = filter == selectedFilter
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable { selectedFilter = filter }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 44.dp else 36.dp)
                                .clip(CircleShape)
                                .background(
                                    when (filter) {
                                        "NORMAL" -> Color(0xFFB0BEC5)
                                        "OFFICE" -> PrimaryColor
                                        else -> Color(0xFFF4A460)
                                    }
                                )
                        )
                        Text(
                            filter,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) PrimaryColor else BackgroundColor.copy(0.7f)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(onClick = {}, shape = CircleShape, color = PrimaryLight) {
                    Icon(
                        Icons.Outlined.UploadFile,
                        contentDescription = "Subir",
                        modifier = Modifier.padding(12.dp),
                        tint = SecondaryColor
                    )
                }
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(BackgroundColor.copy(0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(PrimaryColor)
                    )
                }
                Surface(onClick = {}, shape = CircleShape, color = BackgroundColor.copy(0.2f)) {
                    Icon(
                        Icons.Outlined.FlipCameraAndroid,
                        contentDescription = "Voltear cámara",
                        modifier = Modifier.padding(12.dp),
                        tint = BackgroundColor
                    )
                }
            }
        }
    }
}

@Composable
private fun CameraControl(icon: ImageVector) {
    Surface(onClick = {}, shape = CircleShape, color = BackgroundColor.copy(0.2f)) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.padding(10.dp).size(20.dp),
            tint = BackgroundColor
        )
    }
}

