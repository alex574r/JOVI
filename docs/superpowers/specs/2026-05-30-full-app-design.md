# Jovi — Full App Implementation Spec
**Date:** 2026-05-30  
**Status:** Approved

## Overview
Conectar toda la infraestructura existente (Room DB, ViewModels, 64 pantallas, 48 rutas) reemplazando datos estáticos con datos reales, implementar autenticación funcional, chat completo, settings con dark mode y biometría.

La BD ya tiene: 7 entidades, 5 DAOs, 4 repositorios, seed con 5 usuarios. Lo que falta es conectarlo todo.

---

## Sub-proyecto A — Autenticación + Usuarios seed

### Cambios a entidades
- `UserEntity`: agregar `password: String = ""` 
- Seed actualizado: contraseñas `carlos123`, `ana123`, `luis123`, `stanford123`, `innovatech123`
- `UserSettingsEntity`: agregar seed para los 5 usuarios

### DAO / Repository
- `UserDao.getUserByEmailAndPassword(email, password)`: query directo
- `UserRepository.login(email, password)`: llama al DAO

### Sesión persistente
- SharedPreferences key `current_user_id` (Long, -1 si no hay sesión)
- `JoviNavGraph`: leer al iniciar. Si `current_user_id >= 0` → startDestination = `job_discovery` (saltando splash/onboarding/login)
- `AuthViewModel.login()`: guardar userId en SharedPreferences al éxito
- `AuthViewModel.logout()`: limpiar SharedPreferences, navegar a Login

### LoginScreen
- Campos email + contraseña reales
- Validación: mostrar error específico ("Email no encontrado" / "Contraseña incorrecta")
- Botón "Ingresar como demo" → login con carlos@example.com / carlos123

### Usuarios seed (ya existen, agregar password)
| id | email | password | tipo |
|----|-------|----------|------|
| 1 | carlos@example.com | carlos123 | STUDENT |
| 2 | ana@example.com | ana123 | STUDENT |
| 3 | luis@example.com | luis123 | STUDENT |
| 4 | careers@stanford.edu | stanford123 | RECRUITER |
| 5 | hr@innovatech.com | innovatech123 | RECRUITER |

---

## Sub-proyecto D — Settings ampliado

### Dark mode
- `SettingsViewModel`: expone `settings: StateFlow<UserSettingsEntity?>` y `toggleDarkMode()`
- `MainActivity`: colectar darkMode de SettingsViewModel, pasarlo a `JoviTheme(darkTheme = darkMode)`
- `JoviTheme`: ya tiene soporte Material3 dark — solo necesita el parámetro externo

### Biometría
- Agregar dependencia `androidx.biometric:biometric:1.2.0-alpha05`
- `BiometricVerificationScreen`: funcional con `BiometricPrompt` + `BiometricManager`
- Settings toggle: habilitar/deshabilitar biometría (guardado en `UserSettingsEntity`)
- En Login: si biometría habilitada → ofrecer autenticación biométrica como alternativa

### SettingsScreen actualizado
Opciones:
1. Dark mode — toggle conectado a `SettingsViewModel`
2. Biometría — toggle + navegación a `BiometricVerification`
3. Editar perfil → `EditProfile`
4. Cambiar contraseña → `ChangePassword`
5. Notificaciones → `NotificationPrefs`
6. Privacidad → `PrivacySettings`
7. Ayuda → `Help`
8. Cerrar sesión — limpiar sesión + navegar a Login

### ChangePasswordScreen
- Verificar contraseña actual contra BD
- Actualizar `UserEntity.password` en Room

### NotificationPrefsScreen
- Toggle por tipo: matches, mensajes, posts
- Guardar en `UserSettingsEntity`

### EditProfileScreen
- Guardar cambios de nombre, bio, universidad, ubicación en `UserEntity`

---

## Sub-proyecto C — Datos dinámicos

