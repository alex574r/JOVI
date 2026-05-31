# Jovi Full App Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Conectar toda la infraestructura Room existente con UI real: auth con sesión persistente, chat completo, dark mode funcional, datos dinámicos desde BD, 5 usuarios con contraseñas reales.

**Architecture:** MVVM + Repository + Room. ViewModels creados en NavGraph/MainActivity con Factories. StateFlow para todo estado reactivo. SharedPreferences solo para sesión activa y splash_skip.

**Tech Stack:** Jetpack Compose, Room 2.x, Navigation Compose, biometric:1.1.0 (nueva dep), coroutines, StateFlow.

**Build check command:** `.\gradlew.bat compileDebugKotlin` (expected: BUILD SUCCESSFUL)

**Passwords for test users:** carlos123 / ana123 / luis123 / stanford123 / innovatech123

---

## Task 1: DB Schema v2 — entidades actualizadas + seed completo

**Files:**
- Modify: `app/src/main/java/com/example/jovi/data/db/entity/UserEntity.kt`
- Modify: `app/src/main/java/com/example/jovi/data/db/entity/MessageEntity.kt`
- Modify: `app/src/main/java/com/example/jovi/data/db/entity/UserSettingsEntity.kt`
- Modify: `app/src/main/java/com/example/jovi/data/db/JoviDatabase.kt`

- [ ] **Step 1.1: Agregar `password` y `profileImageUrl` a UserEntity**

```kotlin
// app/src/main/java/com/example/jovi/data/db/entity/UserEntity.kt
package com.example.jovi.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AccountType { STUDENT, RECRUITER }

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val displayName: String,
    val email: String,
    val password: String = "",
    val avatarInitials: String,
    val bio: String = "",
    val accountType: AccountType = AccountType.STUDENT,
    val isVerified: Boolean = false,
    val streakDays: Int = 0,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val university: String = "",
    val company: String = "",
    val location: String = "",
    val headline: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)
```

- [ ] **Step 1.2: Agregar MessageStatus + campos de archivo a MessageEntity**

```kotlin
// app/src/main/java/com/example/jovi/data/db/entity/MessageEntity.kt
package com.example.jovi.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MessageType { TEXT, FILE, INTERVIEW_CARD, SYSTEM }
enum class MessageStatus { SENT, DELIVERED, READ }

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val conversationId: Long,
    val senderId: Long,
    val senderName: String,
    val content: String,
    val type: MessageType = MessageType.TEXT,
    val status: MessageStatus = MessageStatus.SENT,
    val isRead: Boolean = false,
    val fileName: String? = null,
    val fileSize: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
)
```

- [ ] **Step 1.3: Agregar `biometricEnabled` a UserSettingsEntity**

```kotlin
// app/src/main/java/com/example/jovi/data/db/entity/UserSettingsEntity.kt
package com.example.jovi.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val userId: Long,
    val notificationsEnabled: Boolean = true,
    val matchNotifications: Boolean = true,
    val messageNotifications: Boolean = true,
    val darkMode: Boolean = false,
    val biometricEnabled: Boolean = false,
    val language: String = "es",
    val profileVisibility: String = "public",
)
```

- [ ] **Step 1.4: Agregar TypeConverters para enums nuevos y actualizar DB a v2 con destructiveMigration + seed completo**

