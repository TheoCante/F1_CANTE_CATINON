package fr.audric.tp1

import androidx.lifecycle.LiveData
import androidx.room.*

// Interface d'accès aux données de la base, contient les fichiers stockés
@Dao
interface ImageDao {

    // Récupère une LiveData de la base pour écouter les changements
    @Query("SELECT * FROM storedimage")
    fun watchAll(): LiveData<List<StoredImage>>

    // Insère les images données dans la base
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg images: StoredImage)

    // Supprime l'image stockée
    @Delete
    suspend fun delete(image: StoredImage)
}
