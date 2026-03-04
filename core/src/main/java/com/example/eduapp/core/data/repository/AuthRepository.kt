package com.example.eduapp.core.data.repository

import com.example.eduapp.core.domain.model.HelpRequest
import com.example.eduapp.core.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    val currentUserId: String? get() = auth.currentUser?.uid

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("User not found")
            val user = getUser(uid)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        role: String
    ): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Registration failed")
            val user = User(
                id = uid,
                name = name,
                email = email,
                role = role
            )
            firestore.collection("users").document(uid).set(user).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(uid: String): User {
        val doc = firestore.collection("users").document(uid).get().await()
        return doc.toObject(User::class.java) ?: throw Exception("User not found")
    }

    suspend fun getCurrentUser(): User? {
        val uid = currentUserId ?: return null
        return try { getUser(uid) } catch (e: Exception) { null }
    }

    fun signOut() {
        auth.signOut()
    }
    suspend fun updateUserName(name: String): Result<Unit> {
        return try {
            val uid = currentUserId ?: throw Exception("Not logged in")
            firestore.collection("users").document(uid)
                .update("name", name).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateEmail(newEmail: String): Result<Unit> {
        return try {
            auth.currentUser?.verifyBeforeUpdateEmail(newEmail)?.await()
            val uid = currentUserId ?: throw Exception("Not logged in")
            firestore.collection("users").document(uid)
                .update("email", newEmail).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            auth.currentUser?.updatePassword(newPassword)?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun sendHelpRequest(userId: String, userName: String, message: String): Result<Unit> {
        return try {
            val request = hashMapOf(
                "userId" to userId,
                "userName" to userName,
                "message" to message,
                "sentAt" to System.currentTimeMillis()
            )
            firestore.collection("help_requests").add(request).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun updatePhotoUrl(photoUrl: String): Result<Unit> {
        return try {
            val uid = currentUserId ?: throw Exception("Not logged in")
            firestore.collection("users").document(uid)
                .update("photoUrl", photoUrl).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getHelpRequests(): Result<List<HelpRequest>> {
        return try {
            val snapshot = firestore.collection("help_requests")
                .orderBy("sentAt", Query.Direction.DESCENDING)
                .get()
                .await()
            val requests = snapshot.documents.map { doc ->
                HelpRequest(
                    id = doc.id,
                    userId = doc.getString("userId") ?: "",
                    userName = doc.getString("userName") ?: "",
                    message = doc.getString("message") ?: "",
                    sentAt = doc.getLong("sentAt") ?: 0L
                )
            }
            Result.success(requests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