```kotlin
// app/src/main/java/com/example/jovi/data/db/JoviDatabase.kt
package com.example.jovi.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.jovi.data.db.dao.*
import com.example.jovi.data.db.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Converters {
    @TypeConverter fun fromAccountType(v: AccountType): String = v.name
    @TypeConverter fun toAccountType(v: String): AccountType = AccountType.valueOf(v)
    @TypeConverter fun fromPostType(v: PostType): String = v.name
    @TypeConverter fun toPostType(v: String): PostType = PostType.valueOf(v)
    @TypeConverter fun fromMessageType(v: MessageType): String = v.name
    @TypeConverter fun toMessageType(v: String): MessageType = MessageType.valueOf(v)
    @TypeConverter fun fromMessageStatus(v: MessageStatus): String = v.name
    @TypeConverter fun toMessageStatus(v: String): MessageStatus = MessageStatus.valueOf(v)
    @TypeConverter fun fromNotificationType(v: NotificationType): String = v.name
    @TypeConverter fun toNotificationType(v: String): NotificationType = NotificationType.valueOf(v)
    @TypeConverter fun fromApplicationStatus(v: ApplicationStatus): String = v.name
    @TypeConverter fun toApplicationStatus(v: String): ApplicationStatus = ApplicationStatus.valueOf(v)
}

@Database(
    entities = [
        UserEntity::class, PostEntity::class, MessageEntity::class,
        ConversationEntity::class, NotificationEntity::class,
        UserSettingsEntity::class, JobApplicationEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class JoviDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun messageDao(): MessageDao
    abstract fun notificationDao(): NotificationDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile private var INSTANCE: JoviDatabase? = null

        fun getInstance(context: Context): JoviDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, JoviDatabase::class.java, "jovi.db")
                    .fallbackToDestructiveMigration()
                    .addCallback(SeedCallback(context))
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    private class SeedCallback(private val context: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val database = getInstance(context)
                if (database.userDao().count() == 0) seedData(database)
            }
        }

        private suspend fun seedData(db: JoviDatabase) {
            val now = System.currentTimeMillis()
            val users = listOf(
                UserEntity(id = 1, username = "carlos_dev", displayName = "Carlos Mendoza", email = "carlos@example.com", password = "carlos123", avatarInitials = "CM", bio = "Software developer apasionado por la tecnologia mobile. Buscando practicas en empresas innovadoras.", headline = "Android Developer · UNAM", accountType = AccountType.STUDENT, isVerified = true, streakDays = 14, followerCount = 342, followingCount = 128, university = "UNAM", location = "CDMX"),
                UserEntity(id = 2, username = "ana_design", displayName = "Ana Garcia", email = "ana@example.com", password = "ana123", avatarInitials = "AG", bio = "UX/UI Designer con enfoque en productos digitales y accesibilidad.", headline = "UX Designer · Tec de Monterrey", accountType = AccountType.STUDENT, isVerified = false, streakDays = 7, followerCount = 215, followingCount = 89, university = "Tec de Monterrey", location = "Monterrey"),
                UserEntity(id = 3, username = "luis_data", displayName = "Luis Torres", email = "luis@example.com", password = "luis123", avatarInitials = "LT", bio = "Data Scientist. Machine Learning y analisis de datos para decision making.", headline = "Data Scientist · IPN", accountType = AccountType.STUDENT, isVerified = false, streakDays = 3, followerCount = 178, followingCount = 201, university = "IPN", location = "CDMX"),
                UserEntity(id = 4, username = "stanford_careers", displayName = "Stanford University", email = "careers@stanford.edu", password = "stanford123", avatarInitials = "SU", bio = "Conectando talento academico con oportunidades de impacto.", headline = "Oficina de Carreras", accountType = AccountType.RECRUITER, isVerified = true, streakDays = 0, followerCount = 12400, followingCount = 45, company = "Stanford University", location = "California, USA"),
                UserEntity(id = 5, username = "innovatech_hr", displayName = "Innovatech Corp", email = "hr@innovatech.com", password = "innovatech123", avatarInitials = "IC", bio = "Empresa de tecnologia buscando talento joven. Practicas con impacto real.", headline = "Tech Company · CDMX", accountType = AccountType.RECRUITER, isVerified = true, streakDays = 0, followerCount = 5800, followingCount = 23, company = "Innovatech Corp", location = "CDMX"),
            )
            db.userDao().insertAll(users)

            val posts = listOf(
                PostEntity(authorId = 1, authorName = "Carlos Mendoza", authorInitials = "CM", authorUsername = "carlos_dev", type = PostType.THREAD, content = "Acabo de terminar mi primer proyecto con Kotlin Multiplatform. La curva de aprendizaje es real pero los resultados valen la pena.", likeCount = 47, commentCount = 12, tags = "kotlin,android,mobile", timestamp = now - 3_600_000),
                PostEntity(authorId = 2, authorName = "Ana Garcia", authorInitials = "AG", authorUsername = "ana_design", type = PostType.THREAD, content = "Tip de UX: antes de disenar cualquier flujo, mapea los estados vacios. Son los momentos mas criticos de una app.", likeCount = 93, commentCount = 28, tags = "ux,design,tips", timestamp = now - 7_200_000),
                PostEntity(authorId = 1, authorName = "Carlos Mendoza", authorInitials = "CM", authorUsername = "carlos_dev", type = PostType.REEL, content = "Tutorial: como implementar SwipeCard en Jetpack Compose en menos de 50 lineas.", likeCount = 156, commentCount = 34, tags = "compose,android,tutorial", timestamp = now - 86_400_000),
                PostEntity(authorId = 3, authorName = "Luis Torres", authorInitials = "LT", authorUsername = "luis_data", type = PostType.THREAD, content = "Resumen de tendencias en Data Science 2025: LLMs especializados, edge AI, y modelos interpretables.", likeCount = 71, commentCount = 19, tags = "datascience,ml,ai", timestamp = now - 172_800_000),
                PostEntity(authorId = 2, authorName = "Ana Garcia", authorInitials = "AG", authorUsername = "ana_design", type = PostType.REEL, content = "Redisene este landing page en 2 horas. Antes vs despues. Los pequenos detalles de espaciado marcan la diferencia.", likeCount = 204, commentCount = 41, tags = "ux,ui,redesign", timestamp = now - 259_200_000),
                PostEntity(authorId = 5, authorName = "Innovatech Corp", authorInitials = "IC", authorUsername = "innovatech_hr", type = PostType.THREAD, content = "Abrimos 3 posiciones de practicas en desarrollo mobile. Stack: Kotlin + Compose. Modalidad hibrida. Aplica en la app.", likeCount = 128, commentCount = 53, tags = "empleo,practicas,mobile", timestamp = now - 21_600_000),
            )
            db.postDao().insertAll(posts)

            val convs = listOf(
                ConversationEntity(id = 1, userId1 = 1, userId2 = 5, user1Name = "Carlos Mendoza", user2Name = "Innovatech Corp", user1Initials = "CM", user2Initials = "IC", lastMessage = "Nos interesa tu perfil para mobile developer", lastMessageAt = now - 1_800_000, unreadCount = 1),
                ConversationEntity(id = 2, userId1 = 2, userId2 = 4, user1Name = "Ana Garcia", user2Name = "Stanford University", user1Initials = "AG", user2Initials = "SU", lastMessage = "Tu perfil fue seleccionado para la siguiente fase", lastMessageAt = now - 3_600_000, unreadCount = 2),
                ConversationEntity(id = 3, userId1 = 1, userId2 = 4, user1Name = "Carlos Mendoza", user2Name = "Stanford University", user1Initials = "CM", user2Initials = "SU", lastMessage = "Gracias por tu interes en el programa", lastMessageAt = now - 86_400_000, unreadCount = 0),
            )
            db.messageDao().insertAllConversations(convs)

            val messages = listOf(
                MessageEntity(conversationId = 1, senderId = 5, senderName = "Innovatech Corp", content = "Hola Carlos, vimos tu perfil en Jovi y nos parece muy interesante.", status = MessageStatus.READ, isRead = true, timestamp = now - 7_200_000),
                MessageEntity(conversationId = 1, senderId = 1, senderName = "Carlos Mendoza", content = "Muchas gracias! Estoy muy interesado en conocer mas sobre la posicion.", status = MessageStatus.READ, isRead = true, timestamp = now - 3_600_000),
                MessageEntity(conversationId = 1, senderId = 5, senderName = "Innovatech Corp", content = "Tenemos una posicion de Mobile Developer con Kotlin y Compose. Te adjunto la descripcion.", status = MessageStatus.DELIVERED, isRead = false, timestamp = now - 2_000_000),
                MessageEntity(conversationId = 1, senderId = 5, senderName = "Innovatech Corp", content = "Job_Description_Mobile.pdf", type = MessageType.FILE, fileName = "Job_Description_Mobile.pdf", fileSize = "345 KB", status = MessageStatus.DELIVERED, isRead = false, timestamp = now - 1_900_000),
                MessageEntity(conversationId = 1, senderId = 5, senderName = "Innovatech Corp", content = "Nos interesa tu perfil para mobile developer", status = MessageStatus.SENT, isRead = false, timestamp = now - 1_800_000),
                MessageEntity(conversationId = 2, senderId = 4, senderName = "Stanford University", content = "Hola Ana, revisamos tu portafolio y quedamos impresionados.", status = MessageStatus.READ, isRead = true, timestamp = now - 5_400_000),
                MessageEntity(conversationId = 2, senderId = 2, senderName = "Ana Garcia", content = "Muchas gracias! Es un honor ser considerada.", status = MessageStatus.READ, isRead = true, timestamp = now - 4_500_000),
                MessageEntity(conversationId = 2, senderId = 4, senderName = "Stanford University", content = "Aqui tienes los detalles del programa de verano.", status = MessageStatus.DELIVERED, isRead = false, timestamp = now - 4_000_000),
                MessageEntity(conversationId = 2, senderId = 4, senderName = "Stanford University", content = "Summer_Program_2025.pdf", type = MessageType.FILE, fileName = "Summer_Program_2025.pdf", fileSize = "1.2 MB", status = MessageStatus.DELIVERED, isRead = false, timestamp = now - 3_900_000),
                MessageEntity(conversationId = 2, senderId = 4, senderName = "Stanford University", content = "Tu perfil fue seleccionado para la siguiente fase", status = MessageStatus.SENT, isRead = false, timestamp = now - 3_600_000),
                MessageEntity(conversationId = 3, senderId = 4, senderName = "Stanford University", content = "Hola Carlos, gracias por tu interes en nuestro programa de investigacion.", status = MessageStatus.READ, isRead = true, timestamp = now - 90_000_000),
                MessageEntity(conversationId = 3, senderId = 1, senderName = "Carlos Mendoza", content = "Gracias por tu interes en el programa", status = MessageStatus.READ, isRead = true, timestamp = now - 86_400_000),
            )
            db.messageDao().insertAllMessages(messages)

            val notifications = listOf(
                NotificationEntity(userId = 1, type = NotificationType.MATCH, title = "Nuevo Match", body = "Innovatech Corp hizo match contigo para Mobile Developer", senderInitials = "IC", isRead = false, timestamp = now - 1_800_000),
                NotificationEntity(userId = 1, type = NotificationType.LIKE, title = "Le gusto tu post", body = "Ana Garcia dio like a tu publicacion sobre Kotlin", senderInitials = "AG", isRead = false, timestamp = now - 3_600_000),
                NotificationEntity(userId = 1, type = NotificationType.MESSAGE, title = "Nuevo mensaje", body = "Innovatech Corp: Nos interesa tu perfil", senderInitials = "IC", isRead = true, timestamp = now - 7_200_000),
                NotificationEntity(userId = 2, type = NotificationType.MATCH, title = "Nuevo Match", body = "Stanford University hizo match con tu perfil", senderInitials = "SU", isRead = false, timestamp = now - 3_600_000),
                NotificationEntity(userId = 2, type = NotificationType.MESSAGE, title = "Nuevo mensaje", body = "Stanford: Tu perfil fue seleccionado", senderInitials = "SU", isRead = false, timestamp = now - 3_600_000),
                NotificationEntity(userId = 3, type = NotificationType.FOLLOW, title = "Nuevo seguidor", body = "Carlos Mendoza ahora te sigue", senderInitials = "CM", isRead = true, timestamp = now - 86_400_000),
            )
            db.notificationDao().insertAll(notifications)

            users.forEach { db.settingsDao().upsert(UserSettingsEntity(userId = it.id)) }
        }
    }
}
```

- [ ] **Step 1.5: Compilar**

```
.\gradlew.bat compileDebugKotlin
```
Expected: BUILD SUCCESSFUL (si falla con error de TypeConverter, verificar que `MessageStatus` y `MessageType` tienen sus pares de converters)

- [ ] **Step 1.6: Commit**

```
git add app/src/main/java/com/example/jovi/data/
git commit -m "feat: db v2 — password, MessageStatus, biometricEnabled, seed completo"
```

---

## Task 2: Auth con sesión persistente

**Files:**
- Modify: `app/src/main/java/com/example/jovi/data/db/dao/UserDao.kt`
- Modify: `app/src/main/java/com/example/jovi/data/repository/UserRepository.kt`
- Modify: `app/src/main/java/com/example/jovi/viewmodel/AuthViewModel.kt`

- [ ] **Step 2.1: Agregar `getUserByEmailAndPassword` a UserDao**

```kotlin
// En UserDao.kt, agregar dentro de la interfaz:
@Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity?
```

- [ ] **Step 2.2: Agregar `loginWithPassword` a UserRepository**

```kotlin
// En UserRepository.kt, agregar:
suspend fun loginWithPassword(email: String, password: String): UserEntity? =
    userDao.getUserByEmailAndPassword(email, password)
```

- [ ] **Step 2.3: Reescribir AuthViewModel con SharedPreferences para sesión**

