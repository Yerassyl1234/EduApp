package com.example.eduapp.core.data.repository

import com.example.eduapp.core.domain.model.Question
import com.example.eduapp.core.domain.model.Test
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getTests(): Result<List<Test>> {
        return try {
            val snapshot = firestore.collection("tests").get().await()
            val tests = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Test::class.java)?.copy(id = doc.id)
            }
            Result.success(tests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getQuestions(testId: String): Result<List<Question>> {
        return try {
            val snapshot = firestore.collection("questions")
                .whereEqualTo("testId", testId)
                .get().await()
            val questions = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Question::class.java)?.copy(id = doc.id)
            }
            Result.success(questions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun createTest(test: Test): Result<String> {
        return try {
            val docRef = firestore.collection("tests").add(test).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun addQuestion(question: Question): Result<String> {
        return try {
            val docRef = firestore.collection("questions").add(question).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun deleteTest(testId: String): Result<Unit> {
        return try {
            firestore.collection("tests").document(testId).delete().await()
            val questions = firestore.collection("questions")
                .whereEqualTo("testId", testId).get().await()
            for (doc in questions.documents) {
                doc.reference.delete().await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
