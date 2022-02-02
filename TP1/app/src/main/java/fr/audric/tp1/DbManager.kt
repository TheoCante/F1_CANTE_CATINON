package fr.audric.tp1

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

class DbManager(applicationContext: Context) {
    var db : AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "image-db"
    ).build()

    suspend fun getSavedImages(): List<Image> {
        val imageDao = db.imageDao()
        val images: List<Image> = imageDao.getAll()
        return images
    }

    suspend fun saveImage(imagePath : String){
        val image = Image(imagePath.hashCode(),imagePath)
        val imageDao = db.imageDao()
        try{
            imageDao.insertAll(image)
        }
        catch (e: Exception){}

    }
}
