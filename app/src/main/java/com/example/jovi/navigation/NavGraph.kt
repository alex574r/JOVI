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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.jovi.JoviApplication
import com.example.jovi.ui.screens.academic.*
import com.example.jovi.ui.screens.auth.*
import com.example.jovi.ui.screens.calendar.MyAppointmentsScreen
import com.example.jovi.ui.screens.chat.ChatListScreen
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
import com.example.jovi.ui.screens.recruiter.*
import com.example.jovi.ui.screens.search.SearchScreen
import com.example.jovi.ui.screens.settings.*
import com.example.jovi.ui.screens.splash.SplashScreen
import com.example.jovi.ui.screens.tracking.ApplicationTrackerScreen
import com.example.jovi.ui.screens.verification.*
import com.example.jovi.ui.screens.video.VideoInterviewScreen
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.*

private val studentBottomNavItems = listOf(
    Triple(Screen.ProfessionalFeed.route, "Inicio", Pair(Icons.Filled.Home, Icons.Outlined.Home)),
    Triple(Screen.JobDiscovery.route, "Descubrir", Pair(Icons.Filled.Explore, Icons.Outlined.Explore)),
    Triple(Screen.ChatList.route, "Mensajes", Pair(Icons.Filled.Chat, Icons.Outlined.ChatBubble)),
    Triple(Screen.PublicProfile.route, "Perfil", Pair(Icons.Filled.Person, Icons.Outlined.Person)),
)

private val mainScreenRoutes = setOf(
    Screen.ProfessionalFeed.route,
    Screen.JobDiscovery.route,
    Screen.ChatList.route,
    Screen.PublicProfile.route,
)

