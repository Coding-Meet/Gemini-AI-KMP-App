package com.coding.meet.gaminiaikmp.utils

import kotlinx.coroutines.CoroutineDispatcher

interface AppCoroutineDispatchers {
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
}