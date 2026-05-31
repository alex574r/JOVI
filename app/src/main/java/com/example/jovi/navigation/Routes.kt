package com.example.jovi.navigation

sealed class Screen(val route: String) {
    // --- SPLASH / ONBOARDING ---
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")

    // --- AUTH ---
    object Login : Screen("login")
    object RegisterPersonal : Screen("register_personal")
    object RegisterProfessional : Screen("register_professional")
    object ForgotPassword : Screen("forgot_password")
    object BiometricVerification : Screen("biometric_verification")
    object ProfileVerifiedSuccess : Screen("profile_verified_success")

    // --- DISCOVERY ---
    object JobDiscovery : Screen("job_discovery")
    object InternshipDiscovery : Screen("internship_discovery")
    object CandidateDiscovery : Screen("candidate_discovery")
    object VacancyDetail : Screen("vacancy_detail")

    // --- FEED ---
    object ProfessionalFeed : Screen("professional_feed")
    object CreateReelOrThread : Screen("create_reel_or_thread")
    object PostDetail : Screen("post_detail/{postId}") {
        fun createRoute(postId: Long) = "post_detail/$postId"
    }

    // --- MATCH ---
    object MatchCelebration : Screen("match_celebration")

    // --- CHAT ---
    object ChatList : Screen("chat_list")
    object Chat : Screen("chat/{conversationId}") {
        fun createRoute(conversationId: Long) = "chat/$conversationId"
    }

    // --- CALENDAR & VIDEO ---
    object MyAppointments : Screen("my_appointments")
    object VideoInterview : Screen("video_interview")

    // --- HIRING PROCESS ---
    object ApplicationTracker : Screen("application_tracker")
    object MyApplications : Screen("my_applications")
    object InterviewResult : Screen("interview_result")
    object DigitalContract : Screen("digital_contract")

    // --- PROFILE ---
    object PublicProfile : Screen("public_profile")
    object StudentProfileDetail : Screen("student_profile_detail")
    object AddWorkExperience : Screen("add_work_experience")
    object EditProfile : Screen("edit_profile")
    object SavedPosts : Screen("saved_posts")

    // --- SOCIAL ---
    object Notifications : Screen("notifications")
    object Search : Screen("search")

    // --- GAMIFICATION ---
    object DailyStreak : Screen("daily_streak")
    object Leaderboard : Screen("leaderboard")
    object Achievements : Screen("achievements")

    // --- SETTINGS ---
    object Settings : Screen("settings")
    object ChangePassword : Screen("change_password")
    object NotificationPrefs : Screen("notification_prefs")
    object PrivacySettings : Screen("privacy_settings")
    object Help : Screen("help")

    // --- ACADEMIC / RECRUITER ---
    object AcademicVacancyManager : Screen("academic_vacancy_manager")
    object ServiceApplicants : Screen("service_applicants")
    object PublishVacancy : Screen("publish_vacancy")
    object RecruiterFeedback : Screen("recruiter_feedback")
    object RecruiterPortal : Screen("recruiter_portal")
    object RecruiterAnalytics : Screen("recruiter_analytics")
}