@Composable
fun JoviNavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val app = context.applicationContext as JoviApplication

    // --- ViewModels ---
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(app.userRepository)
    )
    val feedViewModel: FeedViewModel = viewModel(
        factory = FeedViewModel.Factory(app.postRepository)
    )
    val notifViewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModel.Factory(app.notificationRepository)
    )
    val chatViewModel: ChatViewModel = viewModel(
        factory = ChatViewModel.Factory(app.messageRepository)
    )
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModel.Factory(app.userRepository, app.postRepository)
    )
    val recruiterViewModel: RecruiterViewModel = viewModel(
        factory = RecruiterViewModel.Factory(app.userRepository, app.postRepository)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in mainScreenRoutes

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
                            2 -> currentRoute == Screen.ChatList.route
                            3 -> currentRoute == Screen.PublicProfile.route
                            else -> false
                        }
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                val targetRoute = when (index) {
                                    0 -> Screen.ProfessionalFeed.route
                                    1 -> Screen.JobDiscovery.route
                                    2 -> Screen.ChatList.route
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
                SplashScreen(onFinished = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }

            // --- ONBOARDING ---
            composable(
                Screen.Onboarding.route,
                enterTransition = { fadeIn(tween(300)) },
                exitTransition = { fadeOut(tween(300)) },
            ) {
                OnboardingScreen(onFinish = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                })
            }

            // --- AUTH ---
            composable(Screen.Login.route) {
                LoginScreen(
                    onLogin = {
                        authViewModel.loginAsDemo()
                        navController.navigate(Screen.JobDiscovery.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onRegister = { navController.navigate(Screen.RegisterPersonal.route) },
                    onGuest = {
                        navController.navigate(Screen.JobDiscovery.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onRecruiterLogin = {
                        authViewModel.loginAsDemo(com.example.jovi.data.db.entity.AccountType.RECRUITER)
                        navController.navigate(Screen.RecruiterPortal.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                )
            }
            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.RegisterPersonal.route) {
                RegistrationPersonalInfoScreen(
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(Screen.RegisterProfessional.route) },
                )
            }
            composable(Screen.RegisterProfessional.route) {
                RegistrationProfessionalInfoScreen(
                    onBack = { navController.popBackStack() },
                    onComplete = { navController.navigate(Screen.BiometricVerification.route) },
                )
            }
            composable(Screen.BiometricVerification.route) {
                BiometricVerificationScreen(
                    onSkip = {
                        navController.navigate(Screen.JobDiscovery.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onVerify = { navController.navigate(Screen.ProfileVerifiedSuccess.route) },
                )
            }
            composable(Screen.ProfileVerifiedSuccess.route) {
                ProfileVerifiedSuccessScreen(
                    onBack = { navController.popBackStack() },
                    onGoToProfile = {
                        navController.navigate(Screen.PublicProfile.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                )
            }

            // --- DISCOVERY ---
            composable(Screen.JobDiscovery.route) {
                JobDiscoveryScreen(
                    onMatch = { navController.navigate(Screen.MatchCelebration.route) },
                    onVacancyDetail = { navController.navigate(Screen.VacancyDetail.route) },
                )
            }
            composable(Screen.InternshipDiscovery.route) {
                InternshipDiscoveryScreen(
                    onMatch = { navController.navigate(Screen.MatchCelebration.route) },
                    onVacancyDetail = { navController.navigate(Screen.VacancyDetail.route) },
                )
            }
            composable(Screen.CandidateDiscovery.route) {
                CandidateDiscoveryScreen(
                    onMatch = { navController.navigate(Screen.MatchCelebration.route) },
                    onViewProfile = { navController.navigate(Screen.StudentProfileDetail.route) },
                )
            }
            composable(Screen.VacancyDetail.route) {
                VacancyDetailScreen(
                    onBack = { navController.popBackStack() },
                    onApply = { navController.navigate(Screen.ApplicationTracker.route) },
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
                    onDismiss = { navController.popBackStack() },
                )
            }

            // --- CHAT LIST ---
            composable(Screen.ChatList.route) {
                val conversations by chatViewModel.conversations.collectAsState()
                ChatListScreen(
                    conversations = conversations,
                    onOpenChat = { name -> navController.navigate(Screen.Chat.createRoute(name)) },
                    onBack = { navController.popBackStack() },
                )
            }

            // --- CHAT ---
            composable(Screen.Chat.route) { backStackEntry ->
                val contactName = backStackEntry.arguments?.getString("contactName") ?: "Empresa"
                ChatScreen(
                    contactName = contactName,
                    onBack = { navController.popBackStack() },
                    onScheduleInterview = { navController.navigate(Screen.MyAppointments.route) },
                    onVideoCall = { navController.navigate(Screen.VideoInterview.route) },
                )
            }

            // --- CALENDAR ---
            composable(Screen.MyAppointments.route) {
                MyAppointmentsScreen(
                    onBack = { navController.popBackStack() },
                    onJoinMeeting = { navController.navigate(Screen.VideoInterview.route) },
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
                    onMessageRecruiter = { navController.navigate(Screen.Chat.createRoute("TechCorp")) },
                )
            }
            composable(Screen.MyApplications.route) {
                MyApplicationsScreen(
                    onBack = { navController.popBackStack() },
                    onViewDetail = { navController.navigate(Screen.ApplicationTracker.route) },
                )
            }
            composable(Screen.InterviewResult.route) {
                InterviewResultScreen(
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate(Screen.DigitalContract.route) },
                )
            }
            composable(Screen.DigitalContract.route) {
                DigitalContractScreen(
                    onBack = { navController.popBackStack() },
                    onSign = { navController.popBackStack() },
                )
            }

            // --- PROFILE ---
            composable(Screen.PublicProfile.route) {
                LaunchedEffect(Unit) { profileViewModel.loadUser(1L) }
                PublicProfileScreen(
                    onBack = { navController.popBackStack() },
                    onShare = {},
                    onSendMatchRequest = { navController.navigate(Screen.Chat.createRoute("Recruiter")) },
                    onAddExperience = { navController.navigate(Screen.AddWorkExperience.route) },
                    onEditProfile = { navController.navigate(Screen.EditProfile.route) },
                    onSettings = { navController.navigate(Screen.Settings.route) },
                    onSavedPosts = { navController.navigate(Screen.SavedPosts.route) },
                    onMyApplications = { navController.navigate(Screen.MyApplications.route) },
                    onAchievements = { navController.navigate(Screen.Achievements.route) },
                    onStreak = { navController.navigate(Screen.DailyStreak.route) },
                )
            }
            composable(Screen.StudentProfileDetail.route) {
                StudentProfileDetailScreen(
                    onBack = { navController.popBackStack() },
                    onReject = { navController.popBackStack() },
                    onAcceptForInterview = { navController.navigate(Screen.RecruiterFeedback.route) },
                )
            }
            composable(Screen.AddWorkExperience.route) {
                AddWorkExperienceScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() },
                )
            }
            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() },
                )
            }
            composable(Screen.SavedPosts.route) {
                SavedPostsScreen(onBack = { navController.popBackStack() })
            }

            // --- FEED ---
            composable(Screen.ProfessionalFeed.route) {
                val threads by feedViewModel.threads.collectAsState()
                val reels by feedViewModel.reels.collectAsState()
                ProfessionalFeedScreen(
                    threads = threads,
                    reels = reels,
                    onToggleLike = { feedViewModel.toggleLike(it) },
                    onCreateContent = { navController.navigate(Screen.CreateReelOrThread.route) },
                    onApply = { navController.navigate(Screen.VacancyDetail.route) },
                    onNotifications = { navController.navigate(Screen.Notifications.route) },
                    onSearch = { navController.navigate(Screen.Search.route) },
                    onOpenPost = { postId -> navController.navigate(Screen.PostDetail.createRoute(postId)) },
                )
            }
            composable(Screen.CreateReelOrThread.route) {
                CreateReelOrThreadScreen(
                    onDismiss = { navController.popBackStack() },
                    onPost = { navController.popBackStack() },
                )
            }
            composable(Screen.PostDetail.route) { backStackEntry ->
                val postId = backStackEntry.arguments?.getString("postId")?.toLongOrNull() ?: 0L
                val threads by feedViewModel.threads.collectAsState()
                val reels by feedViewModel.reels.collectAsState()
                val post = (threads + reels).find { it.id == postId }
                PostDetailScreen(
                    post = post,
                    onBack = { navController.popBackStack() },
                    onLike = { feedViewModel.toggleLike(it) },
                )
            }

            // --- NOTIFICATIONS ---
            composable(Screen.Notifications.route) {
                val notifications by notifViewModel.notifications.collectAsState()
                NotificationsScreen(
                    notifications = notifications,
                    onMarkRead = { notifViewModel.markRead(it) },
                    onMarkAllRead = { notifViewModel.markAllRead() },
                    onBack = { navController.popBackStack() },
                )
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
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onChangePassword = { navController.navigate(Screen.ChangePassword.route) },
                    onNotificationPrefs = { navController.navigate(Screen.NotificationPrefs.route) },
                    onPrivacy = { navController.navigate(Screen.PrivacySettings.route) },
                    onHelp = { navController.navigate(Screen.Help.route) },
                )
            }
            composable(Screen.ChangePassword.route) {
                ChangePasswordScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.NotificationPrefs.route) {
                NotificationPrefsScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.PrivacySettings.route) {
                PrivacySettingsScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Help.route) {
                HelpScreen(onBack = { navController.popBackStack() })
            }

            // --- GAMIFICATION ---
            composable(Screen.DailyStreak.route) {
                DailyStreakScreen(
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate(Screen.JobDiscovery.route) },
                )
            }
            composable(Screen.Leaderboard.route) {
                LeaderboardScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Achievements.route) {
                AchievementsScreen(onBack = { navController.popBackStack() })
            }

            // --- RECRUITER PORTAL ---
            composable(
                Screen.RecruiterPortal.route,
                enterTransition = { fadeIn(tween(300)) },
                exitTransition = { fadeOut(tween(300)) },
            ) {
                RecruiterPortalScreen(
                    navController = navController,
                    viewModel = recruiterViewModel,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onSettings = { navController.navigate(Screen.Settings.route) },
                    onOpenChat = { name -> navController.navigate(Screen.Chat.createRoute(name)) },
                    onAnalytics = { navController.navigate(Screen.RecruiterAnalytics.route) },
                    onPublishVacancy = { navController.navigate(Screen.PublishVacancy.route) },
                    onViewApplicant = { navController.navigate(Screen.StudentProfileDetail.route) },
                )
            }
            composable(Screen.RecruiterAnalytics.route) {
                RecruiterAnalyticsScreen(onBack = { navController.popBackStack() })
            }

            // --- ACADEMIC (legacy) ---
            composable(Screen.AcademicVacancyManager.route) {
                AcademicVacancyManagerScreen(
                    onNotifications = { navController.navigate(Screen.Notifications.route) },
                    onPublishVacancy = { navController.navigate(Screen.PublishVacancy.route) },
                    onReviewApplicants = { navController.navigate(Screen.ServiceApplicants.route) },
                    onMessages = { navController.navigate(Screen.Chat.createRoute("Estudiante")) },
                    onProfile = { navController.navigate(Screen.PublicProfile.route) },
                    onSettings = { navController.navigate(Screen.Settings.route) },
                )
            }
            composable(Screen.ServiceApplicants.route) {
                ServiceApplicantsScreen(
                    onBack = { navController.popBackStack() },
                    onViewProfile = { navController.navigate(Screen.StudentProfileDetail.route) },
                    onChat = { navController.navigate(Screen.Chat.createRoute("Estudiante")) },
                )
            }
            composable(Screen.PublishVacancy.route) {
                PublishVacancyScreen(
                    onBack = { navController.popBackStack() },
                    onPublish = { navController.popBackStack() },
                )
            }
            composable(Screen.RecruiterFeedback.route) {
                RecruiterFeedbackScreen(
                    onBack = { navController.popBackStack() },
                    onSubmit = { navController.navigate(Screen.InterviewResult.route) },
                )
            }
        }
    }
}
