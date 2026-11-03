package com.coding.meet.gaminiaikmp.di

import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.utils.TYPE
import io.github.xxfast.kstore.KStore
import org.koin.mp.KoinPlatform

actual fun getPlatform(): TYPE = TYPE.MOBILE

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
}

actual fun isNetworkAvailable(): Boolean {
    val context: Context = KoinPlatform.getKoin().get()
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val cap = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}

actual suspend fun readGroupKStore(readFun: suspend (KStore<List<Group>>) -> Unit) {}
actual suspend fun readChatMessageKStore(readFun: suspend (KStore<List<ChatMessage>>) -> Unit) {}
