package com.store_me.storeme.repository.storeme

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.store_me.storeme.utils.exception.ApiException
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 이미지 관련 Repository
 */
interface ImageRepository {
    suspend fun uploadImage(folderName: String, uniqueName: String, uri: Uri, onProgress: (Float) -> Unit): Result<String>

    suspend fun uploadImages(folderName: String, accountId: String, uris: List<Uri>, onProgress: (Float) -> Unit): Result<List<String>>
}

class ImageRepositoryImpl @Inject constructor(

): ImageRepository {
    override suspend fun uploadImage(folderName: String, uniqueName: String, uri: Uri, onProgress: (Float) -> Unit): Result<String> {
        return suspendCoroutine { continuation ->
            val storage = Firebase.storage
            val storageRef = storage.getReference(folderName)

            val fileName = "${uniqueName}_${System.currentTimeMillis()}"

            val imageRef = storageRef.child("${fileName}.jpeg")

            imageRef.putFile(uri)
                .addOnProgressListener { taskSnapshot ->
                    val bytesTransferred = taskSnapshot.bytesTransferred
                    val totalByteCount = taskSnapshot.totalByteCount
                    val progress = if (totalByteCount > 0) (100.0f * bytesTransferred / totalByteCount) else 0.0f

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

    override suspend fun uploadImages(folderName: String, accountId: String, uris: List<Uri>, onProgress: (Float) -> Unit): Result<List<String>> {
        return suspendCoroutine { continuation ->
            val storage = Firebase.storage
            val storageRef = storage.getReference(folderName)
            val urls = mutableListOf<String>()
            var uploadCount = 0

            for(uri in uris) {
                val fileName = "${accountId}_${System.currentTimeMillis()}"

                val imageRef = storageRef.child("${fileName}.jpeg")

                imageRef.putFile(uri)
                    .addOnSuccessListener {

                        imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                            Timber.d("이미지 업로드 성공: $downloadUrl")

                            urls.add(downloadUrl.toString())
                            uploadCount++

                            if(uploadCount == uris.size) {
                                continuation.resume(Result.success(urls))
                            }
                        }.addOnFailureListener { e ->
                            Timber.e("URL 가져오기 실패: ${e.message}")
                            continuation.resume(Result.failure(ApiException(code = null, message = e.message)))
                        }

                    }
                    .addOnFailureListener { e ->
                        Timber.e("이미지 업로드 실패: ${e.message}")
                        continuation.resume(Result.failure(ApiException(code = null, message = e.message)))
                    }
            }
        }
    }
}