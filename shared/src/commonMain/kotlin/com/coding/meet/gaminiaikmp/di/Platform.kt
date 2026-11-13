package com.coding.meet.gaminiaikmp.di

import androidx.compose.ui.graphics.ImageBitmap
import com.coding.meet.gaminiaikmp.utils.TYPE

expect fun getPlatform(): TYPE

expect fun ByteArray.toComposeImageBitmap(): ImageBitmap

expect fun isNetworkAvailable(): Boolean