```kotlin
// app/src/main/java/com/example/jovi/viewmodel/AuthViewModel.kt
package com.example.jovi.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jovi.data.db.entity.AccountType
import com.example.jovi.data.db.entity.UserEntity
import com.example.jovi.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: UserEntity) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val userRepository: UserRepository,
    private val prefs: SharedPreferences,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    init {
        restoreSession()
    }

    private fun restoreSession() {
        val savedId = prefs.getLong("current_user_id", -1L)
        if (savedId >= 0) {
            viewModelScope.launch {
                val user = userRepository.getUserByEmail(
                    prefs.getString("current_user_email", "") ?: ""
                )
                if (user != null) {
                    _currentUser.value = user
                    _authState.value = AuthState.Success(user)
                }
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank()) { _authState.value = AuthState.Error("Ingresa tu correo"); return }
        if (password.isBlank()) { _authState.value = AuthState.Error("Ingresa tu contraseña"); return }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = userRepository.loginWithPassword(email.trim(), password)
            if (user != null) {
                saveSession(user)
                _currentUser.value = user
                _authState.value = AuthState.Success(user)
            } else {
                val exists = userRepository.getUserByEmail(email.trim())
                _authState.value = AuthState.Error(
                    if (exists != null) "Contraseña incorrecta" else "Email no encontrado"
                )
            }
        }
    }

    fun loginAsDemo(accountType: AccountType = AccountType.STUDENT) {
        val demoEmail = if (accountType == AccountType.RECRUITER) "hr@innovatech.com" else "carlos@example.com"
        val demoPass = if (accountType == AccountType.RECRUITER) "innovatech123" else "carlos123"
        login(demoEmail, demoPass)
    }

    fun logout() {
        prefs.edit().remove("current_user_id").remove("current_user_email").apply()
        _currentUser.value = null
        _authState.value = AuthState.Idle
    }

    fun clearError() { _authState.value = AuthState.Idle }

    private fun saveSession(user: UserEntity) {
        prefs.edit()
            .putLong("current_user_id", user.id)
            .putString("current_user_email", user.email)
            .apply()
    }

    class Factory(
        private val repository: UserRepository,
        private val prefs: SharedPreferences,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository, prefs) as T
        }
    }
}
```

- [ ] **Step 2.4: Compilar**

