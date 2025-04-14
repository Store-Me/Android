package com.store_me.storeme.repository.storeme

import android.net.Uri
import androidx.core.net.toFile
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.store_me.auth.Auth
import com.store_me.storeme.utils.exception.ApiException
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 이미지 관련 Repository
 */
interface ImageRepository {
    suspend fun uploadImage(folderName: String, uri: Uri, onProgress: (Float) -> Unit): Result<String>

    suspend fun uploadVideo(folderName: String, uri: Uri, mimeType: String, onProgress: (Float) -> Unit): Result<String>
}

class ImageRepositoryImpl @Inject constructor(
    private val auth: Auth
): ImageRepository {
    override suspend fun uploadImage(folderName: String, uri: Uri, onProgress: (Float) -> Unit): Result<String> {
        return suspendCoroutine { continuation ->
            val storage = Firebase.storage
            val storageRef = storage.getReference(folderName)

            val fileName = "${auth.getStoreId()}_${System.currentTimeMillis()}"

            val imageRef = storageRef.child("${fileName}.jpeg")

            imageRef.putFile(uri)
                .addOnProgressListener { taskSnapshot ->
                    val bytesTransferred = taskSnapshot.bytesTransferred
                    val totalByteCount = taskSnapshot.totalByteCount
                    val progress = if (totalByteCount > 0) {
                        (100.0f * bytesTransferred / totalByteCount)
                    } else {
                        0.0f
                    }

                    onProgress(progress)
                    Timber.d("업로드 진행 중: ${progress.toInt()}%")
                }
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        Timber.d("이미지 업로드 성공: $downloadUrl")
                        continuation.resume(Result.success(downloadUrl.toString()))
                    }.addOnFailureListener { e ->
                        Timber.e("URL 가져오기 실패: ${e.message}")
                        continuation.resume(Result.failure(ApiException(code = null, message = e.message)))
                    }

                }.addOnFailureListener { e ->
                    Timber.e("이미지 업로드 실패: ${e.message}")
                    continuation.resume(Result.failure(ApiException(code = null, message = e.message)))
                }
        }
    }

    override suspend fun uploadVideo(
        folderName: String,
        uri: Uri,
        mimeType: String,
        onProgress: (Float) -> Unit
    ): Result<String> {
        return suspendCoroutine { continuation ->
            val storage = Firebase.storage
            val storageRef = storage.getReference(folderName)

            // 확장자 추출
            val extension = android.webkit.MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(mimeType) ?: "mp4"

            val fileName = "${auth.getStoreId()}_${System.currentTimeMillis()}.$extension"
            val videoRef = storageRef.child(fileName)

            // 메타데이터 설정
            val metadata = com.google.firebase.storage.StorageMetadata.Builder()
                .setContentType(mimeType)
                .build()

            videoRef.putFile(uri, metadata)
                .addOnProgressListener { taskSnapshot ->
                    val bytesTransferred = taskSnapshot.bytesTransferred
                    val totalByteCount = taskSnapshot.totalByteCount
                    val progress = if (totalByteCount > 0) {
                        100.0f * bytesTransferred / totalByteCount
                    } else {
                        0.0f
                    }

                    onProgress(progress)
                    Timber.d("동영상 업로드 진행 중: ${progress.toInt()}%")
                }
                .addOnSuccessListener {
                    videoRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        Timber.d("동영상 업로드 성공: $downloadUrl")
                        continuation.resume(Result.success(downloadUrl.toString()))
                    }.addOnFailureListener { e ->
                        Timber.e("URL 가져오기 실패: ${e.message}")
                        continuation.resume(Result.failure(ApiException(code = null, message = e.message)))
                    }
                }
                .addOnFailureListener { e ->
                    Timber.e("동영상 업로드 실패: ${e.message}")
                    continuation.resume(Result.failure(ApiException(code = null, message = e.message)))
                }
        }
    }
}