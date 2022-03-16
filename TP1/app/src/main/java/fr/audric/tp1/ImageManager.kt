package fr.audric.tp1

import android.content.Context
import android.util.Log
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

// Classe pour la gestion d'Image
class ImageManager(applicationContext: Context) {
    // Récupération de la base de données des images stockées
    val db : AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "image-db"
    ).build()

    // Client Http
    val client = HttpClient(OkHttp)
    // Contexte de l'application
    val ctx = applicationContext
    // Préfixe des images générées par l'API
    val urlPrefix = "https://generated.inspirobot.me/a/"

    // Récupération d'une Image par Internet
    suspend fun genereImage(): GeneratedImage {
        // Requête Http pour créer l'url d'une image
        val httpResponse: HttpResponse = client.get("https://inspirobot.me/api?generate=true")
        // Récupération de l'url de l'image créée
        val url: String = httpResponse.receive()
        // Création d'une image GeneratedImage à partir de l'url
        return GeneratedImage(url)
    }

    // Récupère une LiveData de la base pour écouter les changements
    fun watchSavedImages(): LiveData<List<StoredImage>> =  db.imageDao().watchAll()

    // Retourne le ByteArray décrivant l'image
    private suspend fun getImageFromWeb(image: StoredImage):ByteArray {
        // Requête Http pour récupérer l'image
        val httpResponse: HttpResponse = client.get(image.imageUrl)
        // Récupération de l'url de l'image en ByteArray
        return httpResponse.receive()
    }

    // Sauvegarde une image dans la base de données et la stockée dans un fichier
    suspend fun saveImage(imageUrl: String){
        // Création d'une StoredImage à partir de l'image
        val imageToSave = StoredImage(imageUrl.removePrefix(urlPrefix))

        // Création d'un nom de fichier à partir de l'url
        val filename = imageToSave.imageName
        // Creation d'un fichier a partir du nom
        val file = File(ctx.filesDir, filename)

        try {
            // Récupération de l'image depuis le Web
            val imageArray = getImageFromWeb(imageToSave)
            // Sauvegarde de l'image dans un fichier
            withContext(Dispatchers.IO) {
                file.writeBytes(imageArray)
            }
            // Sauvegarde de l'image dans la base de données
            db.imageDao().insertAll(imageToSave)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("FILE", "error", e)
        }
    }

    // Méthode pour récupérer une url depuis une StoredImage
    val StoredImage.imageUrl:String
        get() = urlPrefix + imageName

}
