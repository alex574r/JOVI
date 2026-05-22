package com.example.jovi.ui.screens.search

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*

private data class SearchResult(val name: String, val subtitle: String, val initials: String, val type: String)

private val allResults = listOf(
    SearchResult("Carlos Mendoza", "Software Developer • UNAM", "CM", "person"),
    SearchResult("Ana Garcia", "UX Designer • Tec de Monterrey", "AG", "person"),
    SearchResult("Luis Torres", "Data Scientist • IPN", "LT", "person"),
    SearchResult("Innovatech Corp", "Empresa de tecnologia", "IC", "company"),
    SearchResult("Stanford University", "Institucion academica", "SU", "company"),
    SearchResult("Mobile Developer Intern", "Innovatech Corp • CDMX", "IC", "job"),
    SearchResult("Research Assistant", "Stanford Univ. • Remoto", "SU", "job"),
    SearchResult("UX Design Practitioner", "Startup MX • Hibrido", "S", "job"),
)

@Composable
fun SearchScreen(onBack: () -> Unit, onProfile: () -> Unit) {
    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val results = if (query.isBlank()) emptyList() else allResults.filter {
        it.name.contains(query, ignoreCase = true) || it.subtitle.contains(query, ignoreCase = true)
    }
    val trending = listOf("Kotlin", "UX Design", "Data Science", "Practicas 2025", "Remote Jobs")

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(BackgroundColor).padding(horizontal = 8.dp, vertical = 8.dp).statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, contentDescription = "Volver", tint = TextPrimary) }
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Buscar personas, empleos, empresas...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                    trailingIcon = {
                        if (query.isNotBlank()) IconButton(onClick = { query = "" }) {
                            Icon(Icons.Outlined.Close, contentDescription = null, tint = TextSecondary)
                        }
                    },
                    modifier = Modifier.weight(1f).focusRequester(focusRequester),
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryDark,
                        unfocusedBorderColor = TertiaryColor,
                        focusedContainerColor = SurfaceColor,
                        unfocusedContainerColor = SurfaceColor,
                    ),
                    singleLine = true,
                )
            }
        },
        containerColor = BackgroundColor,
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(bottom = 16.dp)) {
            if (query.isBlank()) {
                item {
                    Text("Tendencias", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
                }
                items(trending) { tag ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.TrendingUp, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(18.dp))
                        Text(tag, style = MaterialTheme.typography.bodyMedium)
                    }
                    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
                }
            } else {
                if (results.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(32.dp)) {
                                Icon(Icons.Outlined.SearchOff, contentDescription = null, tint = TertiaryColor, modifier = Modifier.size(48.dp))
                                Text("Sin resultados para \"$query\"", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            }
                        }
                    }
                } else {
                    items(results) { r ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProfileAvatar(r.initials, size = 44.dp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(r.name, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                                Text(r.subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                            val typeIcon = when(r.type) {
                                "job" -> Icons.Outlined.Work
                                "company" -> Icons.Outlined.Business
                                else -> Icons.Outlined.Person
                            }
                            Icon(typeIcon, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }
                        HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}
