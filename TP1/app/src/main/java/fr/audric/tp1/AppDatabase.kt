package fr.audric.tp1

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StoredImage::class], version = 2)
// Base de donnees pour les images
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}
