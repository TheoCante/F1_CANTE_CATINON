package fr.audric.tp1

import androidx.room.Entity
import androidx.room.PrimaryKey

// Classe représentant une image stockée dans le système de fichiers
@Entity
data class StoredImage(
    @PrimaryKey val imageName: String
) : CommonImage