### ProfileScreen
- Leer `currentUser` de `AuthViewModel`
- Mostrar: nombre, bio, universidad, ubicación, seguidores, siguiendo, streak
- Posts del usuario: filtrar `PostRepository` por `authorId == currentUser.id`

### FeedScreen
- `FeedViewModel`: ya usa `PostRepository.getAllPosts()` — verificar que el Flow esté activo
- Posts del usuario activo marcados visualmente

### NotificationsScreen
- `NotificationViewModel`: filtrar por `currentUser.id`
- Seed: agregar notificaciones para los 5 usuarios

### DiscoveryScreen
- Mantener datos de muestra de vacantes (no hay entidad `JobEntity` aún)
- Mostrar nombre del usuario activo en el header

### ChatList / Perfil en nav
- Mostrar initials/nombre del usuario activo en lugares que antes decían "Sarah Jenkins"

---

## Sub-proyecto B — Chat completo

### Cambios a MessageEntity
```kotlin
enum class MessageStatus { SENT, DELIVERED, READ }

MessageEntity:
  + status: MessageStatus = MessageStatus.SENT
  + fileName: String? = null      // para archivos
  + fileSize: String? = null      // "2.4 MB"
```

### Cambios a MessageDao
- `getMessagesForConversation(conversationId): Flow<List<MessageEntity>>`
- `markConversationAsRead(conversationId, userId)`: UPDATE status = READ
- `insert(message): Long`

### ConversationDao (nuevo o en MessageDao)
- `getConversationsForUser(userId): Flow<List<ConversationEntity>>`
- `getOrCreateConversation(userId1, userId2): Long`

### ChatListScreen
- Cargar conversaciones donde `userId1 == currentUser.id` OR `userId2 == currentUser.id`
- Mostrar: nombre del otro usuario, último mensaje, hora, badge de no leídos
- Click → navegar a ChatScreen con `conversationId` (cambio de ruta: `chat/{conversationId}`)

### ChatScreen
- Parámetro de ruta: `conversationId: Long` (reemplazar `contactName: String`)
- Cargar mensajes via `Flow` desde Room
- Mensaje enviado: burbujas alineadas a la derecha, fondo verde
- Mensaje recibido: burbujas a la izquierda, fondo gris
- Estados: ✓ enviado, ✓✓ entregado, ✓✓ azul = leído
- Redacción: `TextField` + botón enviar + botón adjuntar
- Adjuntar: simular con selector de nombre de archivo, crear `MessageEntity(type=FILE, fileName="CV_Carlos.pdf")`
- Al entrar: marcar mensajes del otro como READ

### ChatViewModel
- `loadConversations(userId)`: Flow de conversaciones del usuario
- `loadMessages(conversationId)`: Flow de mensajes
- `sendMessage(conversationId, senderId, content)`: insert en Room
- `sendDocument(conversationId, senderId, fileName, fileSize)`: insert FILE type
- `markAsRead(conversationId, currentUserId)`: update status

### Seed de mensajes ampliado
- 3 conversaciones con historial realista de 5-8 mensajes cada una
- Variedad: TEXT, FILE, con distintos status (SENT, DELIVERED, READ)

---

## Navegación

### Rutas actualizadas
- `chat/{contactName}` → `chat/{conversationId}` (Long)
- Todas las rutas de Settings ya existen

### JoviNavGraph
- Propagar `currentUser` de `AuthViewModel` a las pantallas que lo necesiten
- `startDestination` dinámico según sesión activa

---

## Invariantes de implementación
1. **Jetpack Compose** para toda UI — sin XML
2. **Room** para toda persistencia — sin SharedPreferences para datos de app (solo para sesión y splash_skip)
3. **MVVM + Repository** — sin lógica de negocio en Composables
4. **StateFlow** para todo estado reactivo
5. La BD ya está en versión 1 — cualquier cambio de esquema requiere `migration` o `fallbackToDestructiveMigration` (usaremos destructive para dev)
