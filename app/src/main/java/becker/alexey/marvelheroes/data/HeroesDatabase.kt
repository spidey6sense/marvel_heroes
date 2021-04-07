package becker.alexey.marvelheroes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [HeroEntity::class, ApiRequestSettingsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HeroesDatabase : RoomDatabase() {
    abstract fun heroDao(): HeroDao

    companion object {
        private const val DATABASE_NAME = "heroes.db"

        @Volatile
        private var INSTANCE: HeroesDatabase? = null
        fun getDatabase(
            context: Context,
        ): HeroesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HeroesDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}