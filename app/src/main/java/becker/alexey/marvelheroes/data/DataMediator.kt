package becker.alexey.marvelheroes.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import becker.alexey.marvelheroes.api.MarvelApi
import becker.alexey.marvelheroes.api.MarvelApiKeys
import becker.alexey.marvelheroes.data.heroresponse.HeroesResponse
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class DataMediator(
    private val heroesDatabase: HeroesDatabase,
    private val marvelApi: MarvelApi
) : RemoteMediator<Int, HeroEntity>() {
    companion object {
        const val HEROES_PAGE_SIZE = 4
    }

    private var offset = 0
    private var maxHeroCount = HEROES_PAGE_SIZE

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HeroEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    getApiConfig()
                    if (offset + HEROES_PAGE_SIZE > maxHeroCount) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    offset
                }
            }

            if (loadType != LoadType.PREPEND) {
                if (loadType == LoadType.REFRESH) {
                    resetDatabase()
                }

                val response = marvelApi.getNewHeroes(
                    HEROES_PAGE_SIZE,
                    loadKey,
                    MarvelApiKeys.getPublicApiKey(),
                    MarvelApiKeys.getNewPublicHashKey(),
                    MarvelApiKeys.getCurrentTimestamp()
                )

                heroesDatabase.withTransaction {
                    updateBase(
                        convertHeroResponseToEntity(response),
                        offset + HEROES_PAGE_SIZE,
                        response.data.total
                    )
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = maxHeroCount < offset
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun updateBase(heroList: List<HeroEntity>, offset: Int, maxHeroCount: Int) {
        heroesDatabase.heroDao().insertHeroes(heroList)
        heroesDatabase.heroDao().updateOffset(offset)
        heroesDatabase.heroDao().updateHeroesCount(maxHeroCount)
    }

    private suspend fun resetDatabase() {
        offset = 0
        maxHeroCount = 20
        heroesDatabase.withTransaction {
            heroesDatabase.heroDao().clearHeroes()
            heroesDatabase.heroDao().updateOffset(0)
            heroesDatabase.heroDao().updateHeroesCount(HEROES_PAGE_SIZE)
        }
    }

    private suspend fun getApiConfig() {
        offset = heroesDatabase.withTransaction {
            heroesDatabase.heroDao().getOffset().offset
        }
        maxHeroCount = heroesDatabase.withTransaction {
            heroesDatabase.heroDao().getHeroesCount().heroCount
        }
//        Log.i(
//            "Api Request Config:",
//            "offset: $offset page size:$HEROES_PAGE_SIZE max hero count:$maxHeroCount"
//        )
    }

    private fun convertHeroResponseToEntity(response: HeroesResponse): List<HeroEntity> {
        val heroesList = mutableListOf<HeroEntity>()
//        Log.i("Hero", response.toString())
        response.data.results.forEach {
            val hero = HeroEntity(
                0,
                it.name,
                it.thumbnail.path.replace(ThumbnailEnum.HTTP.value, ThumbnailEnum.HTTPS.value),
                it.thumbnail.extension,
                it.description
            )
            // Log.i("Hero", hero.toString())
            heroesList.add(hero)
        }
        return heroesList
    }
}