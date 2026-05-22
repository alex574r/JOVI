package com.example.jovi.ui.screens.feed

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private data class FeedPost(
    val id: Int,
    val authorName: String,
    val authorInitials: String,
    val authorRole: String,
    val timeAgo: String,
    val content: String,
    val tags: List<String>,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
)

private data class ReelItem(
    val id: Int,
    val authorName: String,
    val authorInitials: String,
    val title: String,
    val views: String,
    val likeCount: Int,
    val isLiked: Boolean = false,
)

private val sampleThreads = listOf(
    FeedPost(1, "Carlos Mendoza", "CM", "Software Developer", "hace 1h", "Acabo de terminar mi primer proyecto con Kotlin Multiplatform. La curva de aprendizaje es real pero los resultados valen la pena. Comparto mis notas y errores mas comunes.", listOf("kotlin", "android", "mobile"), 47, 12),
    FeedPost(2, "Ana Garcia", "AG", "UX Designer", "hace 2h", "Tip de UX: antes de disenar cualquier flujo, mapea los estados vacios. Son los momentos mas criticos de una app y los mas olvidados en el diseno inicial.", listOf("ux", "design", "tips"), 93, 28),
    FeedPost(3, "Stanford University", "SU", "Reclutador Verificado", "hace 6h", "Abrimos convocatoria para el programa de investigacion de verano 2025. Plazas limitadas para estudiantes de ingenieria y ciencias computacionales.", listOf("oportunidad", "becas", "stanford"), 312, 87, isLiked = true),
    FeedPost(4, "Luis Torres", "LT", "Data Scientist", "hace 1d", "Resumen de tendencias en Data Science para 2025: LLMs especializados, edge AI, y la vuelta a modelos interpretables.", listOf("datascience", "ml", "ai"), 71, 19),
    FeedPost(5, "Innovatech Corp", "IC", "Reclutador", "hace 3h", "Estamos buscando 3 practicantes de desarrollo mobile. Stack: Kotlin + Compose. Modalidad hibrida.", listOf("empleo", "practicas", "mobile"), 128, 53),
)

private val sampleReels = listOf(
    ReelItem(1, "Carlos Mendoza", "CM", "Tutorial: SwipeCard en Compose en 50 lineas", "1.2K", 156),
    ReelItem(2, "Ana Garcia", "AG", "Redisene un landing page en 2 horas (antes/despues)", "3.4K", 204, isLiked = true),
    ReelItem(3, "Luis Torres", "LT", "Explico: como funciona un modelo de regresion en 60 segundos", "890", 67),
    ReelItem(4, "Innovatech Corp", "IC", "Un dia en la vida de un desarrollador mobile en CDMX", "5.1K", 341),
    ReelItem(5, "Stanford University", "SU", "Como preparar tu CV para universidades de primer nivel", "12K", 892, isLiked = true),
)

@Composable
fun ProfessionalFeedScreen(
    onCreateContent: () -> Unit,
    onApply: () -> Unit,
    onNotifications: () -> Unit = {},
    onSearch: () -> Unit = {},
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Threads", "Reels")

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundColor)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "jovi",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = SecondaryColor,
                    letterSpacing = androidx.compose.ui.unit.TextUnit(1f, androidx.compose.ui.unit.TextUnitType.Sp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onSearch) {
                        Icon(Icons.Outlined.Search, contentDescription = "Buscar", tint = TextPrimary)
                    }
                    IconButton(onClick = onNotifications) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones", tint = TextPrimary)
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateContent,
                containerColor = SecondaryColor,
                contentColor = PrimaryColor,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear contenido")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = selectedTab == index
                    val bgColor by animateColorAsState(
                        targetValue = if (isSelected) SecondaryColor else SurfaceColor,
                        animationSpec = tween(200),
                        label = "tab_bg"
                    )
                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) PrimaryColor else TextSecondary,
                        animationSpec = tween(200),
                        label = "tab_text"
                    )
                    Surface(
                        onClick = { selectedTab = index },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50),
                        color = bgColor,
                    ) {
                        Box(modifier = Modifier.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                            Text(tab, color = textColor, style = MaterialTheme.typography.labelLarge, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }

            HorizontalDivider(color = DividerColor, modifier = Modifier.padding(top = 4.dp))

            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { w -> w } + fadeIn(tween(200))).togetherWith(slideOutHorizontally { w -> -w } + fadeOut(tween(200)))
                    } else {
                        (slideInHorizontally { w -> -w } + fadeIn(tween(200))).togetherWith(slideOutHorizontally { w -> w } + fadeOut(tween(200)))
                    }
                },
                modifier = Modifier.fillMaxSize(),
                label = "feed_content"
            ) { tab ->
                when (tab) {
                    0 -> ThreadsFeed(threads = sampleThreads, onApply = onApply)
                    else -> ReelsFeed(reels = sampleReels)
                }
            }
        }
    }
}

