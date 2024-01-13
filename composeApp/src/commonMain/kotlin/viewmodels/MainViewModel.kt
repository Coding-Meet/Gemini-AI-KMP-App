package viewmodels

import AllPlatform
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import com.russhwolf.settings.*
import dev.icerock.moko.mvvm.viewmodel.ViewModel
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
    val allPlatform = AllPlatform().type()
    val imageUris = mutableStateListOf<String>()

    var isShowDialog by mutableStateOf(false)
    var currentPos by mutableStateOf(-1)
    val robotList = mutableStateListOf<Robot>()
    val isGenerating by mutableStateOf(false)
    var isDesktopDrawerOpen by mutableStateOf(true)


    fun setApikeyLocalStorage() {
        settings["api_key"] = newChartRobotAndApiKeyText.trim()
    }

    fun getApikeyLocalStorage() {
        newChartRobotAndApiKeyText = settings.getString("api_key","")
    }


}