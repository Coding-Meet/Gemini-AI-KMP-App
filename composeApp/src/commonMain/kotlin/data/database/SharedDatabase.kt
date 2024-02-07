package data.database

import app.cash.sqldelight.ColumnAdapter
import com.coding.meet.gaminiaikmp.GeminiApiChatDB
import com.coding.meet.gaminiaikmp.Message
import di.DatabaseDriverFactory
import domain.model.Role
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class SharedDatabase(
    private val driverProvider: DatabaseDriverFactory,
) {
    private var database: GeminiApiChatDB? = null

    @OptIn(ExperimentalEncodingApi::class)
    val byteArrayToStringAdapter = object : ColumnAdapter<List<ByteArray>, String> {
        override fun decode(databaseValue: String): List<ByteArray> {
            if (databaseValue.isNotEmpty()) {
                val encodedList = databaseValue.split(",")
                val decodedList = ArrayList<ByteArray>()

                for (encodedBytes in encodedList) {
                    val byteArray = Base64.decode(encodedBytes)
                    decodedList.add(byteArray)
                }

                return decodedList
            } else {
                return emptyList()
            }
        }

        override fun encode(value: List<ByteArray>): String {
            if (value.isNotEmpty()) {

                val encodedList = ArrayList<String>()

                for (byteArray in value) {
                    val encodedBytes = Base64.encode(byteArray)
                    encodedList.add(encodedBytes)
                }
                return encodedList.joinToString(",")
            } else {
                return ""
            }
        }

    }

    private val roleToStringAdapter = object : ColumnAdapter<Role, String> {
        override fun decode(databaseValue: String): Role {
            return Role.valueOf(databaseValue)
        }

        override fun encode(value: Role): String {
            return value.name
        }
    }

    private suspend fun initDatabase() {
        if (database == null) {
            database = GeminiApiChatDB.invoke(
                driverProvider.createDriver(),
                MessageAdapter = Message.Adapter(
                    byteArrayToStringAdapter, roleToStringAdapter
                )
            )
        }
    }

    suspend operator fun <R> invoke(block: suspend (GeminiApiChatDB) -> R): R {
        initDatabase()
        return block(database!!)
    }
}