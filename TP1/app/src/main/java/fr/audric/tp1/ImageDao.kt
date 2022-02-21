package fr.audric.tp1

import androidx.lifecycle.LiveData
import androidx.room.*

// Notre interface d'acces aux donnes de la base contenant les fichiers stockes
@Dao
interface ImageDao {

    // Recupere une LiveData de la base pour ecouter les changements
    @Query("SELECT * FROM storedimage")
    fun watchAll(): LiveData<List<StoredImage>>

    //  Insere les image donnes dans la base
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg images: StoredImage)

    //  Supprime l'image stocke
    @Delete
    suspend fun delete(image: StoredImage)
}
