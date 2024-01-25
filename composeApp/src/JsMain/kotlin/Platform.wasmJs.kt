import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import utils.AppCoroutineDispatchers
import utils.TYPE

class WasmPlatform: Platform {
    override val type = TYPE.WEB
}

actual fun getPlatform(): Platform = WasmPlatform()

//actual suspend fun provideDbDriver(
//    schema: SqlSchema<QueryResult.AsyncValue<Unit>>
//): SqlDriver {
//    return WebWorkerDriver(
//        Worker(
//            js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
//        )
//    ).also { schema.create(it).await() }
//}

actual class AppCoroutineDispatchersImpl actual constructor() : AppCoroutineDispatchers {
    override val io: CoroutineDispatcher
        get() = Dispatchers.Default
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
}