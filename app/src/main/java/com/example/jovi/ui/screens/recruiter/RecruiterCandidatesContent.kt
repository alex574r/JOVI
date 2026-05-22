package com.example.jovi.ui.screens.recruiter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.data.db.entity.UserEntity
import com.example.jovi.ui.components.ProfileAvatar
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.RecruiterViewModel

@Composable
fun RecruiterCandidatesContent(
    viewModel: RecruiterViewModel,
    onViewProfile: () -> Unit,
    onChat: (String) -> Unit,
) {
    val candidates by viewModel.candidates.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val displayed = if (searchQuery.isEmpty()) candidates
    else candidates.filter {
        it.displayName.contains(searchQuery, ignoreCase = true) ||
                it.university.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundColor)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundColor)
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Candidatos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = PrimaryDark)
            IconButton(onClick = {}) {
                Icon(Icons.Outlined.FilterList, contentDescription = "Filtrar", tint = TextPrimary)
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar candidatos...", color = TextSecondary) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(14.dp),
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = TextSecondary) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = TertiaryColor,
                focusedBorderColor = PrimaryDark,
            ),
            singleLine = true,
        )

        Spacer(Modifier.height(8.dp))

        if (displayed.isEmpty() && candidates.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.SearchOff, contentDescription = null, tint = TertiaryColor, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("Sin resultados para \"$searchQuery\"", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }
        } else {
            val listToShow = if (displayed.isEmpty()) listOf(
                // demo candidates when DB not seeded
                UserEntity(id = 1, username = "carlos_dev", displayName = "Carlos Mendoza", email = "", avatarInitials = "CM", university = "UNAM", bio = "Software Developer | Kotlin, Compose"),
                UserEntity(id = 2, username = "ana_design", displayName = "Ana Garcia", email = "", avatarInitials = "AG", university = "Tec de Monterrey", bio = "UX/UI Designer | Figma, Prototyping"),
                UserEntity(id = 3, username = "luis_data", displayName = "Luis Torres", email = "", avatarInitials = "LT", university = "IPN", bio = "Data Scientist | ML, Python"),
            ) else displayed

            LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                items(listToShow, key = { it.id }) { candidate ->
                    CandidateListRow(candidate = candidate, onViewProfile = onViewProfile, onChat = { onChat(candidate.displayName) })
                    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(start = 76.dp))
                }
            }
        }
    }
}

@Composable
private fun CandidateListRow(
    candidate: UserEntity,
    onViewProfile: () -> Unit,
    onChat: () -> Unit,
) {
    Surface(onClick = onViewProfile, color = BackgroundColor) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileAvatar(candidate.avatarInitials, size = 48.dp)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(candidate.displayName, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                if (candidate.university.isNotEmpty()) {
                    Text(candidate.university, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                if (candidate.bio.isNotEmpty()) {
                    Text(candidate.bio, style = MaterialTheme.typography.bodySmall, color = TextSecondary, maxLines = 1)
                }
            }
            IconButton(onClick = onChat, modifier = Modifier.size(38.dp)) {
                Icon(Icons.Outlined.MailOutline, contentDescription = "Chat", tint = PrimaryDark, modifier = Modifier.size(18.dp))
            }
        }
    }
}
