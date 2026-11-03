package com.coding.meet.gaminiaikmp.presentation.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * Type-safe navigation routes for the application.
 */
@Serializable
sealed interface ScreenRoute : NavKey {
    @Serializable
    data object ConversationList : ScreenRoute

    @Serializable
    data class ConversationDetail(val groupId: String = "") : ScreenRoute

    @Serializable
    data object Settings : ScreenRoute
}

val nav3Configuration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(ScreenRoute.ConversationList::class, ScreenRoute.ConversationList.serializer())
            subclass(ScreenRoute.ConversationDetail::class, ScreenRoute.ConversationDetail.serializer())
            subclass(ScreenRoute.Settings::class, ScreenRoute.Settings.serializer())
        }
    }
}

