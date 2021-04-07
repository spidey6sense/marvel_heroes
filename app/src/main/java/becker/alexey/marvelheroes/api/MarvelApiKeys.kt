package becker.alexey.marvelheroes.api

import becker.alexey.marvelheroes.BuildConfig
import java.math.BigInteger
import java.security.MessageDigest

object MarvelApiKeys {
    private const val privateKey = BuildConfig.API_MARVEL_PRIVATE_KEY
    private const val publicKey = BuildConfig.API_MARVEL_PUBLIC_KEY
    private var timestamp = 0L

    private fun updateTimestamp() {
        this.timestamp = System.currentTimeMillis()
    }

    fun getNewPublicHashKey(): String {
        updateTimestamp()
        return md5(timestamp.toString() + privateKey + publicKey)
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun getPublicApiKey(): String {
        return this.publicKey
    }

    fun getCurrentTimestamp(): String {
        return this.timestamp.toString()
    }
}