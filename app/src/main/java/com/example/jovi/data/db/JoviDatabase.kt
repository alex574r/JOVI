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
                PostEntity(authorId = 5, authorName = "Innovatech Corp", authorInitials = "IC", authorUsername = "innovatech_hr", type = PostType.THREAD, content = "Abrimos 3 posiciones de practicas en desarrollo mobile. Stack: Kotlin + Compose. Modalidad hibrida.", likeCount = 128, commentCount = 53, tags = "empleo,practicas,mobile", timestamp = now - 21_600_000),
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
                MessageEntity(conversationId = 1, senderId = 5, senderName = "Innovatech Corp", content = "Tenemos una posicion de Mobile Developer con Kotlin y Compose.", status = MessageStatus.DELIVERED, isRead = false, timestamp = now - 2_000_000),
                MessageEntity(conversationId = 1, senderId = 5, senderName = "Innovatech Corp", content = "Job_Description_Mobile.pdf", type = MessageType.FILE, fileName = "Job_Description_Mobile.pdf", fileSize = "345 KB", status = MessageStatus.DELIVERED, isRead = false, timestamp = now - 1_900_000),
                MessageEntity(conversationId = 1, senderId = 5, senderName = "Innovatech Corp", content = "Nos interesa tu perfil para mobile developer", status = MessageStatus.SENT, isRead = false, timestamp = now - 1_800_000),
                MessageEntity(conversationId = 2, senderId = 4, senderName = "Stanford University", content = "Hola Ana, revisamos tu portafolio y quedamos impresionados.", status = MessageStatus.READ, isRead = true, timestamp = now - 5_400_000),
                MessageEntity(conversationId = 2, senderId = 2, senderName = "Ana Garcia", content = "Muchas gracias! Es un honor ser considerada.", status = MessageStatus.READ, isRead = true, timestamp = now - 4_500_000),
                MessageEntity(conversationId = 2, senderId = 4, senderName = "Stanford University", content = "Summer_Program_2025.pdf", type = MessageType.FILE, fileName = "Summer_Program_2025.pdf", fileSize = "1.2 MB", status = MessageStatus.DELIVERED, isRead = false, timestamp = now - 3_900_000),
                MessageEntity(conversationId = 2, senderId = 4, senderName = "Stanford University", content = "Tu perfil fue seleccionado para la siguiente fase", status = MessageStatus.SENT, isRead = false, timestamp = now - 3_600_000),
                MessageEntity(conversationId = 3, senderId = 4, senderName = "Stanford University", content = "Hola Carlos, gracias por tu interes en nuestro programa.", status = MessageStatus.READ, isRead = true, timestamp = now - 90_000_000),
                MessageEntity(conversationId = 3, senderId = 1, senderName = "Carlos Mendoza", content = "Gracias por considerarme, seria una gran oportunidad.", status = MessageStatus.READ, isRead = true, timestamp = now - 88_000_000),
                MessageEntity(conversationId = 3, senderId = 1, senderName = "Carlos Mendoza", content = "Gracias por tu interes en el programa", status = MessageStatus.READ, isRead = true, timestamp = now - 86_400_000),
            )
            db.messageDao().insertAllMessages(messages)

            val notifications = listOf(
                NotificationEntity(userId = 1, type = NotificationType.MATCH, title = "Nuevo Match", body = "Innovatech Corp hizo match contigo para Mobile Developer", senderInitials = "IC", isRead = false),
                NotificationEntity(userId = 1, type = NotificationType.LIKE, title = "Le gusto tu post", body = "Ana Garcia dio like a tu publicacion sobre Kotlin", senderInitials = "AG", isRead = false),
                NotificationEntity(userId = 1, type = NotificationType.MESSAGE, title = "Nuevo mensaje", body = "Innovatech Corp: Nos interesa tu perfil", senderInitials = "IC", isRead = true),
                NotificationEntity(userId = 2, type = NotificationType.MATCH, title = "Nuevo Match", body = "Stanford University hizo match con tu perfil", senderInitials = "SU", isRead = false),
                NotificationEntity(userId = 2, type = NotificationType.MESSAGE, title = "Nuevo mensaje", body = "Stanford: Tu perfil fue seleccionado", senderInitials = "SU", isRead = false),
                NotificationEntity(userId = 3, type = NotificationType.FOLLOW, title = "Nuevo seguidor", body = "Carlos Mendoza ahora te sigue", senderInitials = "CM", isRead = true),
            )
            db.notificationDao().insertAll(notifications)

            users.forEach { db.settingsDao().upsert(UserSettingsEntity(userId = it.id)) }
        }
    }
}
