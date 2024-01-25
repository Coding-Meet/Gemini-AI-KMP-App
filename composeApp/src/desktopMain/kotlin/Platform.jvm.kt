import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import utils.AppCoroutineDispatchers
import utils.TYPE

class JVMPlatform: Platform {
    override val type = TYPE.DESKTOP
}

actual fun getPlatform(): Platform = JVMPlatform()

//actual suspend fun provideDbDriver(
//    schema: SqlSchema<QueryResult.AsyncValue<Unit>>
//): SqlDriver {
//    return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
//        .also { schema.create(it).await() }
//}

actual class AppCoroutineDispatchersImpl actual constructor() : AppCoroutineDispatchers {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
}