```
.\gradlew.bat compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 2.5: Commit**

```
git add app/src/main/java/com/example/jovi/data/db/dao/UserDao.kt
git add app/src/main/java/com/example/jovi/data/repository/UserRepository.kt
git add app/src/main/java/com/example/jovi/viewmodel/AuthViewModel.kt
git commit -m "feat: auth con password y sesion persistente en SharedPreferences"
```

---

## Task 3: LoginScreen con UI de auth real

**Files:**
- Modify: `app/src/main/java/com/example/jovi/ui/screens/auth/LoginScreen.kt`
- Modify: `app/src/main/java/com/example/jovi/navigation/NavGraph.kt`

- [ ] **Step 3.1: Reescribir LoginScreen con authViewModel y estados de error/loading**

```kotlin
// app/src/main/java/com/example/jovi/ui/screens/auth/LoginScreen.kt
package com.example.jovi.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.AuthState
import com.example.jovi.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onLoginAsRecruiter: () -> Unit,
    onRegister: () -> Unit,
    onGuest: () -> Unit,
    onForgotPassword: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (val s = authState) {
            is AuthState.Success -> {
                if (s.user.accountType == com.example.jovi.data.db.entity.AccountType.RECRUITER) {
                    onLoginAsRecruiter()
                } else {
                    onLoginSuccess()
                }
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(56.dp))
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(24.dp))

        Surface(modifier = Modifier.size(140.dp), shape = CircleShape, color = PrimaryLight) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(64.dp), tint = SecondaryColor)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("carlos@example.com / carlos123", style = MaterialTheme.typography.labelSmall, color = TextSecondary, textAlign = TextAlign.Center)
        Text("ana@example.com / ana123  ·  hr@innovatech.com / innovatech123", style = MaterialTheme.typography.labelSmall, color = TextSecondary, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))

        if (authState is AuthState.Error) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = ErrorColor.copy(0.1f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    (authState as AuthState.Error).message,
                    modifier = Modifier.padding(12.dp),
                    color = ErrorColor,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Spacer(Modifier.height(12.dp))
        }

        JoviTextField(value = email, onValueChange = { email = it; authViewModel.clearError() }, placeholder = "Correo electrónico", leadingIcon = Icons.Outlined.Email, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        Spacer(Modifier.height(14.dp))
        JoviTextField(value = password, onValueChange = { password = it; authViewModel.clearError() }, placeholder = "Contraseña", leadingIcon = Icons.Outlined.Lock, isPassword = true)
        Spacer(Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            TextButton(onClick = onForgotPassword) {
                Text("¿Olvidaste la contraseña?", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(20.dp))

        if (authState is AuthState.Loading) {
            CircularProgressIndicator(color = PrimaryDark, modifier = Modifier.size(36.dp))
        } else {
            JoviPrimaryButton(text = "Ingresar", onClick = { authViewModel.login(email, password) })
        }

        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = TertiaryColor)
            Text("  ó  ", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            HorizontalDivider(modifier = Modifier.weight(1f), color = TertiaryColor)
        }
        Spacer(Modifier.height(16.dp))
        JoviSecondaryButton(text = "Crear una cuenta", onClick = onRegister, leadingIcon = Icons.Outlined.PersonAdd)
        Spacer(Modifier.height(12.dp))
        JoviPrimaryButton(text = "Continuar como Invitado", onClick = onGuest, leadingIcon = Icons.Outlined.Person)
        Spacer(Modifier.height(32.dp))
    }
}
```

- [ ] **Step 3.2: Actualizar el composable de Login en NavGraph — cambiar firma + pasar authViewModel**

En `NavGraph.kt`, localizar el bloque `composable(Screen.Login.route)` y reemplazar:

```kotlin
composable(Screen.Login.route) {
    LoginScreen(
        authViewModel = authViewModel,
        onLoginSuccess = {
            navController.navigate(Screen.JobDiscovery.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        },
        onLoginAsRecruiter = {
            navController.navigate(Screen.RecruiterPortal.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        },
        onRegister = { navController.navigate(Screen.RegisterPersonal.route) },
        onGuest = {
            navController.navigate(Screen.JobDiscovery.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        },
        onForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
    )
}
```

- [ ] **Step 3.3: Compilar**

```
.\gradlew.bat compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 3.4: Commit**

```
git add app/src/main/java/com/example/jovi/ui/screens/auth/LoginScreen.kt
git add app/src/main/java/com/example/jovi/navigation/NavGraph.kt
git commit -m "feat: LoginScreen con auth real, estados de error y loading"
```

---

## Task 4: SettingsViewModel + Dark Mode funcional

**Files:**
- Create: `app/src/main/java/com/example/jovi/viewmodel/SettingsViewModel.kt`
- Modify: `app/src/main/java/com/example/jovi/ui/theme/Theme.kt`
- Modify: `app/src/main/java/com/example/jovi/MainActivity.kt`
- Modify: `app/src/main/java/com/example/jovi/navigation/NavGraph.kt`

- [ ] **Step 4.1: Crear SettingsViewModel**

```kotlin
// app/src/main/java/com/example/jovi/viewmodel/SettingsViewModel.kt
package com.example.jovi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jovi.data.db.entity.UserSettingsEntity
import com.example.jovi.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _settings = MutableStateFlow<UserSettingsEntity?>(null)
    val settings: StateFlow<UserSettingsEntity?> = _settings.asStateFlow()

    fun loadSettings(userId: Long) {
        viewModelScope.launch {
            userRepository.getSettings(userId).collect { _settings.value = it }
        }
    }

    fun toggleDarkMode() = updateSettings { copy(darkMode = !darkMode) }
    fun toggleNotifications() = updateSettings { copy(notificationsEnabled = !notificationsEnabled) }
    fun toggleMatchNotifications() = updateSettings { copy(matchNotifications = !matchNotifications) }
    fun toggleMessageNotifications() = updateSettings { copy(messageNotifications = !messageNotifications) }
    fun toggleBiometric() = updateSettings { copy(biometricEnabled = !biometricEnabled) }

    private fun updateSettings(transform: UserSettingsEntity.() -> UserSettingsEntity) {
        val current = _settings.value ?: return
        viewModelScope.launch {
            userRepository.upsertSettings(current.transform())
        }
    }

    class Factory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repository) as T
        }
    }
}
```

- [ ] **Step 4.2: Agregar soporte darkTheme a JoviTheme**

```kotlin
// app/src/main/java/com/example/jovi/ui/theme/Theme.kt
package com.example.jovi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val JoviLightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = SecondaryColor,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = SecondaryColor,
    secondary = SecondaryColor,
    onSecondary = BackgroundColor,
    secondaryContainer = PrimaryLight,
    onSecondaryContainer = SecondaryColor,
    tertiary = TertiaryColor,
    onTertiary = TextPrimary,
    tertiaryContainer = SurfaceColor,
    background = BackgroundColor,
    onBackground = TextPrimary,
    surface = BackgroundColor,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceColor,
    onSurfaceVariant = TextSecondary,
    outline = TertiaryColor,
    outlineVariant = DividerColor,
    error = ErrorColor,
    onError = BackgroundColor,
)

private val JoviDarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = SecondaryColor,
    primaryContainer = SecondaryColor,
    onPrimaryContainer = PrimaryColor,
    secondary = PrimaryColor,
    onSecondary = SecondaryColor,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkDivider,
    outlineVariant = DarkDivider,
    error = ErrorColor,
    onError = BackgroundColor,
)

@Composable
fun JoviTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) JoviDarkColorScheme else JoviLightColorScheme,
        typography = Typography,
        content = content,
    )
}
```

- [ ] **Step 4.3: Agregar colores dark a Color.kt**

```kotlin
// Al final de app/src/main/java/com/example/jovi/ui/theme/Color.kt, agregar:
val DarkBackground = Color(0xFF0F1A0A)
val DarkSurface = Color(0xFF1A2E12)
val DarkSurfaceVariant = Color(0xFF243B1A)
val DarkTextPrimary = Color(0xFFE8FAE0)
val DarkTextSecondary = Color(0xFF9FBF8A)
val DarkDivider = Color(0xFF2E4A22)
```

- [ ] **Step 4.4: Actualizar MainActivity para recoger darkMode de SettingsViewModel**

```kotlin
// app/src/main/java/com/example/jovi/MainActivity.kt
package com.example.jovi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.example.jovi.data.db.JoviDatabase
import com.example.jovi.navigation.JoviNavGraph
import com.example.jovi.ui.theme.JoviTheme
import com.example.jovi.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels {
        val app = application as JoviApplication
        SettingsViewModel.Factory(app.userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Coil.setImageLoader(ImageLoader.Builder(this).components { add(SvgDecoder.Factory()) }.build())
        JoviDatabase.getInstance(applicationContext)
        enableEdgeToEdge()
        setContent {
            val settings by settingsViewModel.settings.collectAsState()
            val darkMode = settings?.darkMode ?: false
            JoviTheme(darkTheme = darkMode) {
                val navController = rememberNavController()
                JoviNavGraph(navController = navController, settingsViewModel = settingsViewModel)
            }
        }
    }
}
```

- [ ] **Step 4.5: Agregar `settingsViewModel` como parámetro a JoviNavGraph y conectar con AuthViewModel para cargar settings al login**

En `NavGraph.kt`, actualizar la firma de `JoviNavGraph` y el init de ViewModels:

```kotlin
// Cambiar firma:
@Composable
fun JoviNavGraph(navController: NavHostController, settingsViewModel: SettingsViewModel) {
    // ... resto igual ...
    
    // Cargar settings cuando hay usuario activo:
    val currentUser by authViewModel.currentUser.collectAsState()
    LaunchedEffect(currentUser?.id) {
        currentUser?.id?.let { settingsViewModel.loadSettings(it) }
    }
    
    // ... resto de NavHost igual ...
}
```

También actualizar el `startDestination` para sesión activa. Localizar donde se define `skipSplash` y agregar:

```kotlin
val context = LocalContext.current
val prefs = remember { context.getSharedPreferences("jovi_prefs", Context.MODE_PRIVATE) }
val skipSplash = remember { prefs.getBoolean("splash_skip", false) }
val hasSession = remember { prefs.getLong("current_user_id", -1L) >= 0 }
val startDestination = when {
    hasSession -> Screen.JobDiscovery.route
    skipSplash -> Screen.Onboarding.route
    else -> Screen.Splash.route
}
```

También actualizar la Factory de AuthViewModel en NavGraph para pasar `prefs`:

```kotlin
val authViewModel: AuthViewModel = viewModel(
    factory = AuthViewModel.Factory(app.userRepository, prefs)
)
```

- [ ] **Step 4.6: Compilar**

```
.\gradlew.bat compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 4.7: Commit**

```
git add app/src/main/java/com/example/jovi/viewmodel/SettingsViewModel.kt
git add app/src/main/java/com/example/jovi/ui/theme/
git add app/src/main/java/com/example/jovi/MainActivity.kt
git add app/src/main/java/com/example/jovi/navigation/NavGraph.kt
git commit -m "feat: SettingsViewModel, dark mode funcional, sesion activa salta a app"
```

---

## Task 5: SettingsScreen + NotificationPrefs conectados a BD

**Files:**
- Modify: `app/src/main/java/com/example/jovi/ui/screens/settings/SettingsScreen.kt`
- Modify: `app/src/main/java/com/example/jovi/ui/screens/settings/NotificationPrefsScreen.kt`
- Modify: `app/src/main/java/com/example/jovi/navigation/NavGraph.kt`

- [ ] **Step 5.1: Actualizar SettingsScreen para recibir SettingsViewModel y AuthViewModel**

```kotlin
// app/src/main/java/com/example/jovi/ui/screens/settings/SettingsScreen.kt
package com.example.jovi.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.AuthViewModel
import com.example.jovi.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel,
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onChangePassword: () -> Unit = {},
    onNotificationPrefs: () -> Unit = {},
    onPrivacy: () -> Unit = {},
    onHelp: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onBiometric: () -> Unit = {},
) {
    val settings by settingsViewModel.settings.collectAsState()

    Scaffold(
        topBar = { JoviTopBar(title = "Ajustes", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            SectionLabel("Perfil y cuenta")
            SettingsArrowRow(icon = Icons.Outlined.Person, title = "Editar perfil", subtitle = "Nombre, bio, universidad", onClick = onEditProfile)
            SettingsArrowRow(icon = Icons.Outlined.Lock, title = "Cambiar contraseña", subtitle = "Actualiza tu contraseña de acceso", onClick = onChangePassword)
            SettingsToggleRow(
                icon = Icons.Outlined.Fingerprint,
                title = "Autenticación biométrica",
                subtitle = "Huella dactilar o Face ID",
                checked = settings?.biometricEnabled ?: false,
                onCheckedChange = { settingsViewModel.toggleBiometric() }
            )

            SectionLabel("Preferencias")
            SettingsToggleRow(
                icon = Icons.Outlined.Notifications,
                title = "Notificaciones push",
                subtitle = "Recibir alertas en tiempo real",
                checked = settings?.notificationsEnabled ?: true,
                onCheckedChange = { settingsViewModel.toggleNotifications() }
            )
            SettingsArrowRow(icon = Icons.Outlined.NotificationsNone, title = "Preferencias de notificación", subtitle = "Que tipo de alertas recibir", onClick = onNotificationPrefs)
            SettingsToggleRow(
                icon = Icons.Outlined.DarkMode,
                title = "Modo oscuro",
                subtitle = "Cambia el tema de la app",
                checked = settings?.darkMode ?: false,
                onCheckedChange = { settingsViewModel.toggleDarkMode() }
            )

            SectionLabel("Privacidad y seguridad")
            SettingsArrowRow(icon = Icons.Outlined.Security, title = "Privacidad", subtitle = "Controla quien ve tu información", onClick = onPrivacy)

            SectionLabel("Soporte")
            SettingsArrowRow(icon = Icons.Outlined.HelpOutline, title = "Centro de ayuda", subtitle = "Preguntas frecuentes y soporte", onClick = onHelp)
            SettingsArrowRow(icon = Icons.Outlined.Info, title = "Acerca de Jovi", subtitle = "Versión 1.0.2", onClick = {})

            Spacer(Modifier.height(24.dp))
            Surface(
                onClick = { authViewModel.logout(); onLogout() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                color = StatusRejected.copy(0.08f),
            ) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Logout, contentDescription = null, tint = StatusRejected)
                    Text("Cerrar sesión", style = MaterialTheme.typography.bodyMedium, color = StatusRejected, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionLabel(title: String) {
    Text(title.uppercase(), style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
}

@Composable
private fun SettingsToggleRow(icon: ImageVector, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(22.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedThumbColor = BackgroundColor, checkedTrackColor = PrimaryDark))
    }
    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
private fun SettingsArrowRow(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(onClick = onClick, color = BackgroundColor) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(22.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = TertiaryColor)
        }
    }
    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
}
```

- [ ] **Step 5.2: Actualizar NotificationPrefsScreen con toggles reales**

```kotlin
// app/src/main/java/com/example/jovi/ui/screens/settings/NotificationPrefsScreen.kt
package com.example.jovi.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.JoviTopBar
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.SettingsViewModel

@Composable
fun NotificationPrefsScreen(
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit,
) {
    val settings by settingsViewModel.settings.collectAsState()

    Scaffold(
        topBar = { JoviTopBar(title = "Notificaciones", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Elige qué notificaciones recibir", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Spacer(Modifier.height(8.dp))

            NotifToggle("Matches", "Cuando una empresa hace match contigo", settings?.matchNotifications ?: true) {
                settingsViewModel.toggleMatchNotifications()
            }
            NotifToggle("Mensajes", "Nuevos mensajes en tus conversaciones", settings?.messageNotifications ?: true) {
                settingsViewModel.toggleMessageNotifications()
            }
            NotifToggle("Todas las notificaciones", "Activar o desactivar todo", settings?.notificationsEnabled ?: true) {
                settingsViewModel.toggleNotifications()
            }
        }
    }
}

@Composable
private fun NotifToggle(title: String, subtitle: String, checked: Boolean, onToggle: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Switch(checked = checked, onCheckedChange = { onToggle() }, colors = SwitchDefaults.colors(checkedThumbColor = BackgroundColor, checkedTrackColor = PrimaryDark))
    }
    HorizontalDivider(color = DividerColor)
}
```

- [ ] **Step 5.3: Actualizar el composable Settings en NavGraph**

Localizar `composable(Screen.Settings.route)` y reemplazar con:

```kotlin
composable(Screen.Settings.route) {
    SettingsScreen(
        authViewModel = authViewModel,
        settingsViewModel = settingsViewModel,
        onBack = { navController.popBackStack() },
        onLogout = {
            navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } }
        },
        onChangePassword = { navController.navigate(Screen.ChangePassword.route) },
        onNotificationPrefs = { navController.navigate(Screen.NotificationPrefs.route) },
        onPrivacy = { navController.navigate(Screen.PrivacySettings.route) },
        onHelp = { navController.navigate(Screen.Help.route) },
        onEditProfile = { navController.navigate(Screen.EditProfile.route) },
        onBiometric = { navController.navigate(Screen.BiometricVerification.route) },
    )
}
```

Localizar `composable(Screen.NotificationPrefs.route)` y reemplazar con:

```kotlin
composable(Screen.NotificationPrefs.route) {
    NotificationPrefsScreen(settingsViewModel = settingsViewModel, onBack = { navController.popBackStack() })
}
```

- [ ] **Step 5.4: Compilar**

```
.\gradlew.bat compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 5.5: Commit**

```
git add app/src/main/java/com/example/jovi/ui/screens/settings/
git add app/src/main/java/com/example/jovi/navigation/NavGraph.kt
git commit -m "feat: SettingsScreen y NotificationPrefs conectados a BD via SettingsViewModel"
```

---

## Task 6: ChangePasswordScreen + EditProfileScreen con persistencia real

**Files:**
- Modify: `app/src/main/java/com/example/jovi/ui/screens/settings/ChangePasswordScreen.kt`
- Modify: `app/src/main/java/com/example/jovi/ui/screens/profile/EditProfileScreen.kt`
- Modify: `app/src/main/java/com/example/jovi/navigation/NavGraph.kt`

- [ ] **Step 6.1: Reescribir ChangePasswordScreen con verificación real**

```kotlin
// app/src/main/java/com/example/jovi/ui/screens/settings/ChangePasswordScreen.kt
package com.example.jovi.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.AuthViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
) {
    var current by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }
    val user by authViewModel.currentUser.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { JoviTopBar(title = "Cambiar contraseña", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            if (error.isNotEmpty()) {
                Text(error, color = ErrorColor, style = MaterialTheme.typography.bodySmall)
            }
            if (success) {
                Text("¡Contraseña actualizada!", color = PrimaryDark, style = MaterialTheme.typography.bodySmall)
            }
            JoviTextField(value = current, onValueChange = { current = it; error = "" }, placeholder = "Contraseña actual", leadingIcon = Icons.Outlined.Lock, isPassword = true)
            JoviTextField(value = newPass, onValueChange = { newPass = it; error = "" }, placeholder = "Nueva contraseña", leadingIcon = Icons.Outlined.Lock, isPassword = true)
            JoviTextField(value = confirm, onValueChange = { confirm = it; error = "" }, placeholder = "Confirmar contraseña", leadingIcon = Icons.Outlined.Lock, isPassword = true)
            Spacer(Modifier.height(8.dp))
            JoviPrimaryButton(text = "Guardar cambios", onClick = {
                val u = user ?: return@JoviPrimaryButton
                when {
                    current != u.password -> error = "La contraseña actual es incorrecta"
                    newPass.length < 6 -> error = "La nueva contraseña debe tener al menos 6 caracteres"
                    newPass != confirm -> error = "Las contraseñas no coinciden"
                    else -> {
                        scope.launch {
                            authViewModel.updatePassword(newPass)
                            success = true
                            current = ""; newPass = ""; confirm = ""
                        }
                    }
                }
            })
        }
    }
}
```

- [ ] **Step 6.2: Agregar `updatePassword` a AuthViewModel**

```kotlin
// En AuthViewModel.kt, agregar dentro de la clase:
fun updatePassword(newPassword: String) {
    val user = _currentUser.value ?: return
    viewModelScope.launch {
        val updated = user.copy(password = newPassword)
        userRepository.update(updated)
        _currentUser.value = updated
    }
}
```

- [ ] **Step 6.3: Reescribir EditProfileScreen con guardado real**

```kotlin
// app/src/main/java/com/example/jovi/ui/screens/profile/EditProfileScreen.kt
package com.example.jovi.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit,
) {
    val user by authViewModel.currentUser.collectAsState()
    var displayName by remember(user) { mutableStateOf(user?.displayName ?: "") }
    var bio by remember(user) { mutableStateOf(user?.bio ?: "") }
    var university by remember(user) { mutableStateOf(user?.university ?: "") }
    var location by remember(user) { mutableStateOf(user?.location ?: "") }
    var headline by remember(user) { mutableStateOf(user?.headline ?: "") }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { JoviTopBar(title = "Editar perfil", onBack = onBack) },
        containerColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            JoviTextField(value = displayName, onValueChange = { displayName = it }, placeholder = "Nombre completo", leadingIcon = Icons.Outlined.Person)
            JoviTextField(value = headline, onValueChange = { headline = it }, placeholder = "Título profesional (ej. Android Developer)", leadingIcon = Icons.Outlined.Work)
            JoviTextField(value = bio, onValueChange = { bio = it }, placeholder = "Sobre mí", leadingIcon = Icons.Outlined.Info)
            JoviTextField(value = university, onValueChange = { university = it }, placeholder = "Universidad", leadingIcon = Icons.Outlined.School)
            JoviTextField(value = location, onValueChange = { location = it }, placeholder = "Ciudad, País", leadingIcon = Icons.Outlined.LocationOn)
            Spacer(Modifier.height(8.dp))
            JoviPrimaryButton(text = "Guardar cambios", onClick = {
                val u = user ?: return@JoviPrimaryButton
                scope.launch {
                    authViewModel.updateProfile(u.copy(displayName = displayName, bio = bio, university = university, location = location, headline = headline))
                    onSave()
                }
            })
        }
    }
}
```

- [ ] **Step 6.4: Agregar `updateProfile` a AuthViewModel**

```kotlin
// En AuthViewModel.kt, agregar:
fun updateProfile(updated: UserEntity) {
    viewModelScope.launch {
        userRepository.update(updated)
        _currentUser.value = updated
    }
}
```

- [ ] **Step 6.5: Actualizar composables de ChangePassword y EditProfile en NavGraph**

```kotlin
// Reemplazar composable(Screen.ChangePassword.route):
composable(Screen.ChangePassword.route) {
    ChangePasswordScreen(authViewModel = authViewModel, onBack = { navController.popBackStack() })
}

// Reemplazar composable(Screen.EditProfile.route):
composable(Screen.EditProfile.route) {
    EditProfileScreen(
        authViewModel = authViewModel,
        onBack = { navController.popBackStack() },
        onSave = { navController.popBackStack() }
    )
}
```

- [ ] **Step 6.6: Compilar**

```
.\gradlew.bat compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 6.7: Commit**

```
git add app/src/main/java/com/example/jovi/ui/screens/settings/ChangePasswordScreen.kt
git add app/src/main/java/com/example/jovi/ui/screens/profile/EditProfileScreen.kt
git add app/src/main/java/com/example/jovi/viewmodel/AuthViewModel.kt
git add app/src/main/java/com/example/jovi/navigation/NavGraph.kt
git commit -m "feat: ChangePassword y EditProfile guardan en Room"
```

---

## Task 7: Chat — ruta por conversationId + ViewModel actualizado

**Files:**
- Modify: `app/src/main/java/com/example/jovi/navigation/Routes.kt`
- Modify: `app/src/main/java/com/example/jovi/data/db/dao/MessageDao.kt`
- Modify: `app/src/main/java/com/example/jovi/data/repository/MessageRepository.kt`
- Modify: `app/src/main/java/com/example/jovi/viewmodel/ChatViewModel.kt`

- [ ] **Step 7.1: Cambiar ruta Chat de `contactName` a `conversationId`**

```kotlin
// En Routes.kt, reemplazar:
object Chat : Screen("chat/{conversationId}") {
    fun createRoute(conversationId: Long) = "chat/$conversationId"
}
```

- [ ] **Step 7.2: Agregar query de conversaciones por usuario en MessageDao**

```kotlin
// En MessageDao.kt, agregar dentro de la interfaz:
@Query("SELECT * FROM conversations WHERE userId1 = :userId OR userId2 = :userId ORDER BY lastMessageAt DESC")
fun getConversationsForUser(userId: Long): Flow<List<ConversationEntity>>

@Query("UPDATE conversations SET lastMessage = :msg, lastMessageAt = :ts, unreadCount = unreadCount + 1 WHERE id = :convId")
suspend fun updateLastMessage(convId: Long, msg: String, ts: Long)

@Query("UPDATE conversations SET unreadCount = 0 WHERE id = :convId")
suspend fun resetUnreadCount(convId: Long)
```

- [ ] **Step 7.3: Actualizar MessageRepository**

```kotlin
// app/src/main/java/com/example/jovi/data/repository/MessageRepository.kt
package com.example.jovi.data.repository

import com.example.jovi.data.db.dao.MessageDao
import com.example.jovi.data.db.entity.ConversationEntity
import com.example.jovi.data.db.entity.MessageEntity
import com.example.jovi.data.db.entity.MessageType
import kotlinx.coroutines.flow.Flow

class MessageRepository(private val messageDao: MessageDao) {
    fun getAllConversations(): Flow<List<ConversationEntity>> = messageDao.getAllConversations()
    fun getConversationsForUser(userId: Long): Flow<List<ConversationEntity>> = messageDao.getConversationsForUser(userId)
    fun getMessages(conversationId: Long): Flow<List<MessageEntity>> = messageDao.getMessages(conversationId)

    suspend fun sendMessage(message: MessageEntity): Long {
        val id = messageDao.insertMessage(message)
        messageDao.updateLastMessage(message.conversationId, message.content, message.timestamp)
        return id
    }

    suspend fun sendDocument(conversationId: Long, senderId: Long, senderName: String, fileName: String, fileSize: String): Long {
        val msg = MessageEntity(
            conversationId = conversationId,
            senderId = senderId,
            senderName = senderName,
            content = fileName,
            type = MessageType.FILE,
            fileName = fileName,
            fileSize = fileSize,
        )
        val id = messageDao.insertMessage(msg)
        messageDao.updateLastMessage(conversationId, "📎 $fileName", msg.timestamp)
        return id
    }

    suspend fun markAllRead(conversationId: Long) {
        messageDao.markAllRead(conversationId)
        messageDao.resetUnreadCount(conversationId)
    }
}
```

- [ ] **Step 7.4: Actualizar ChatViewModel**

```kotlin
// app/src/main/java/com/example/jovi/viewmodel/ChatViewModel.kt
package com.example.jovi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jovi.data.db.entity.ConversationEntity
import com.example.jovi.data.db.entity.MessageEntity
import com.example.jovi.data.db.entity.MessageType
import com.example.jovi.data.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val messageRepository: MessageRepository) : ViewModel() {

    private val _conversations = MutableStateFlow<List<ConversationEntity>>(emptyList())
    val conversations: StateFlow<List<ConversationEntity>> = _conversations.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val messages: StateFlow<List<MessageEntity>> = _messages.asStateFlow()

    fun loadConversationsForUser(userId: Long) {
        viewModelScope.launch {
            messageRepository.getConversationsForUser(userId).collect { _conversations.value = it }
        }
    }

    fun loadMessages(conversationId: Long) {
        viewModelScope.launch {
            messageRepository.getMessages(conversationId).collect { _messages.value = it }
        }
    }

    fun sendMessage(conversationId: Long, senderId: Long, senderName: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            messageRepository.sendMessage(
                MessageEntity(conversationId = conversationId, senderId = senderId, senderName = senderName, content = text, type = MessageType.TEXT)
            )
        }
    }

    fun sendDocument(conversationId: Long, senderId: Long, senderName: String, fileName: String, fileSize: String) {
        viewModelScope.launch {
            messageRepository.sendDocument(conversationId, senderId, senderName, fileName, fileSize)
        }
    }

    fun markAsRead(conversationId: Long) {
        viewModelScope.launch { messageRepository.markAllRead(conversationId) }
    }

    class Factory(private val repository: MessageRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(repository) as T
        }
    }
}
```

- [ ] **Step 7.5: Compilar**

```
.\gradlew.bat compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 7.6: Commit**

