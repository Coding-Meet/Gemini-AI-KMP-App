package viewmodels

import AllPlatform
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import utils.Screens

class MainViewModel  : ViewModel() {

    var searchText by mutableStateOf("")
    var screens by mutableStateOf(Screens.MAIN)
    var userText by mutableStateOf("")
    val allPlatform = AllPlatform().type()
}