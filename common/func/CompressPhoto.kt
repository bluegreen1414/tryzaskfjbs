package com.kapirti.social_chat_food_video.common.func

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream


fun compressPhoto(bitmap: Bitmap, quality: Int = 80): ByteArray {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    return outputStream.toByteArray()
}
