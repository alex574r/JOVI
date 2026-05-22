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
    @TypeConverter fun fromNotificationType(v: NotificationType): String = v.name
    @TypeConverter fun toNotificationType(v: String): NotificationType = NotificationType.valueOf(v)
    @TypeConverter fun fromApplicationStatus(v: ApplicationStatus): String = v.name
    @TypeConverter fun toApplicationStatus(v: String): ApplicationStatus = ApplicationStatus.valueOf(v)
}

@Database(
    entities = [
        UserEntity::class,
        PostEntity::class,
        MessageEntity::class,
        ConversationEntity::class,
        NotificationEntity::class,
        UserSettingsEntity::class,
        JobApplicationEntity::class,
    ],
    version = 1,
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
            val users = listOf(
                UserEntity(id = 1, username = "carlos_dev", displayName = "Carlos Mendoza", email = "carlos@example.com", avatarInitials = "CM", bio = "Software developer apasionado por la tecnologia. Buscando practicas en empresas innovadoras.", accountType = AccountType.STUDENT, isVerified = true, streakDays = 14, followerCount = 342, followingCount = 128, university = "UNAM", location = "CDMX"),
                UserEntity(id = 2, username = "ana_design", displayName = "Ana Garcia", email = "ana@example.com", avatarInitials = "AG", bio = "UX/UI Designer con enfoque en productos digitales. Portfolio en construccion.", accountType = AccountType.STUDENT, isVerified = false, streakDays = 7, followerCount = 215, followingCount = 89, university = "Tec de Monterrey", location = "Monterrey"),
                UserEntity(id = 3, username = "luis_data", displayName = "Luis Torres", email = "luis@example.com", avatarInitials = "LT", bio = "Data Scientist. Machine Learning y analisis de datos para decision making.", accountType = AccountType.STUDENT, isVerified = false, streakDays = 3, followerCount = 178, followingCount = 201, university = "IPN", location = "CDMX"),
                UserEntity(id = 4, username = "stanford_careers", displayName = "Stanford University", email = "careers@stanford.edu", avatarInitials = "SU", bio = "Conectando talento academico con oportunidades de impacto. Oficina de carreras.", accountType = AccountType.RECRUITER, isVerified = true, streakDays = 0, followerCount = 12400, followingCount = 45, company = "Stanford University", location = "California"),
                UserEntity(id = 5, username = "innovatech_hr", displayName = "Innovatech Corp", email = "hr@innovatech.com", avatarInitials = "IC", bio = "Empresa de tecnologia buscando talento joven. Practicas con impacto real.", accountType = AccountType.RECRUITER, isVerified = true, streakDays = 0, followerCount = 5800, followingCount = 23, company = "Innovatech Corp", location = "CDMX"),
            )
            db.userDao().insertAll(users)

            val posts = listOf(
                PostEntity(authorId = 1, authorName = "Carlos Mendoza", authorInitials = "CM", authorUsername = "carlos_dev", type = PostType.THREAD, content = "Acabo de terminar mi primer proyecto con Kotlin Multiplatform. La curva de aprendizaje es real pero los resultados valen la pena. Comparto mis notas y errores mas comunes.", likeCount = 47, commentCount = 12, tags = "kotlin,android,mobile", timestamp = System.currentTimeMillis() - 3600000),
                PostEntity(authorId = 2, authorName = "Ana Garcia", authorInitials = "AG", authorUsername = "ana_design", type = PostType.THREAD, content = "Tip de UX: antes de disenar cualquier flujo, mapea los estados vacios. Son los momentos mas criticos de una app y los mas olvidados en el diseno inicial.", likeCount = 93, commentCount = 28, tags = "ux,design,tips", timestamp = System.currentTimeMillis() - 7200000),
                PostEntity(authorId = 1, authorName = "Carlos Mendoza", authorInitials = "CM", authorUsername = "carlos_dev", type = PostType.REEL, content = "Tutorial: como implementar SwipeCard en Jetpack Compose en menos de 50 lineas.", likeCount = 156, commentCount = 34, tags = "compose,android,tutorial", timestamp = System.currentTimeMillis() - 86400000),
                PostEntity(authorId = 3, authorName = "Luis Torres", authorInitials = "LT", authorUsername = "luis_data", type = PostType.THREAD, content = "Resumen de las tendencias en Data Science para 2025: LLMs especializados, edge AI, y la vuelta a modelos interpretables. El hype de los LLMs gigantes esta bajando.", likeCount = 71, commentCount = 19, tags = "datascience,ml,ai", timestamp = System.currentTimeMillis() - 172800000),
                PostEntity(authorId = 2, authorName = "Ana Garcia", authorInitials = "AG", authorUsername = "ana_design", type = PostType.REEL, content = "Redisene este landing page en 2 horas. Antes vs despues. Los pequenos detalles de espaciado marcan toda la diferencia.", likeCount = 204, commentCount = 41, tags = "ux,ui,redesign", timestamp = System.currentTimeMillis() - 259200000),
                PostEntity(authorId = 4, authorName = "Stanford University", authorInitials = "SU", authorUsername = "stanford_careers", type = PostType.THREAD, content = "Abrimos convocatoria para el programa de investigacion de verano 2025. Plazas limitadas para estudiantes de ingenieria y ciencias computacionales.", likeCount = 312, commentCount = 87, tags = "oportunidad,investigacion,stanford", timestamp = System.currentTimeMillis() - 43200000),
                PostEntity(authorId = 5, authorName = "Innovatech Corp", authorInitials = "IC", authorUsername = "innovatech_hr", type = PostType.THREAD, content = "Estamos buscando 3 practicantes de desarrollo mobile. Stack: Kotlin + Compose. Modalidad hibrida. Aplica directamente en la app.", likeCount = 128, commentCount = 53, tags = "empleo,practicas,mobile", timestamp = System.currentTimeMillis() - 21600000),
            )
            db.postDao().insertAll(posts)

            val convs = listOf(
                ConversationEntity(id = 1, userId1 = 1, userId2 = 5, user1Name = "Carlos Mendoza", user2Name = "Innovatech Corp", user1Initials = "CM", user2Initials = "IC", lastMessage = "Nos interesa tu perfil para la posicion de mobile developer", lastMessageAt = System.currentTimeMillis() - 1800000, unreadCount = 2),
                ConversationEntity(id = 2, userId1 = 2, userId2 = 4, user1Name = "Ana Garcia", user2Name = "Stanford University", user1Initials = "AG", user2Initials = "SU", lastMessage = "Tu perfil ha sido seleccionado para la siguiente fase", lastMessageAt = System.currentTimeMillis() - 3600000, unreadCount = 1),
            )
            db.messageDao().insertAllConversations(convs)

            val messages = listOf(
                MessageEntity(conversationId = 1, senderId = 5, senderName = "Innovatech Corp", content = "Hola Carlos, vimos tu perfil en Jovi y nos parece muy interesante."),
                MessageEntity(conversationId = 1, senderId = 1, senderName = "Carlos Mendoza", content = "Muchas gracias! Estoy muy interesado en conocer mas sobre la posicion."),
                MessageEntity(conversationId = 1, senderId = 5, senderName = "Innovatech Corp", content = "Nos interesa tu perfil para la posicion de mobile developer", isRead = false),
            )
            db.messageDao().insertAllMessages(messages)

            val notifications = listOf(
                NotificationEntity(userId = 1, type = NotificationType.MATCH, title = "Nuevo Match", body = "Innovatech Corp hizo match contigo para Mobile Developer", senderInitials = "IC", isRead = false),
                NotificationEntity(userId = 1, type = NotificationType.LIKE, title = "A alguien le gusto tu post", body = "Ana Garcia dio like a tu publicacion sobre Kotlin", senderInitials = "AG", isRead = false),
                NotificationEntity(userId = 1, type = NotificationType.MESSAGE, title = "Nuevo mensaje", body = "Tienes un mensaje de Innovatech Corp", senderInitials = "IC", isRead = true),
                NotificationEntity(userId = 1, type = NotificationType.FOLLOW, title = "Nuevo seguidor", body = "Luis Torres ahora te sigue", senderInitials = "LT", isRead = true),
            )
            db.notificationDao().insertAll(notifications)

            db.settingsDao().upsert(UserSettingsEntity(userId = 1))
        }
    }
}
