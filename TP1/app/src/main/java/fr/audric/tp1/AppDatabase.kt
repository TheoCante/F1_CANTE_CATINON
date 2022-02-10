package fr.audric.tp1

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StoredImage::class], version = 2)
// la base de donnees pour les image
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}
