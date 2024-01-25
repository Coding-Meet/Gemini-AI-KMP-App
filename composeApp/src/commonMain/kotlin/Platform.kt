import utils.*

interface Platform {
    val type: TYPE
}

expect fun getPlatform(): Platform


expect class AppCoroutineDispatchersImpl(): AppCoroutineDispatchers