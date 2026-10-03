package com.example.app_eleitoral

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import android.util.Base64

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

object Ui {
    val green = Color.rgb(46, 125, 50)

    fun root(activity: AppCompatActivity): LinearLayout =
        LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 18, 28, 24)
            setBackgroundResource(R.drawable.fundofinal)
            addView(ImageView(activity).apply {
                setImageResource(R.drawable.eleitoral)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                contentDescription = "Logo da pesquisa eleitoral"
            }, LinearLayout.LayoutParams(-1, 92))
        }

    fun title(activity: AppCompatActivity, text: String): TextView =
        TextView(activity).apply {
            this.text = text
            textSize = 25f
            setTextColor(Color.rgb(20, 35, 55))
            gravity = Gravity.CENTER
            setPadding(0, 6, 0, 22)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

    fun button(activity: AppCompatActivity, text: String, action: () -> Unit) =
        Button(activity).apply {
            this.text = text
            setOnClickListener { action() }
            setTextColor(Color.WHITE)
            setBackgroundColor(green)
            textSize = 15f
            minHeight = 48.dp
            setPadding(16.dp, 14.dp, 16.dp, 14.dp)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 8.dp
                bottomMargin = 8.dp
            }
        }
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
