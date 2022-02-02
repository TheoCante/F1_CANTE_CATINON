package fr.audric.tp1

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Query("SELECT * FROM image")
    suspend fun getAll(): List<Image>

    @Query("SELECT * FROM image WHERE uid IN (:imageIds)")
    suspend fun loadAllByIds(imageIds: IntArray): List<Image>

    @Insert
    suspend fun insertAll(vararg images: Image)

    @Delete
    suspend fun delete(image: Image)
}
