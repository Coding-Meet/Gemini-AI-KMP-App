import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import utils.*

interface Platform {
    val type: TYPE
}

expect fun getPlatform(): Platform

expect suspend fun provideDbDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>
): SqlDriver


expect class AppCoroutineDispatchersImpl(): AppCoroutineDispatchers