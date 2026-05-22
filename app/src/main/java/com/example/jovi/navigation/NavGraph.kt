package com.example.jovi.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.jovi.ui.screens.academic.*
import com.example.jovi.ui.screens.auth.*
import com.example.jovi.ui.screens.calendar.MyAppointmentsScreen
import com.example.jovi.ui.screens.chat.ChatScreen
import com.example.jovi.ui.screens.contract.DigitalContractScreen
import com.example.jovi.ui.screens.discovery.*
import com.example.jovi.ui.screens.feed.*
import com.example.jovi.ui.screens.feedback.*
import com.example.jovi.ui.screens.gamification.*
import com.example.jovi.ui.screens.match.MatchCelebrationScreen
import com.example.jovi.ui.screens.notifications.NotificationsScreen
import com.example.jovi.ui.screens.onboarding.OnboardingScreen
import com.example.jovi.ui.screens.profile.*
import com.example.jovi.ui.screens.search.SearchScreen
import com.example.jovi.ui.screens.settings.SettingsScreen
import com.example.jovi.ui.screens.splash.SplashScreen
import com.example.jovi.ui.screens.tracking.ApplicationTrackerScreen
import com.example.jovi.ui.screens.verification.*
import com.example.jovi.ui.screens.video.VideoInterviewScreen
import com.example.jovi.ui.theme.*

private val studentBottomNavItems = listOf(
    Triple(Screen.ProfessionalFeed.route, "Inicio", Pair(Icons.Filled.Home, Icons.Outlined.Home)),
    Triple(Screen.JobDiscovery.route, "Descubrir", Pair(Icons.Filled.Search, Icons.Outlined.Search)),
    Triple(Screen.Chat.route.split("/")[0], "Mensajes", Pair(Icons.Filled.Chat, Icons.Outlined.ChatBubble)),
    Triple(Screen.PublicProfile.route, "Perfil", Pair(Icons.Filled.Person, Icons.Outlined.Person)),
)

private val mainScreenRoutes = setOf(
    Screen.ProfessionalFeed.route,
    Screen.JobDiscovery.route,
    Screen.PublicProfile.route,
)


