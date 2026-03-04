# Add project specific ProGuard rules here.

# Firebase
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Firestore model classes
-keep class com.example.eduapp.core.domain.model.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Filament / SceneView (AR)
-keep class com.google.android.filament.** { *; }
-keep class io.github.sceneview.** { *; }
-keep class com.google.ar.** { *; }

# Compose
-dontwarn androidx.compose.**

# Coil
-keep class coil.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keep line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile