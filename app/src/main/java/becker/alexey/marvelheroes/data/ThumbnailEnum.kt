package becker.alexey.marvelheroes.data

enum class ThumbnailEnum(val value: String) {
    THUMBNAIL_NOT_AVAILABLE("image_not_available"),
    THUMBNAIL_SMALL("/standard_medium."),
    THUMBNAIL_BIG("/standard_fantastic."),
    HTTP("http"),
    HTTPS("https")
}