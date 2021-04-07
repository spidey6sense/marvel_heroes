package becker.alexey.marvelheroes.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HeroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeroes(posts: List<HeroEntity>)

    @Query("SELECT * FROM heroes ORDER BY id ASC")
    fun getHeroesPaged(): PagingSource<Int, HeroEntity>

    @Query("SELECT * FROM heroes WHERE id=:heroId")
    fun getHeroBiography(heroId: Int): Flow<HeroEntity>

    @Query("DELETE FROM heroes")
    suspend fun clearHeroes()

    @Query("SELECT * FROM api_settings WHERE id=0")
    fun getHeroesCount(): ApiRequestSettingsEntity

    @Query("UPDATE api_settings SET `hero_count`=:heroCount WHERE id=0")
    suspend fun updateHeroesCount(heroCount: Int)

    @Query("UPDATE api_settings SET `offset`=:offset WHERE id=0")
    suspend fun updateOffset(offset: Int)

    @Query("SELECT * FROM api_settings WHERE id=0")
    fun getOffset(): ApiRequestSettingsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApiRequestParams(offset: ApiRequestSettingsEntity)

}