@Composable
fun JoviNavGraph(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in mainScreenRoutes ||
            currentRoute?.startsWith("chat") == true

    Scaffold(
        containerColor = BackgroundColor,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = BackgroundColor, tonalElevation = 4.dp) {
                    studentBottomNavItems.forEachIndexed { index, (route, label, icons) ->
                        val (filled, outlined) = icons
                        val isSelected = when (index) {
                            0 -> currentRoute == Screen.ProfessionalFeed.route
                            1 -> currentRoute in listOf(Screen.JobDiscovery.route, Screen.InternshipDiscovery.route, Screen.CandidateDiscovery.route)
                            2 -> currentRoute?.startsWith("chat") == true
                            3 -> currentRoute == Screen.PublicProfile.route
                            else -> false
                        }
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                val targetRoute = when (index) {
                                    0 -> Screen.ProfessionalFeed.route
                                    1 -> Screen.JobDiscovery.route
                                    2 -> Screen.Chat.createRoute("Empresa Demo")
                                    3 -> Screen.PublicProfile.route
                                    else -> route
                                }
                                navController.navigate(targetRoute) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(280)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(280)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(280)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(280)) },
        ) {
            // --- SPLASH ---
            composable(
                Screen.Splash.route,
                enterTransition = { fadeIn(tween(300)) },
                exitTransition = { fadeOut(tween(300)) },
            ) {
                SplashScreen(
                    onFinished = {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            // --- ONBOARDING ---
            composable(
                Screen.Onboarding.route,
                enterTransition = { fadeIn(tween(300)) },
                exitTransition = { fadeOut(tween(300)) },
            ) {
                OnboardingScreen(
                    onFinish = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            // --- AUTH ---
            composable(Screen.Login.route) {
                LoginScreen(
                    onLogin = { navController.navigate(Screen.JobDiscovery.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                    onRegister = { navController.navigate(Screen.RegisterPersonal.route) },
                    onGuest = { navController.navigate(Screen.JobDiscovery.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                    onRecruiterLogin = { navController.navigate(Screen.AcademicVacancyManager.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                    onForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                )
            }
            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.RegisterPersonal.route) {
                RegistrationPersonalInfoScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(Screen.RegisterProfessional.route) }
                )
            }
            composable(Screen.RegisterProfessional.route) {
                RegistrationProfessionalInfoScreen(
                    onBack = { navController.popBackStack() },
                    onComplete = { navController.navigate(Screen.BiometricVerification.route) }
                )
            }
            composable(Screen.BiometricVerification.route) {
                BiometricVerificationScreen(
                    onSkip = { navController.navigate(Screen.JobDiscovery.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                    onVerify = { navController.navigate(Screen.ProfileVerifiedSuccess.route) }
                )
            }
            composable(Screen.ProfileVerifiedSuccess.route) {
                ProfileVerifiedSuccessScreen(
                    onBack = { navController.popBackStack() },
                    onGoToProfile = { navController.navigate(Screen.PublicProfile.route) { popUpTo(Screen.Login.route) { inclusive = true } } }
                )
            }

            // --- DISCOVERY ---
            composable(Screen.JobDiscovery.route) {
                JobDiscoveryScreen(onMatch = { navController.navigate(Screen.MatchCelebration.route) })
            }
            composable(Screen.InternshipDiscovery.route) {
                InternshipDiscoveryScreen(onMatch = { navController.navigate(Screen.MatchCelebration.route) })
            }
            composable(Screen.CandidateDiscovery.route) {
                CandidateDiscoveryScreen(
                    onMatch = { navController.navigate(Screen.MatchCelebration.route) },
                    onViewProfile = { navController.navigate(Screen.StudentProfileDetail.route) }
                )
            }

            // --- MATCH ---
            composable(
                Screen.MatchCelebration.route,
                enterTransition = { fadeIn(tween(300)) + scaleIn(tween(350), initialScale = 0.85f) },
                exitTransition = { fadeOut(tween(300)) },
            ) {
                MatchCelebrationScreen(
                    onSendMessage = { navController.navigate(Screen.Chat.createRoute("Innovatech Corp")) },
                    onKeepSearching = { navController.popBackStack() },
                    onDismiss = { navController.popBackStack() }
                )
            }

            // --- CHAT ---
            composable(Screen.Chat.route) { backStackEntry ->
                val contactName = backStackEntry.arguments?.getString("contactName") ?: "Empresa"
                ChatScreen(
                    contactName = contactName,
                    onBack = { navController.popBackStack() },
                    onScheduleInterview = { navController.navigate(Screen.MyAppointments.route) },
                    onVideoCall = { navController.navigate(Screen.VideoInterview.route) }
                )
            }

            // --- CALENDAR ---
            composable(Screen.MyAppointments.route) {
                MyAppointmentsScreen(
                    onBack = { navController.popBackStack() },
                    onJoinMeeting = { navController.navigate(Screen.VideoInterview.route) }
                )
            }

            // --- VIDEO ---
            composable(
                Screen.VideoInterview.route,
                enterTransition = { fadeIn(tween(300)) },
                exitTransition = { fadeOut(tween(300)) },
            ) {
                VideoInterviewScreen(onEndCall = { navController.popBackStack() })
            }

            // --- PROCESS ---
            composable(Screen.ApplicationTracker.route) {
                ApplicationTrackerScreen(
                    onBack = { navController.popBackStack() },
                    onMessageRecruiter = { navController.navigate(Screen.Chat.createRoute("TechCorp")) }
                )
            }
            composable(Screen.InterviewResult.route) {
                InterviewResultScreen(
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate(Screen.DigitalContract.route) }
                )
            }
            composable(Screen.DigitalContract.route) {
                DigitalContractScreen(
                    onBack = { navController.popBackStack() },
                    onSign = { navController.popBackStack() }
                )
            }

            // --- PROFILE ---
            composable(Screen.PublicProfile.route) {
                PublicProfileScreen(
                    onBack = { navController.popBackStack() },
                    onShare = {},
                    onSendMatchRequest = { navController.navigate(Screen.Chat.createRoute("Recruiter")) },
                    onAddExperience = { navController.navigate(Screen.AddWorkExperience.route) }
                )
            }
            composable(Screen.StudentProfileDetail.route) {
                StudentProfileDetailScreen(
                    onBack = { navController.popBackStack() },
                    onReject = { navController.popBackStack() },
                    onAcceptForInterview = { navController.navigate(Screen.RecruiterFeedback.route) }
                )
            }
            composable(Screen.AddWorkExperience.route) {
                AddWorkExperienceScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }
            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }

            // --- FEED ---
            composable(Screen.ProfessionalFeed.route) {
                ProfessionalFeedScreen(
                    onCreateContent = { navController.navigate(Screen.CreateReelOrThread.route) },
                    onApply = { navController.navigate(Screen.ApplicationTracker.route) },
                    onNotifications = { navController.navigate(Screen.Notifications.route) },
                    onSearch = { navController.navigate(Screen.Search.route) },
                )
            }
            composable(Screen.CreateReelOrThread.route) {
                CreateReelOrThreadScreen(
                    onDismiss = { navController.popBackStack() },
                    onPost = { navController.popBackStack() }
                )
            }

            // --- NOTIFICATIONS ---
            composable(Screen.Notifications.route) {
                NotificationsScreen(onBack = { navController.popBackStack() })
            }

            // --- SEARCH ---
            composable(
                Screen.Search.route,
                enterTransition = { fadeIn(tween(280)) },
                exitTransition = { fadeOut(tween(280)) },
            ) {
                SearchScreen(
                    onBack = { navController.popBackStack() },
                    onProfile = { navController.navigate(Screen.StudentProfileDetail.route) },
                )
            }

            // --- SETTINGS ---
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // --- GAMIFICATION ---
            composable(Screen.DailyStreak.route) {
                DailyStreakScreen(
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate(Screen.JobDiscovery.route) }
                )
            }
            composable(Screen.Leaderboard.route) {
                LeaderboardScreen(onBack = { navController.popBackStack() })
            }

            // --- ACADEMIC / RECRUITER ---
            composable(Screen.AcademicVacancyManager.route) {
                AcademicVacancyManagerScreen(
                    onNotifications = { navController.navigate(Screen.Notifications.route) },
                    onPublishVacancy = { navController.navigate(Screen.PublishVacancy.route) },
                    onReviewApplicants = { navController.navigate(Screen.ServiceApplicants.route) },
                    onMessages = { navController.navigate(Screen.Chat.createRoute("Estudiante")) },
                    onProfile = { navController.navigate(Screen.PublicProfile.route) },
                    onSettings = { navController.navigate(Screen.Settings.route) }
                )
            }
            composable(Screen.ServiceApplicants.route) {
                ServiceApplicantsScreen(
                    onBack = { navController.popBackStack() },
                    onViewProfile = { navController.navigate(Screen.StudentProfileDetail.route) },
                    onChat = { navController.navigate(Screen.Chat.createRoute("Estudiante")) }
                )
            }
            composable(Screen.PublishVacancy.route) {
                PublishVacancyScreen(
                    onBack = { navController.popBackStack() },
                    onPublish = { navController.popBackStack() }
                )
            }
            composable(Screen.RecruiterFeedback.route) {
                RecruiterFeedbackScreen(
                    onBack = { navController.popBackStack() },
                    onSubmit = { navController.navigate(Screen.InterviewResult.route) }
                )
            }
        }
    }
}
