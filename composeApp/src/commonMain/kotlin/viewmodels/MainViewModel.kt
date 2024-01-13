package viewmodels

import AllPlatform
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import models.Robot
import utils.Screens

class MainViewModel  : ViewModel() {

    var searchText by mutableStateOf("")
    var screens by mutableStateOf(Screens.MAIN)
    var userText by mutableStateOf("")
    var newChartRobotText by mutableStateOf("")
    val allPlatform = AllPlatform().type()
    val imageUris = mutableStateListOf<String>()

    var isAddNewChat by mutableStateOf(false)
    var currentPos by mutableStateOf(-1)
    val robotList = mutableStateListOf<Robot>()
    val isGenerating by mutableStateOf(false)
    var isDesktopDrawerOpen by mutableStateOf(true)

}