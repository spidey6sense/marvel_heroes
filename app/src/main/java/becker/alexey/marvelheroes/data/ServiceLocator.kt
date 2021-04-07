package becker.alexey.marvelheroes.data

import android.app.Application
import android.content.Context
import becker.alexey.marvelheroes.api.MarvelApi

interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                        app = context.applicationContext as Application
                    )
                }
                return instance!!
            }
        }

//        /**
//         * Allows tests to replace the default implementations.
//         */
//        @VisibleForTesting
//        fun swap(locator: ServiceLocator) {
//            instance = locator
//        }
    }

    fun getRepository(): DataRepository

    fun getMarvelApi(): MarvelApi
}

open class DefaultServiceLocator(val app: Application) : ServiceLocator {
    private val db by lazy {
        HeroesDatabase.getDatabase(app)
    }

    private val api by lazy {
        MarvelApi.create()
    }

    override fun getRepository(): DataRepository {
        return DataRepository(db, api)
    }

    override fun getMarvelApi(): MarvelApi = api
}