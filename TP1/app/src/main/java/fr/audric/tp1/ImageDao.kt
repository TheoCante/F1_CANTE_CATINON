package fr.audric.tp1

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

// Notre interface d'acces aux donnes de la base contenant les fichiers stockes
@Dao
interface ImageDao {

    // Recupere une LiveData de la base pour ecouter les changements
    @Query("SELECT * FROM storedimage")
    fun watchAll(): LiveData<List<StoredImage>>

    //  Insere les image donnes dans la base
    @Insert
    suspend fun insertAll(vararg images: StoredImage)

    //  Supprime l'image stocke
    @Delete
    suspend fun delete(image: StoredImage)
}