```
git add app/src/main/java/com/example/jovi/navigation/Routes.kt
git add app/src/main/java/com/example/jovi/data/db/dao/MessageDao.kt
git add app/src/main/java/com/example/jovi/data/repository/MessageRepository.kt
git add app/src/main/java/com/example/jovi/viewmodel/ChatViewModel.kt
git commit -m "feat: chat por conversationId, filtro por usuario, sendDocument"
```

---

## Task 8: ChatListScreen y ChatScreen completamente conectados

**Files:**
- Modify: `app/src/main/java/com/example/jovi/ui/screens/chat/ChatListScreen.kt`
- Modify: `app/src/main/java/com/example/jovi/ui/screens/chat/ChatScreen.kt`
- Modify: `app/src/main/java/com/example/jovi/navigation/NavGraph.kt`

- [ ] **Step 8.1: Actualizar ChatListScreen para navegar por conversationId**

En `ChatListScreen.kt`, el `onClick` del `ConversationRow` actualmente llama `onOpenChat(otherName)`. Cambiar:

```kotlin
// En ChatListScreen, cambiar la firma:
@Composable
fun ChatListScreen(
    conversations: List<ConversationEntity>,
    currentUserId: Long,
    onOpenChat: (Long) -> Unit,   // Long = conversationId
    onBack: () -> Unit,
)

// Y en ConversationRow, cambiar onClick:
onClick = { onOpenChat(conv.id) }
```

