package fr.audric.tp1

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.room.Room
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageManager(applicationContext: Context) {
    var db : AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "image-db"
    ).build()

    val client = HttpClient(OkHttp)
    val ctx = applicationContext
    val urlPrefix = "https://generated.inspirobot.me/a/"

    suspend fun genereImage(): GeneratedImage {
        val httpResponse: HttpResponse = client.get("https://inspirobot.me/api?generate=true")
        var url: String = httpResponse.receive()
        return GeneratedImage(url)
    }

    suspend fun getSavedImages(): List<StoredImage> {
        val imageDao = db.imageDao()
        val images: List<StoredImage> = imageDao.getAll()
        return images
    }

    fun watchSavedImages(): LiveData<List<StoredImage>> =  db.imageDao().watchAll()


    private suspend fun getImageFromWeb(image: StoredImage):ByteArray {
        val httpResponse: HttpResponse = client.get(image.imageUrl)
        return httpResponse.receive()
    }


    suspend fun saveImage(imageUrl: String?){
        if(imageUrl == null) return
        val imageToSave: StoredImage = StoredImage(imageUrl.removePrefix(urlPrefix))
        val imageDao = db.imageDao()
        imageDao.insertAll(imageToSave)
        Log.i("image", "image saved")
        val filename = imageToSave.imageName
        val file = File(ctx.filesDir, filename)
        try {
            val imageArray = getImageFromWeb(imageToSave)
            withContext(Dispatchers.IO) {
                file.writeBytes(imageArray)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("FILE", "error", e)
        }
    }

    val StoredImage.imageUrl:String
    get() = urlPrefix + imageName

}
