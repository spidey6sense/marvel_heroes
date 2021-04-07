package becker.alexey.marvelheroes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "api_settings")
data class ApiRequestSettingsEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "offset")
    val offset: Int,
    @ColumnInfo(name = "hero_count")
    val heroCount: Int
)