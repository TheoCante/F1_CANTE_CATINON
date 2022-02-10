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
    // Recuperation de la base de donnes des images stockes
    val db : AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "image-db"
    ).build()

    // Le client Http
    val client = HttpClient(OkHttp)
    // Le contexte de l'application
    val ctx = applicationContext
    // Le prefix des image genere par notre API
    val urlPrefix = "https://generated.inspirobot.me/a/"

    // Recuperation d'une Image par Internet
    suspend fun genereImage(): GeneratedImage {
        // Requete Http pour creer l'url d'une image
        val httpResponse: HttpResponse = client.get("https://inspirobot.me/api?generate=true")
        // Recuperation de l'url de l'image Cree
        val url: String = httpResponse.receive()
        // Creation d'une image GeneratedImage a partir de l'url
        return GeneratedImage(url)
    }

    // Recupere une LiveData de la base pour ecouter les changements
    fun watchSavedImages(): LiveData<List<StoredImage>> =  db.imageDao().watchAll()

    // Retourne le ByteArray decrivant l'image
    private suspend fun getImageFromWeb(image: StoredImage):ByteArray {
        // Requete Http pour recuperer l'image
        val httpResponse: HttpResponse = client.get(image.imageUrl)
        // Recuperation de l'url de l'image en ByteArray
        return httpResponse.receive()
    }

    // Sauvegarde une image dans la base de donnes et la stocke dans un fichier
    suspend fun saveImage(imageUrl: String){
        // Creation d'une StoredImage a partir de l'image
        val imageToSave = StoredImage(imageUrl.removePrefix(urlPrefix))

        // Creation d'un nom de fichier a partir de l'url
        val filename = imageToSave.imageName
        // Creation d'un fichier a partir du nom
        val file = File(ctx.filesDir, filename)

        try {
            //  Recuperation de l'image depuis le Web
            val imageArray = getImageFromWeb(imageToSave)
            //  Sauvegarde de l'image dans un fichier
            withContext(Dispatchers.IO) {
                file.writeBytes(imageArray)
            }
            // Sauvegarde de l'image dans la base de donnes
            db.imageDao().insertAll(imageToSave)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("FILE", "error", e)
        }
    }

    //metchode pour recuperer une url depuis une StoredImage
    val StoredImage.imageUrl:String
        get() = urlPrefix + imageName

}
