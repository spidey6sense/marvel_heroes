package becker.alexey.marvelheroes.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.withTransaction
import becker.alexey.marvelheroes.api.MarvelApi
import becker.alexey.marvelheroes.data.DataMediator.Companion.HEROES_PAGE_SIZE

class DataRepository(private val heroesDatabase: HeroesDatabase, private val marvelApi: MarvelApi) {
    @OptIn(ExperimentalPagingApi::class)
    fun getHeroesFlow() = Pager(
        config = PagingConfig(HEROES_PAGE_SIZE),
        remoteMediator = DataMediator(heroesDatabase, marvelApi)
    ) {
        heroesDatabase.heroDao().getHeroesPaged()
    }.flow

    suspend fun clearHeroes() {
        heroesDatabase.withTransaction {
            heroesDatabase.heroDao().clearHeroes()
            heroesDatabase.heroDao()
                .insertApiRequestParams(ApiRequestSettingsEntity(0, 0, HEROES_PAGE_SIZE))
        }
    }

    fun getHeroDatabase(): HeroesDatabase {
        return heroesDatabase
    }
}