- [ ] **Step 8.2: Reescribir ChatScreen completamente conectado a Room**

```kotlin
// app/src/main/java/com/example/jovi/ui/screens/chat/ChatScreen.kt
package com.example.jovi.ui.screens.chat

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jovi.data.db.entity.MessageEntity
import com.example.jovi.data.db.entity.MessageStatus
import com.example.jovi.data.db.entity.MessageType
import com.example.jovi.ui.components.*
import com.example.jovi.ui.theme.*
import com.example.jovi.viewmodel.ChatViewModel
import com.example.jovi.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    conversationId: Long,
    contactName: String,
    contactInitials: String,
    chatViewModel: ChatViewModel,
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onScheduleInterview: () -> Unit = {},
    onVideoCall: () -> Unit = {},
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val messages by chatViewModel.messages.collectAsState()
    var messageText by remember { mutableStateOf("") }
    var showDocDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(conversationId) {
        chatViewModel.loadMessages(conversationId)
        chatViewModel.markAsRead(conversationId)
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ProfileAvatar(initials = contactInitials, size = 38.dp)
                        Column {
                            Text(contactName, style = MaterialTheme.typography.titleMedium)
                            Text("EN LÍNEA", style = MaterialTheme.typography.labelSmall, color = PrimaryDark)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Atrás", tint = TextPrimary) }
                },
                actions = {
                    IconButton(onClick = onVideoCall) { Icon(Icons.Outlined.Call, contentDescription = "Llamar", tint = TextPrimary) }
                    IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, contentDescription = "Más", tint = TextPrimary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        },
        bottomBar = {
            Surface(shadowElevation = 4.dp, color = BackgroundColor) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { showDocDialog = true }) {
                        Icon(Icons.Outlined.AttachFile, contentDescription = "Adjuntar", tint = TextSecondary)
                    }
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Mensaje...", color = TextSecondary) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TertiaryColor, unfocusedBorderColor = TertiaryColor),
                        singleLine = true,
                    )
                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(PrimaryColor), contentAlignment = Alignment.Center) {
                        IconButton(onClick = {
                            val u = currentUser ?: return@IconButton
                            chatViewModel.sendMessage(conversationId, u.id, u.displayName, messageText)
                            messageText = ""
                        }) {
                            Icon(Icons.Default.Send, contentDescription = "Enviar", tint = SecondaryColor, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        },
        containerColor = BackgroundColor,
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Surface(shape = RoundedCornerShape(50), color = SurfaceColor) {
                        Text("HOY", modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                }
            }
            items(messages, key = { it.id }) { msg ->
                val isMe = msg.senderId == currentUser?.id
                when (msg.type) {
                    MessageType.FILE -> FileMessageBubble(msg, isMe, contactInitials, currentUser?.avatarInitials ?: "?")
                    else -> TextMessageBubble(msg, isMe, contactInitials, currentUser?.avatarInitials ?: "?")
                }
            }
        }
    }

    if (showDocDialog) {
        DocumentPickerDialog(
            onDismiss = { showDocDialog = false },
            onSend = { fileName, fileSize ->
                val u = currentUser ?: return@DocumentPickerDialog
                chatViewModel.sendDocument(conversationId, u.id, u.displayName, fileName, fileSize)
                showDocDialog = false
            }
        )
    }
}

@Composable
private fun TextMessageBubble(msg: MessageEntity, isMe: Boolean, contactInitials: String, myInitials: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMe) ProfileAvatar(initials = contactInitials, size = 28.dp, modifier = Modifier.padding(end = 6.dp))
        Column(horizontalAlignment = if (isMe) Alignment.End else Alignment.Start, verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Surface(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = if (isMe) 16.dp else 4.dp, bottomEnd = if (isMe) 4.dp else 16.dp),
                color = if (isMe) PrimaryColor else SurfaceColor
            ) {
                Text(msg.content, modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp), style = MaterialTheme.typography.bodyMedium, color = if (isMe) SecondaryColor else TextPrimary)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(formatMsgTime(msg.timestamp), style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                if (isMe) StatusIcon(msg.status)
            }
        }
        if (isMe) ProfileAvatar(initials = myInitials, size = 28.dp, modifier = Modifier.padding(start = 6.dp))
    }
}

@Composable
private fun FileMessageBubble(msg: MessageEntity, isMe: Boolean, contactInitials: String, myInitials: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMe) ProfileAvatar(initials = contactInitials, size = 28.dp, modifier = Modifier.padding(end = 6.dp))
        Surface(shape = RoundedCornerShape(12.dp), color = if (isMe) PrimaryColor else SurfaceColor) {
            Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(8.dp), color = ErrorColor.copy(0.15f)) {
                    Icon(Icons.Outlined.PictureAsPdf, contentDescription = null, tint = ErrorColor, modifier = Modifier.padding(8.dp).size(20.dp))
                }
                Column {
                    Text(msg.fileName ?: msg.content, style = MaterialTheme.typography.labelMedium, color = if (isMe) SecondaryColor else TextPrimary)
                    Text(msg.fileSize ?: "", style = MaterialTheme.typography.labelSmall, color = if (isMe) SecondaryColor.copy(0.7f) else TextSecondary)
                }
                Icon(Icons.Outlined.Download, contentDescription = null, tint = if (isMe) SecondaryColor else TextSecondary)
            }
        }
        if (isMe) ProfileAvatar(initials = myInitials, size = 28.dp, modifier = Modifier.padding(start = 6.dp))
    }
}

@Composable
private fun StatusIcon(status: MessageStatus) {
    when (status) {
        MessageStatus.SENT -> Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(12.dp), tint = TextSecondary)
        MessageStatus.DELIVERED -> Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(12.dp), tint = TextSecondary)
        MessageStatus.READ -> Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(12.dp), tint = PrimaryDark)
    }
}

@Composable
private fun DocumentPickerDialog(onDismiss: () -> Unit, onSend: (String, String) -> Unit) {
    val docs = listOf("CV_Actualizado.pdf" to "1.2 MB", "Portafolio_2025.pdf" to "3.4 MB", "Carta_Presentacion.docx" to "245 KB", "Transcripcion_Oficial.pdf" to "890 KB")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar documento") },
        text = {
            Column {
                docs.forEach { (name, size) ->
                    Surface(onClick = { onSend(name, size) }, color = BackgroundColor) {
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.PictureAsPdf, contentDescription = null, tint = ErrorColor, modifier = Modifier.size(20.dp))
                            Column {
                                Text(name, style = MaterialTheme.typography.bodyMedium)
                                Text(size, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            }
                        }
                    }
                    HorizontalDivider(color = DividerColor)
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

private fun formatMsgTime(millis: Long): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(millis))
```

