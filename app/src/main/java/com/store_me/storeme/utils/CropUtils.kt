package com.store_me.storeme.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.FileProvider
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * UCrop을 사용하기 위한 유틸
 */
object CropUtils {
    /**
     * UCrop을 사용하기 위한 Intent를 반환합니다.
     * @param context Context
     * @param aspectRatio 자르기 비율
     * @param sourceUri 자르기 전의 이미지 Uri
     * @return UCrop을 사용하기 위한 Intent
     */
    fun getCropIntent(
        context: Context,
        aspectRatio: Pair<Float, Float> = Pair(1f, 1f),
        sourceUri: Uri
    ): Intent {
        val options = UCrop.Options().apply {
            // 상태 표시줄 색상
            setStatusBarColor(Color.Black.toArgb())

            setShowCropGrid(false)
            setHideBottomControls(true)


            // 툴바 색상 및 제목
            setToolbarColor(Color.Black.toArgb())
            setToolbarTitle("사진 자르기")
            setToolbarWidgetColor(Color.White.toArgb())

            setActiveControlsWidgetColor(Color.Black.toArgb())

            // 백그라운드 색상
            setRootViewBackgroundColor(Color.Black.toArgb())

            setCompressionFormat(Bitmap.CompressFormat.JPEG)
        }

        val file = File(context.cacheDir, "cropped_image_${System.currentTimeMillis()}.jpg")
        val contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        return UCrop.of(sourceUri, contentUri)
            .withAspectRatio(aspectRatio.first, aspectRatio.second)
            .withMaxResultSize(1024, 1024)
            .withOptions(options)
            .getIntent(context)
    }

    fun deleteCashedImage(uri: Uri, coroutine: CoroutineScope) {
        coroutine.launch(Dispatchers.IO) {
            val file = File(uri.path ?: "")
            if (file.exists()) {
                file.delete()
            }
        }
    }
}