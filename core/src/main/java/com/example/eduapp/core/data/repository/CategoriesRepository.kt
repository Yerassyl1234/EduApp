package com.example.eduapp.core.data.repository

import com.example.eduapp.core.domain.model.Category
import com.example.eduapp.core.domain.model.Component
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriesRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val snapshot = firestore.collection("categories")
                .orderBy("order", Query.Direction.ASCENDING)
                .get().await()
            val categories = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Category::class.java)?.copy(id = doc.id)
            }
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getComponents(categoryId: String): Result<List<Component>> {
        return try {
            val snapshot = firestore.collection("components")
                .whereEqualTo("categoryId", categoryId)
                .get().await()
            val components = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Component::class.java)?.copy(id = doc.id)
            }
            Result.success(components)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getComponent(componentId: String): Result<Component> {
        return try {
            val doc = firestore.collection("components")
                .document(componentId).get().await()
            val component = doc.toObject(Component::class.java)?.copy(id = doc.id)
                ?: throw Exception("Component not found")
            Result.success(component)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addCategory(category: Category): Result<String> {
        return try {
            val data = hashMapOf(
                "title" to category.title,
                "imageUrl" to category.imageUrl,
                "order" to category.order,
                "published" to category.published
            )
            val docRef = firestore.collection("categories").add(data).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCategory(category: Category): Result<Unit> {
        return try {
            val data = hashMapOf(
                "title" to category.title,
                "imageUrl" to category.imageUrl,
                "order" to category.order,
                "published" to category.published
            )
            firestore.collection("categories").document(category.id).set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun publishCategory(categoryId: String): Result<Unit> {
        return try {
            firestore.collection("categories").document(categoryId)
                .update("published", true).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCategory(categoryId: String): Result<Unit> {
        return try {
            val components = firestore.collection("components")
                .whereEqualTo("categoryId", categoryId)
                .get().await()
            components.documents.forEach { it.reference.delete().await() }
            firestore.collection("categories").document(categoryId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addComponent(component: Component): Result<String> {
        return try {
            val data = hashMapOf(
                "categoryId" to component.categoryId,
                "title" to component.title,
                "description" to component.description,
                "composition" to component.composition,
                "function" to component.function,
                "imageUrl" to component.imageUrl,
                "modelFileName" to component.modelFileName
            )
            val docRef = firestore.collection("components").add(data).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateComponent(component: Component): Result<Unit> {
        return try {
            val data = hashMapOf(
                "categoryId" to component.categoryId,
                "title" to component.title,
                "description" to component.description,
                "composition" to component.composition,
                "function" to component.function,
                "imageUrl" to component.imageUrl,
                "modelFileName" to component.modelFileName
            )
            firestore.collection("components").document(component.id).set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteComponent(componentId: String): Result<Unit> {
        return try {
            firestore.collection("components").document(componentId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
