package fr.audric.tp1

import android.content.Context
import android.util.Log
import androidx.room.Room
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class ImageManager(applicationContext: Context) {
    var db : AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "image-db"
    ).build()

    val client = HttpClient(OkHttp)
    val ctx = applicationContext
    val urlPrefix = "https://generated.inspirobot.me/a/"

    suspend fun genereImage(): Image? {
        try{
            val httpResponse: HttpResponse = client.get("https://inspirobot.me/api?generate=true")
            var stringBody: String = httpResponse.receive()
            stringBody = stringBody.substring(34)
            return Image(stringBody.hashCode(), stringBody)
        }
        catch(e:Exception){
            Log.e("Err","Erreur réseau")
        }
        return null
    }

    suspend fun getSavedImages(): List<Image> {
        val imageDao = db.imageDao()
        val images: List<Image> = imageDao.getAll()
        return images
    }

    suspend fun saveImage(image : Image){
        val imageDao = db.imageDao()
        try{
            imageDao.insertAll(image)
            Log.i("image","image saved")
            val filename = image.imageName
            ctx.openFileOutput(filename, Context.MODE_PRIVATE).use {
                val c = getByteArray(image)
                it.write(c)
            }
        }
        catch (e: Exception){
            Log.i("image","image not saved")
        }
    }

    suspend fun getByteArray(image: Image): ByteArray? {
        try{
            val c= ctx.openFileInput(image.imageName).readBytes()
            if(c.isNotEmpty()) return c
        }
        catch (e: Exception){
            Log.i("FILE","Image non stockée")
        }
        try{
            val httpResponse: HttpResponse = client.get(getImageUrl(image))
            return httpResponse.receive()
        }
        catch (e: Exception){
            Log.e("FILE","Pas de co")
        }
        return null
    }

    fun getImageUrl(image: Image): String{
        return urlPrefix + image.imageName
    }
}
