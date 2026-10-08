package com.example.app_eleitoral

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import android.util.Base64

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

object Ui {
    fun message(activity: AppCompatActivity, text: String) {
        AlertDialog.Builder(activity).setMessage(text).setPositiveButton("OK", null).show()
    }

    fun space(activity: AppCompatActivity, height: Int = 12) =
        View(activity).apply { layoutParams = LinearLayout.LayoutParams(1, height) }
}

    object ImageUtils {
        fun bitmapToBase64(bitmap: Bitmap): String {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        fun base64ToBitmap(base64String: String): Bitmap? {
            return try {
                val decodedByte = Base64.decode(base64String, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
            } catch (e: Exception) {
                null
            }
        }
    }
