package fr.audric.tp1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class StoredImage(
    @PrimaryKey val imageName: String
) : CommonImage {

}