- [ ] **Step 8.3: Actualizar NavGraph para usar rutas numéricas y pasar parámetros correctos al chat**

Localizar el bloque `// --- CHAT LIST ---` y `// --- CHAT ---` en NavGraph y reemplazar:

```kotlin
// --- CHAT LIST ---
composable(Screen.ChatList.route) {
    val currentUser by authViewModel.currentUser.collectAsState()
    LaunchedEffect(currentUser?.id) {
        currentUser?.id?.let { chatViewModel.loadConversationsForUser(it) }
    }
    val conversations by chatViewModel.conversations.collectAsState()
    ChatListScreen(
        conversations = conversations,
        currentUserId = currentUser?.id ?: -1L,
        onOpenChat = { convId -> navController.navigate(Screen.Chat.createRoute(convId)) },
        onBack = { navController.popBackStack() },
    )
}

// --- CHAT ---
composable(Screen.Chat.route) { backStackEntry ->
    val conversationId = backStackEntry.arguments?.getString("conversationId")?.toLongOrNull() ?: return@composable
    val conversations by chatViewModel.conversations.collectAsState()
    val conv = conversations.find { it.id == conversationId }
    val currentUser by authViewModel.currentUser.collectAsState()
    val contactName = if (conv?.userId1 == currentUser?.id) conv?.user2Name ?: "" else conv?.user1Name ?: ""
    val contactInitials = if (conv?.userId1 == currentUser?.id) conv?.user2Initials ?: "?" else conv?.user1Initials ?: "?"
    ChatScreen(
        conversationId = conversationId,
        contactName = contactName,
        contactInitials = contactInitials,
        chatViewModel = chatViewModel,
        authViewModel = authViewModel,
        onBack = { navController.popBackStack() },
        onScheduleInterview = { navController.navigate(Screen.MyAppointments.route) },
        onVideoCall = { navController.navigate(Screen.VideoInterview.route) },
    )
}
```

También actualizar cualquier `Screen.Chat.createRoute("...")` hardcodeado en MatchCelebrationScreen y otros lugares que usen nombre de contacto — cambiarlos por un conversationId real (usar 1L como fallback para demo):

```kotlin
// En MatchCelebrationScreen:
onSendMessage = { navController.navigate(Screen.Chat.createRoute(1L)) },
```

- [ ] **Step 8.4: Compilar**

```
.\gradlew.bat compileDebugKotlin
```
Expected: BUILD SUCCESSFUL. Si hay errores de imports o parámetros en otros lugares que usen `Screen.Chat.createRoute(String)`, cambiarlos a `createRoute(Long)`.

- [ ] **Step 8.5: Commit**

```
git add app/src/main/java/com/example/jovi/ui/screens/chat/
git add app/src/main/java/com/example/jovi/navigation/
git commit -m "feat: ChatScreen y ChatListScreen completamente conectados a Room"
```

---

## Task 9: Perfil dinámico desde usuario activo

**Files:**
- Modify: `app/src/main/java/com/example/jovi/ui/screens/profile/PublicProfileScreen.kt`
- Modify: `app/src/main/java/com/example/jovi/viewmodel/ProfileViewModel.kt`
- Modify: `app/src/main/java/com/example/jovi/navigation/NavGraph.kt`

- [ ] **Step 9.1: Actualizar ProfileViewModel para cargar usuario activo y sus posts**

