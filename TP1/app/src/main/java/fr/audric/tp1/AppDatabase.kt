package fr.audric.tp1

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Image::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}
