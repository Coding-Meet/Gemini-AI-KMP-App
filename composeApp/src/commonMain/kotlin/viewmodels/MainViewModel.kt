package viewmodels

import AllPlatform
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import com.russhwolf.settings.*
import data.network.client
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import getPlatform
import io.ktor.client.plugins.*
import models.Robot
import utils.DialogType
import utils.Screens

class MainViewModel : ViewModel() {

    val settings = Settings()
    var searchText by mutableStateOf("")
    var screens by mutableStateOf(Screens.MAIN)
    var dialogTypeState by mutableStateOf(DialogType.NEW_CHAT)
    var userText by mutableStateOf("")
    var newChartRobotAndApiKeyText by mutableStateOf("")
    val allPlatform = getPlatform().type

    val imageUris = mutableStateListOf<String>()

    var isShowDialog by mutableStateOf(false)
    var currentPos by mutableStateOf(-1)
    val robotList = mutableStateListOf<Robot>()
    val isGenerating by mutableStateOf(false)
    var isDesktopDrawerOpen by mutableStateOf(true)


    fun setApikeyLocalStorage() {
        settings["api_key"] = newChartRobotAndApiKeyText.trim()
        initializeInterceptor()
    }

    fun getApikeyLocalStorage() :String{
        return settings.getString("api_key","AIzaSyBZ4r4qf_-G9lE1K3N7pHop499KkfdDEbM")
    }
    init {
        initializeInterceptor()
    }

    private fun initializeInterceptor(){
        client.plugin(HttpSend).intercept { builder ->
            builder.url.parameters.append("key", getApikeyLocalStorage())
            execute(builder)
        }
    }
}