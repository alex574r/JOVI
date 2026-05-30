package com.example.jovi.ui.screens.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.data.db.entity.PostEntity
import com.example.jovi.ui.components.JoviTopBar
import com.example.jovi.ui.components.ProfileAvatar
import com.example.jovi.ui.theme.*

@Composable
fun PostDetailScreen(
    post: PostEntity?,
    onBack: () -> Unit,
    onLike: (PostEntity) -> Unit = {},
) {
    var isLiked by remember(post?.id) { mutableStateOf(post?.isLiked ?: false) }
    var likeCount by remember(post?.id) { mutableIntStateOf(post?.likeCount ?: 0) }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            JoviTopBar(
                title = "Publicación",
                onBack = onBack,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Share, contentDescription = "Compartir", tint = TextPrimary)
                    }
                }
            )
        }
    ) { padding ->
        if (post == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryDark)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundColor)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        ProfileAvatar(post.authorInitials, size = 48.dp)
                        Column {
                            Text(post.authorName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text("@${post.authorUsername}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }

                    Text(post.content, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, lineHeight = MaterialTheme.typography.bodyLarge.lineHeight)

                    if (post.tags.isNotEmpty()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            post.tags.split(",").take(4).forEach { tag ->
                                Surface(shape = RoundedCornerShape(50), color = PrimaryLight) {
                                    Text(
                                        "#${tag.trim()}",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = PrimaryDark,
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = DividerColor)

                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IconButton(
                                onClick = {
                                    isLiked = !isLiked
                                    likeCount += if (isLiked) 1 else -1
                                    post.let { onLike(it) }
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (isLiked) PrimaryDark else TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text("$likeCount likes", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                            Text("${post.commentCount} comentarios", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                }

                HorizontalDivider(thickness = 8.dp, color = SurfaceColor)
            }

            item {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Comentarios", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))

                    listOf(
                        Triple("CM", "Carlos Mendoza", "Excelente punto! Yo tuve la misma experiencia al inicio de mi practica."),
                        Triple("LT", "Luis Torres", "Muy buen tip, lo aplicare en mi proximo proyecto de data."),
                        Triple("IC", "Innovatech Corp", "Nos encanta este tipo de contenido. Sigan compartiendo sus experiencias!"),
                    ).forEach { (initials, name, comment) ->
                        CommentItem(initials = initials, name = name, comment = comment)
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentItem(initials: String, name: String, comment: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        ProfileAvatar(initials, size = 36.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceColor, RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(name, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
            Text(comment, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}
