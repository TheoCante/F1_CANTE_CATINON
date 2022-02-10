package fr.audric.tp1

import androidx.room.Entity
import androidx.room.PrimaryKey

//classe representant une image stocke dans le systeme de fichier
@Entity
data class StoredImage(
    @PrimaryKey val imageName: String
) : CommonImage