@Composable
private fun ThreadsFeed(threads: List<FeedPost>, onApply: () -> Unit) {
    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 88.dp),
    ) {
        items(threads, key = { it.id }) { post ->
            ThreadCard(post = post, onApply = onApply)
            HorizontalDivider(color = DividerColor)
        }
    }
}

@Composable
private fun ThreadCard(post: FeedPost, onApply: () -> Unit) {
    var isLiked by remember { mutableStateOf(post.isLiked) }
    var isSaved by remember { mutableStateOf(post.isSaved) }
    var likeCount by remember { mutableIntStateOf(post.likeCount) }

    Column(
        modifier = Modifier.fillMaxWidth().background(BackgroundColor).padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            ProfileAvatar(post.authorInitials, size = 42.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(post.authorName, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Text(post.authorRole, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            Text(post.timeAgo, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }

        Text(post.content, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)

        if (post.tags.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                post.tags.take(3).forEach { tag ->
                    Surface(shape = RoundedCornerShape(50), color = PrimaryLight) {
                        Text("#$tag", modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = { isLiked = !isLiked; likeCount += if (isLiked) 1 else -1 }, modifier = Modifier.size(32.dp)) {
                        Icon(if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, contentDescription = null, tint = if (isLiked) PrimaryDark else TextSecondary, modifier = Modifier.size(18.dp))
                    }
                    Text("$likeCount", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }
                    Text("${post.commentCount}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = { isSaved = !isSaved }, modifier = Modifier.size(32.dp)) {
                    Icon(if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder, contentDescription = null, tint = if (isSaved) SecondaryColor else TextSecondary, modifier = Modifier.size(18.dp))
                }
                Surface(onClick = onApply, shape = RoundedCornerShape(50), color = PrimaryColor) {
                    Text("Aplicar", modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp), style = MaterialTheme.typography.labelSmall, color = SecondaryColor, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ReelsFeed(reels: List<ReelItem>) {
    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier.fillMaxSize().background(BackgroundColor),
        contentPadding = PaddingValues(bottom = 88.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(reels, key = { it.id }) { reel -> ReelCard(reel) }
    }
}

@Composable
private fun ReelCard(reel: ReelItem) {
    var isLiked by remember { mutableStateOf(reel.isLiked) }
    var likeCount by remember { mutableIntStateOf(reel.likeCount) }

    Box(
        modifier = Modifier.fillMaxWidth().height(220.dp).padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(colors = listOf(SecondaryColor, SecondaryColor.copy(0.85f))))
    ) {
        Column(
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                ProfileAvatar(reel.authorInitials, size = 32.dp)
                Column {
                    Text(reel.authorName, style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("${reel.views} vistas", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.6f))
                }
            }
            Text(reel.title, style = MaterialTheme.typography.titleSmall, color = Color.White, fontWeight = FontWeight.SemiBold)
        }
        Column(
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { isLiked = !isLiked; likeCount += if (isLiked) 1 else -1 }, modifier = Modifier.size(40.dp)) {
                    Icon(if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, contentDescription = null, tint = if (isLiked) PrimaryColor else Color.White, modifier = Modifier.size(26.dp))
                }
                Text("$likeCount", style = MaterialTheme.typography.labelSmall, color = Color.White)
            }
            IconButton(onClick = {}, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Outlined.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
            }
        }
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(12.dp).clip(RoundedCornerShape(50)).background(PrimaryColor.copy(0.9f)).padding(horizontal = 10.dp, vertical = 4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Outlined.PlayCircleOutline, contentDescription = null, tint = SecondaryColor, modifier = Modifier.size(12.dp))
                Text("Reel", style = MaterialTheme.typography.labelSmall, color = SecondaryColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}
