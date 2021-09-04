package hr.algebra.nasaapplication.handler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import hr.algebra.nasaapplication.factory.createGetHttpUrlConnection
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection

private const val ARTICLE_PICTURE_WIDTH = 1000
private const val ARTICLE_PICTURE_HEIGHT = 750
private const val QUALITY = 100
private const val TAG = "ImagesHandler"


/**
 * Downloads an image from a given url resizes it and saves it to android file system
 * @param url url of the image for download
 * @param fileName the name of picture to be used on android device
 * @return file path of the picture saved on android device, can be null!!!
 */

fun downloadImageAndStore(context: Context, url: String, fileName: String): String? {
    val extension = url.substring(url.lastIndexOf(".")) // .jpg
    val file: File = getFile(context, fileName, extension) // android/.../image_name.jpg

    try {
        val con: HttpURLConnection = createGetHttpUrlConnection(url)
        con.inputStream.use { `is` ->
            file.outputStream().use { fos ->
                val bitmap = BitmapFactory.decodeStream(`is`)
                val resizedBitmap: Bitmap = getResizedBitmap(
                    bitmap,
                    ARTICLE_PICTURE_HEIGHT,
                    ARTICLE_PICTURE_WIDTH
                )

                //Byte array for fos
                val buffer: ByteArray = getBytesFromBitmap(resizedBitmap)
                fos.write(buffer)
                return file.absolutePath
            }
        }
    } catch (e: Exception) {
        Log.e(e.toString(), e.message, e)
    }

    return null
}

private fun getBytesFromBitmap(bitmap: Bitmap): ByteArray {
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, bos)
    return bos.toByteArray()
}

private fun getResizedBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
    val scaleWidth = newWidth.toFloat() / bitmap.width
    val scaleHeight = newHeight.toFloat() / bitmap.height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
}

private fun getFile(context: Context, fileName: String, extension: String): File {
    var dir: File? = context.applicationContext.getExternalFilesDir(null)
    var file = File(dir, File.separator.toString() + fileName + extension)
    if (file.exists()) {
        file.delete()
    }
    return file
}

///sdcard/Android/data/hr.algebra.nasaapplication/files/A_Nearby_Supernova_in_Spiral_Galaxy_M100.jpg
