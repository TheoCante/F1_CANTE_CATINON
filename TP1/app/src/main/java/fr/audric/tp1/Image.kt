package fr.audric.tp1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Image(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "imagePath") val imagePath: String?
)
