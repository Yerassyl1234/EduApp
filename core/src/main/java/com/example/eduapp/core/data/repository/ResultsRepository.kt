package com.example.eduapp.core.data.repository

import com.example.eduapp.core.domain.model.TestResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResultsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun saveResult(result: TestResult): Result<Unit> {
        return try {
            firestore.collection("results").add(result).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun publishResult(resultId: String): Result<Unit> {
        return try {
            firestore.collection("results").document(resultId)
                .update("published", true).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getPublishedResultsForUser(userId: String): Result<List<TestResult>> {
        return try {
            val snapshot = firestore.collection("results")
                .whereEqualTo("userId", userId)
                .whereEqualTo("published", true)
                .get().await()
            val results = snapshot.documents.mapNotNull { doc ->
                doc.toObject(TestResult::class.java)?.copy(id = doc.id)
            }
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getAllResults(): Result<List<TestResult>> {
        return try {
            val snapshot = firestore.collection("results")
                .orderBy("answeredAt", Query.Direction.DESCENDING)
                .get().await()
            val results = snapshot.documents.mapNotNull { doc ->
                doc.toObject(TestResult::class.java)?.copy(id = doc.id)
            }
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getResultsForUser(userId: String): Result<List<TestResult>> {
        return try {
            val snapshot = firestore.collection("results")
                .whereEqualTo("userId", userId)
                .orderBy("answeredAt", Query.Direction.DESCENDING)
                .get().await()
            val results = snapshot.documents.mapNotNull { doc ->
                doc.toObject(TestResult::class.java)?.copy(id = doc.id)
            }
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getResultsForTest(testId: String): Result<List<TestResult>> {
        return try {
            val snapshot = firestore.collection("results")
                .whereEqualTo("testId", testId)
                .orderBy("answeredAt", Query.Direction.DESCENDING)
                .get().await()
            val results = snapshot.documents.mapNotNull { doc ->
                doc.toObject(TestResult::class.java)?.copy(id = doc.id)
            }
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
