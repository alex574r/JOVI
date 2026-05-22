package com.example.jovi.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jovi.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun JoviPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryColor,
            contentColor = SecondaryColor,
            disabledContainerColor = TertiaryColor,
            disabledContentColor = TextSecondary,
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            leadingIcon?.let {
                Icon(it, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(text, style = MaterialTheme.typography.labelLarge)
            trailingIcon?.let {
                Spacer(Modifier.width(8.dp))
                Icon(it, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun JoviSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = SecondaryColor,
        ),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, TertiaryColor),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.let {
                Icon(it, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun JoviTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    minLines: Int = 1,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = TextSecondary) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryColor,
            unfocusedBorderColor = TertiaryColor,
            focusedContainerColor = BackgroundColor,
            unfocusedContainerColor = BackgroundColor,
            cursorColor = SecondaryColor,
        ),
        leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null, tint = TextSecondary) } },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                }
            }
        } else trailingIcon?.let {
            {
                if (onTrailingIconClick != null) {
                    IconButton(onClick = onTrailingIconClick) {
                        Icon(it, contentDescription = null, tint = TextSecondary)
                    }
                } else {
                    Icon(it, contentDescription = null, tint = TextSecondary)
                }
            }
        },
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        minLines = minLines,
    )
}

@Composable
fun SkillChip(
    label: String,
    onRemove: (() -> Unit)? = null,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val bg = if (selected) PrimaryColor else PrimaryLight
    val textColor = if (selected) SecondaryColor else SecondaryColor
    Surface(
        onClick = onClick ?: {},
        shape = RoundedCornerShape(50),
        color = bg,
        modifier = Modifier.padding(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = textColor)
            onRemove?.let {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Eliminar",
                    modifier = Modifier
                        .size(14.dp),
                    tint = textColor
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bg, textColor) = when (status.lowercase()) {
        "pending", "pendiente" -> StatusPending to Color(0xFF5D4037)
        "interviewed", "entrevistado" -> StatusInterviewed to BackgroundColor
        "accepted", "aceptado" -> StatusAccepted to BackgroundColor
        "rejected", "rechazado" -> StatusRejected to BackgroundColor
        else -> TertiaryColor to TextPrimary
    }
    Surface(shape = RoundedCornerShape(50), color = bg) {
        Text(
            status,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SwipeActionButtons(
    onDislike: () -> Unit,
    onUndo: () -> Unit,
    onSave: () -> Unit,
    onLike: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SwipeCircleButton(icon = Icons.Default.Close, tint = TextSecondary, onClick = onDislike, size = 52.dp)
        SwipeCircleButton(icon = Icons.Default.Replay, tint = StatusPending, onClick = onUndo, size = 44.dp)
        SwipeCircleButton(icon = Icons.Default.Bookmark, tint = StatusInterviewed, onClick = onSave, size = 44.dp)
        SwipeCircleButton(icon = Icons.Default.Favorite, tint = BackgroundColor, bg = PrimaryColor, onClick = onLike, size = 60.dp)
    }
}

@Composable
private fun SwipeCircleButton(
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit,
    bg: Color = BackgroundColor,
    size: androidx.compose.ui.unit.Dp = 52.dp,
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = bg,
        shadowElevation = 4.dp,
        modifier = Modifier.size(size)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(size * 0.45f))
        }
    }
}

@Composable
fun SwipeCard(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    var offsetX by remember { mutableStateOf(0f) }
    val rotation by animateFloatAsState(
        targetValue = offsetX / 25f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "card_rotation"
    )

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .graphicsLayer { rotationZ = rotation }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        when {
                            offsetX > 250f -> {
                                onSwipeRight()
                                offsetX = 0f
                            }
                            offsetX < -250f -> {
                                onSwipeLeft()
                                offsetX = 0f
                            }
                            else -> offsetX = 0f
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        offsetX = (offsetX + dragAmount).coerceIn(-600f, 600f)
                    }
                )
            },
        content = content
    )
}

@Composable
fun ProfileAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 40.dp,
    bgColor: Color = PrimaryLight,
    textColor: Color = SecondaryColor,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            initials.take(2).uppercase(),
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.35f).sp
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    action: String? = null,
    onActionClick: (() -> Unit)? = null,
    icon: ImageVector? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            icon?.let { Icon(it, contentDescription = null, tint = SecondaryColor, modifier = Modifier.size(18.dp)) }
            Text(title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        }
        action?.let {
            TextButton(onClick = onActionClick ?: {}) {
                Text(it, color = PrimaryDark, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoviTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(title, style = MaterialTheme.typography.titleMedium) },
        navigationIcon = {
            onBack?.let {
                IconButton(onClick = it) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Atrás", tint = TextPrimary)
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackgroundColor,
            titleContentColor = TextPrimary,
        )
    )
}

@Composable
fun VerifiedBadge(modifier: Modifier = Modifier) {
    Icon(
        Icons.Default.Verified,
        contentDescription = "Verificado",
        tint = PrimaryDark,
        modifier = modifier.size(18.dp)
    )
}

@Composable
fun ProgressStepBar(current: Int, total: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(total) { i ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (i < current) PrimaryColor else TertiaryColor)
            )
        }
    }
}

@Composable
fun InfoChip(text: String, icon: ImageVector? = null) {
    Surface(
        shape = RoundedCornerShape(50),
        color = SurfaceColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, TertiaryColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon?.let { Icon(it, contentDescription = null, modifier = Modifier.size(12.dp), tint = TextSecondary) }
            Text(text, style = MaterialTheme.typography.labelSmall, color = TextPrimary)
        }
    }
}
