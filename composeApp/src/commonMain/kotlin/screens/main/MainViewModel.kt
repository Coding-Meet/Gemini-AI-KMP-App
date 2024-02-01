package screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import com.russhwolf.settings.*
import getPlatform
import models.Group
import moe.tlaster.precompose.viewmodel.ViewModel
import utils.Screens
import com.coding.meet.gaminiaikmp.BuildKonfig

class MainViewModel : ViewModel() {

    private val settings = Settings()
    var searchText by mutableStateOf("")
    var screens by mutableStateOf(Screens.MAIN)

    var isChatShowDialog by mutableStateOf(false)
    var newGroupText by mutableStateOf("")

    val platformType = getPlatform()

    var isApiShowDialog by mutableStateOf(false)
    var apiKeyText by mutableStateOf("")

    var currentPos by mutableStateOf(-1)
    val groupList = mutableStateListOf<Group>()
    var isDesktopDrawerOpen by mutableStateOf(true)


    fun setApikeyLocalStorage(apiKeyText :String) {
        settings["GEMINI_API_KEY"] = apiKeyText
    }

    fun getApikeyLocalStorage() :String{
        return settings.getString("GEMINI_API_KEY",BuildKonfig.GEMINI_API_KEY)
    }

}