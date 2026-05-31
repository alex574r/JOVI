package com.example.jovi.ui.screens.recruiter

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.RecruiterViewModel

@Composable
fun RecruiterPortalScreen(
    navController: NavHostController,
    viewModel: RecruiterViewModel,
    onLogout: () -> Unit,
    onSettings: () -> Unit,
    onOpenChat: (Long) -> Unit,
    onAnalytics: () -> Unit,
    onPublishVacancy: () -> Unit,
    onViewApplicant: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        Triple("Inicio", Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
        Triple("Candidatos", Icons.Filled.People, Icons.Outlined.People),
        Triple("Mensajes", Icons.Filled.MailOutline, Icons.Outlined.MailOutline),
        Triple("Perfil", Icons.Filled.Business, Icons.Outlined.BusinessCenter),
    )

    Scaffold(
        containerColor = BackgroundColor,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            NavigationBar(containerColor = BackgroundColor, tonalElevation = 4.dp) {
                tabs.forEachIndexed { index, (label, filled, outlined) ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        icon = { Icon(if (isSelected) filled else outlined, contentDescription = label) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryDark,
                            selectedTextColor = PrimaryDark,
                            indicatorColor = PrimaryLight,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { w -> w } + fadeIn(tween(200))).togetherWith(
                            slideOutHorizontally { w -> -w } + fadeOut(tween(200))
                        )
                    } else {
                        (slideInHorizontally { w -> -w } + fadeIn(tween(200))).togetherWith(
                            slideOutHorizontally { w -> w } + fadeOut(tween(200))
                        )
                    }
                },
                label = "recruiter_tab"
            ) { tab ->
                when (tab) {
                    0 -> RecruiterDashboardContent(
                        viewModel = viewModel,
                        onAnalytics = onAnalytics,
                        onPublishVacancy = onPublishVacancy,
                        onViewApplicants = { selectedTab = 1 },
                        onOpenChat = { _ -> selectedTab = 2 },
                    )
                    1 -> RecruiterCandidatesContent(
                        viewModel = viewModel,
                        onViewProfile = onViewApplicant,
                        onChat = onOpenChat,
                    )
                    2 -> RecruiterMessagesContent(onOpenChat = onOpenChat)
                    3 -> RecruiterProfileContent(
                        viewModel = viewModel,
                        onSettings = onSettings,
                        onLogout = onLogout,
                        onAnalytics = onAnalytics,
                        onPublishVacancy = onPublishVacancy,
                    )
                }
            }
        }
    }
}
