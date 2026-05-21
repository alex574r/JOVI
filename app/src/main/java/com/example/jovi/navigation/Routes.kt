package com.example.jovi.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object RegisterPersonal : Screen("register_personal")
    object RegisterProfessional : Screen("register_professional")
    object BiometricVerification : Screen("biometric_verification")
    object ProfileVerifiedSuccess : Screen("profile_verified_success")

    object JobDiscovery : Screen("job_discovery")
    object InternshipDiscovery : Screen("internship_discovery")
    object CandidateDiscovery : Screen("candidate_discovery")

    object ProfessionalFeed : Screen("professional_feed")
    object CreateReelOrThread : Screen("create_reel_or_thread")

    object MatchCelebration : Screen("match_celebration")

    object Chat : Screen("chat/{contactName}") {
        fun createRoute(contactName: String) = "chat/$contactName"
    }
    object MyAppointments : Screen("my_appointments")
    object VideoInterview : Screen("video_interview")

    object ApplicationTracker : Screen("application_tracker")
    object InterviewResult : Screen("interview_result")
    object DigitalContract : Screen("digital_contract")

    object PublicProfile : Screen("public_profile")
    object StudentProfileDetail : Screen("student_profile_detail")
    object AddWorkExperience : Screen("add_work_experience")

    object DailyStreak : Screen("daily_streak")
    object Leaderboard : Screen("leaderboard")

    object AcademicVacancyManager : Screen("academic_vacancy_manager")
    object ServiceApplicants : Screen("service_applicants")
    object PublishVacancy : Screen("publish_vacancy")
    object RecruiterFeedback : Screen("recruiter_feedback")
}
