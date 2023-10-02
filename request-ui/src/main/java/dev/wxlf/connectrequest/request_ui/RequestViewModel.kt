package dev.wxlf.connectrequest.request_ui

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.wxlf.connectrequest.data.models.StreetModel
import dev.wxlf.connectrequest.data.remote.repositories.StatRepository
import dev.wxlf.connectrequest.request_ui.models.HouseDisplayableModel
import dev.wxlf.connectrequest.request_ui.models.StreetDisplayableModel
import dev.wxlf.connectrequest.request_ui.util.mapToDisplayable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Stable
data class RequestUiState(
    val isError: Boolean = false,
    val errorMsg: String = "",
    val errorOn: ErrorOn = ErrorOn.IDLE,
    val streets: List<StreetDisplayableModel> = listOf(),
    val houses: List<HouseDisplayableModel> = listOf()
)

enum class ErrorOn {
    IDLE, LoadStreets
}

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val statRepository: StatRepository
) : ViewModel() {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    private var streets: ArrayList<StreetModel> = arrayListOf()

    private val _uiState: MutableStateFlow<RequestUiState> = MutableStateFlow(
        RequestUiState()
    )
    val uiState = _uiState.asStateFlow()

    fun loadStreets() = viewModelScope.launch(defaultDispatcher) {
        statRepository.fetchAllStreets().fold(
            onSuccess = { streets = it },
            onFailure = { setErrorState(it.localizedMessage.orEmpty(), ErrorOn.LoadStreets) }
        )
    }

    fun searchStreets(query: String) =
        _uiState.update { currentState ->
            currentState.copy(
                isError = false,
                errorMsg = "",
                streets = streets.mapToDisplayable()
                    .filter { street -> street.street.contains(query) }
            )
        }

    private suspend fun setErrorState(msg: String, errorOn: ErrorOn) = withContext(defaultDispatcher) {
        _uiState.update { it.copy(isError = true, errorMsg = msg, errorOn = errorOn) }
    }
}