```kotlin
// app/src/main/java/com/example/jovi/viewmodel/ProfileViewModel.kt
package com.example.jovi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jovi.data.db.entity.PostEntity
import com.example.jovi.data.db.entity.UserEntity
import com.example.jovi.data.repository.PostRepository
import com.example.jovi.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user.asStateFlow()

    private val _posts = MutableStateFlow<List<PostEntity>>(emptyList())
    val posts: StateFlow<List<PostEntity>> = _posts.asStateFlow()

    fun loadUser(userId: Long) {
        viewModelScope.launch {
            userRepository.getUserById(userId).collect { _user.value = it }
        }
        viewModelScope.launch {
            postRepository.getPostsByAuthor(userId).collect { _posts.value = it }
        }
    }

    class Factory(
        private val userRepository: UserRepository,
        private val postRepository: PostRepository,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository, postRepository) as T
        }
    }
}
```

- [ ] **Step 9.2: Agregar `getPostsByAuthor` a PostDao y PostRepository**

En `PostDao.kt`, agregar:
```kotlin
@Query("SELECT * FROM posts WHERE authorId = :authorId ORDER BY timestamp DESC")
fun getPostsByAuthor(authorId: Long): Flow<List<PostEntity>>
```

En `PostRepository.kt`, agregar:
```kotlin
fun getPostsByAuthor(authorId: Long): Flow<List<PostEntity>> = postDao.getPostsByAuthor(authorId)
```

- [ ] **Step 9.3: Actualizar composable PublicProfile en NavGraph para usar currentUser**

```kotlin
composable(Screen.PublicProfile.route) {
    val currentUser by authViewModel.currentUser.collectAsState()
    LaunchedEffect(currentUser?.id) {
        currentUser?.id?.let { profileViewModel.loadUser(it) }
    }
    val profileUser by profileViewModel.user.collectAsState()
    val profilePosts by profileViewModel.posts.collectAsState()
    val displayUser = profileUser ?: currentUser
    PublicProfileScreen(
        user = displayUser,
        posts = profilePosts,
        onBack = { navController.popBackStack() },
        onShare = {},
        onSendMatchRequest = { navController.navigate(Screen.Chat.createRoute(1L)) },
        onAddExperience = { navController.navigate(Screen.AddWorkExperience.route) },
        onEditProfile = { navController.navigate(Screen.EditProfile.route) },
        onSettings = { navController.navigate(Screen.Settings.route) },
        onSavedPosts = { navController.navigate(Screen.SavedPosts.route) },
        onMyApplications = { navController.navigate(Screen.MyApplications.route) },
        onAchievements = { navController.navigate(Screen.Achievements.route) },
        onStreak = { navController.navigate(Screen.DailyStreak.route) },
    )
}
```

- [ ] **Step 9.4: Actualizar PublicProfileScreen para aceptar `user: UserEntity?` y `posts: List<PostEntity>`**

En `PublicProfileScreen.kt`, cambiar la firma para recibir datos dinámicos:

```kotlin
@Composable
fun PublicProfileScreen(
    user: UserEntity?,
    posts: List<PostEntity>,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onSendMatchRequest: () -> Unit,
    onAddExperience: () -> Unit,
    onEditProfile: () -> Unit,
    onSettings: () -> Unit,
    onSavedPosts: () -> Unit,
    onMyApplications: () -> Unit,
    onAchievements: () -> Unit,
    onStreak: () -> Unit,
)
```

Y usar `user?.displayName ?: "Usuario"`, `user?.bio ?: ""`, `user?.avatarInitials ?: "?"`, `user?.followerCount ?: 0`, `user?.followingCount ?: 0`, `user?.streakDays ?: 0` en lugar de datos estáticos dentro del composable.

- [ ] **Step 9.5: Compilar**

```
.\gradlew.bat compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 9.6: Commit**

```
git add app/src/main/java/com/example/jovi/viewmodel/ProfileViewModel.kt
git add app/src/main/java/com/example/jovi/ui/screens/profile/PublicProfileScreen.kt
git add app/src/main/java/com/example/jovi/data/
git add app/src/main/java/com/example/jovi/navigation/NavGraph.kt
git commit -m "feat: perfil dinamico desde usuario activo, posts desde Room"
```

---

## Task 10: Discovery y Feed con nombre de usuario dinámico

**Files:**
- Modify: `app/src/main/java/com/example/jovi/ui/screens/discovery/JobDiscoveryScreen.kt`
- Modify: `app/src/main/java/com/example/jovi/ui/screens/discovery/InternshipDiscoveryScreen.kt`
- Modify: `app/src/main/java/com/example/jovi/navigation/NavGraph.kt`

- [ ] **Step 10.1: Pasar `currentUserName` real a las pantallas de descubrimiento en NavGraph**

```kotlin
// Localizar composable(Screen.JobDiscovery.route) y reemplazar:
composable(Screen.JobDiscovery.route) {
    val currentUser by authViewModel.currentUser.collectAsState()
    JobDiscoveryScreen(
        onMatch = { navController.navigate(Screen.MatchCelebration.route) },
        onVacancyDetail = { navController.navigate(Screen.VacancyDetail.route) },
        currentUserName = currentUser?.displayName ?: "Tú",
    )
}

// Hacer lo mismo para InternshipDiscovery:
composable(Screen.InternshipDiscovery.route) {
    val currentUser by authViewModel.currentUser.collectAsState()
    InternshipDiscoveryScreen(
        onMatch = { navController.navigate(Screen.MatchCelebration.route) },
        onVacancyDetail = { navController.navigate(Screen.VacancyDetail.route) },
        currentUserName = currentUser?.displayName ?: "Tú",
    )
}
```

- [ ] **Step 10.2: Verificar y corregir FeedViewModel — debe usar Flow activo de Room**

En `FeedViewModel.kt`, verificar que el init colecta el flow (ya debería estar). Si no, agregar:

```kotlin
// En FeedViewModel init:
viewModelScope.launch {
    postRepository.getAllPosts().collect { allPosts ->
        _threads.value = allPosts.filter { it.type == PostType.THREAD }
        _reels.value = allPosts.filter { it.type == PostType.REEL }
    }
}
```

- [ ] **Step 10.3: Actualizar NotificationsScreen en NavGraph para filtrar por userId**

```kotlin
// En NavGraph, localizar composable(Screen.Notifications.route):
composable(Screen.Notifications.route) {
    val currentUser by authViewModel.currentUser.collectAsState()
    LaunchedEffect(currentUser?.id) {
        currentUser?.id?.let { notifViewModel.loadForUser(it) }
    }
    val notifications by notifViewModel.notifications.collectAsState()
    NotificationsScreen(
        notifications = notifications,
        onMarkRead = { notifViewModel.markRead(it) },
        onMarkAllRead = { notifViewModel.markAllRead() },
        onBack = { navController.popBackStack() },
    )
}
```

Agregar `loadForUser(userId: Long)` a `NotificationViewModel.kt`:
```kotlin
fun loadForUser(userId: Long) {
    viewModelScope.launch {
        notificationRepository.getNotificationsForUser(userId).collect { _notifications.value = it }
    }
}
```

Agregar `getNotificationsForUser(userId: Long)` a `NotificationDao.kt`:
```kotlin
@Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
fun getNotificationsForUser(userId: Long): Flow<List<NotificationEntity>>
```

Agregar a `NotificationRepository.kt`:
```kotlin
fun getNotificationsForUser(userId: Long): Flow<List<NotificationEntity>> = notificationDao.getNotificationsForUser(userId)
```

- [ ] **Step 10.4: Compilar**

```
.\gradlew.bat compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 10.5: Commit**

```
git add app/src/main/java/com/example/jovi/ui/screens/discovery/
git add app/src/main/java/com/example/jovi/viewmodel/
git add app/src/main/java/com/example/jovi/data/
git add app/src/main/java/com/example/jovi/navigation/NavGraph.kt
git commit -m "feat: discovery y notificaciones con datos del usuario activo"
```

---

## Task 11: Build final, push y PR

- [ ] **Step 11.1: Build completo**

```
.\gradlew.bat assembleDebug
```
Expected: BUILD SUCCESSFUL. Si hay errores de compilación, corregirlos antes de continuar.

- [ ] **Step 11.2: Push y PR**

```powershell
git push origin cambios-1
& "C:\Program Files\GitHub CLI\gh.exe" pr create `
    --title "feat: auth real, chat completo, dark mode, datos dinamicos desde Room" `
    --body "Implementa auth con sesion persistente, 5 usuarios seed con contraseñas, chat Room completo con archivos y estados de entrega, dark mode funcional, settings conectados a BD, perfil dinamico." `
    --base main --repo alex574r/JOVI
```

- [ ] **Step 11.3: Merge**

```powershell
& "C:\Program Files\GitHub CLI\gh.exe" pr merge <PR_NUMBER> --merge --repo alex574r/JOVI
```
