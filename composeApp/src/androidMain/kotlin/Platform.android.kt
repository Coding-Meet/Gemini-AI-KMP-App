import androidx.compose.ui.platform.ClipboardManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import utils.AppCoroutineDispatchers
import utils.TYPE

class AndroidPlatform : Platform {
    override val type = TYPE.MOBILE
}

actual fun getPlatform(): Platform = AndroidPlatform()

//actual suspend fun provideDbDriver(
//    schema: SqlSchema<QueryResult.AsyncValue<Unit>>
//): SqlDriver {
//    val mainSchema = GeminiApiChatDB.Schema
//
//    return AndroidSqliteDriver(mainSchema, KoinPlatform.getKoin().get(), "gemini_api_chat",
//        callback = object : AndroidSqliteDriver.Callback(mainSchema) {
//            override fun onOpen(db: SupportSQLiteDatabase) {
//                db.setForeignKeyConstraintsEnabled(true)
//            }
//        })
//}
actual suspend fun clipData(clipboardManager: ClipboardManager) : String? {
    return  clipboardManager.getText()?.text.toString().trim()
}
actual class AppCoroutineDispatchersImpl actual constructor() : AppCoroutineDispatchers {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
}
