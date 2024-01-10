import utils.TYPE

interface Platform {
    val type: TYPE
}

expect fun getPlatform(): Platform