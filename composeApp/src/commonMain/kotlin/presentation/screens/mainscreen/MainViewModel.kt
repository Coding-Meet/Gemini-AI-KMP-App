package presentation.screens.mainscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import com.russhwolf.settings.*
import di.getPlatform
import moe.tlaster.precompose.viewmodel.ViewModel
import utils.Screens
import com.coding.meet.gaminiaikmp.BuildKonfig
import domain.use_cases.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope
import utils.AppCoroutineDispatchers


class MainViewModel(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val getAllGroupUserCase: GetAllGroupUserCase,
    private val insertGroupUseCase: InsertGroupUseCase
) : ViewModel() {

    private val settings = Settings()
    var screens by mutableStateOf(Screens.MAIN)

    var isNewChatShowDialog by mutableStateOf(false)
    var newGroupText by mutableStateOf("")

    val platformType = getPlatform()

    var isApiShowDialog by mutableStateOf(false)
    var apiKeyText by mutableStateOf("")

    var isAlertDialogShow by mutableStateOf(false)
    var alertTitleText by mutableStateOf("")
    var alertDescText by mutableStateOf("")

    var currentPos by mutableStateOf(-1)
    var isDesktopDrawerOpen by mutableStateOf(true)

    private val _uiState = MutableStateFlow(GroupUiState())
    val uiState: StateFlow<GroupUiState> = _uiState.asStateFlow()

    init {
        getGroupList()
    }

    fun getGroupList() {
        viewModelScope.launch(appCoroutineDispatchers.io) {
            _uiState.update { GroupUiState(data = getAllGroupUserCase.getGroupList()) }
        }
    }
    fun addNewGroup(groupId: String, groupName: String, date: String, icon: String) {
        viewModelScope.launch(appCoroutineDispatchers.io) {
            insertGroupUseCase.insertGroup(
                groupId, groupName, date, icon
            )
            getGroupList()
        }
    }

    fun setApikeyLocalStorage(apiKeyText: String) {
        settings["GEMINI_API_KEY"] = apiKeyText
    }

    fun getApikeyLocalStorage(): String {
        return settings.getString("GEMINI_API_KEY", BuildKonfig.GEMINI_API_KEY)
    }

}