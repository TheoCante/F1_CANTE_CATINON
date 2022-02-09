package fr.audric.tp1

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Query("SELECT * FROM storedimage")
    suspend fun getAll(): List<StoredImage>

    @Query("SELECT * FROM storedimage")
    fun watchAll(): LiveData<List<StoredImage>>

    @Insert
    suspend fun insertAll(vararg images: StoredImage)

    @Delete
    suspend fun delete(image: StoredImage)
}
