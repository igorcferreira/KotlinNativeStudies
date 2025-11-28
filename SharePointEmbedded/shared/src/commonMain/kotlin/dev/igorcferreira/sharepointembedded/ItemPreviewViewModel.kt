package dev.igorcferreira.sharepointembedded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import dev.igorcferreira.msgraphapi.MSGraphAPI
import dev.igorcferreira.msgraphapi.files.previewItem
import dev.igorcferreira.msgraphapi.files.response.DriveItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ItemPreviewViewModel(
    private val graphAPI: MSGraphAPI,
    private val driveId: String
): ViewModel() {
    private val _loading = MutableStateFlow(false)
    private val _previewUrl = MutableStateFlow<String?>(null)
    private val _error = MutableStateFlow<String?>(null)

    @NativeCoroutinesState
    val loading: StateFlow<Boolean>
        get() = _loading.asStateFlow()
    @NativeCoroutinesState
    val previewUrl: StateFlow<String?>
        get() = _previewUrl.asStateFlow()
    @NativeCoroutinesState
    val error: StateFlow<String?>
        get() = _error.asStateFlow()

    fun load(item: DriveItem) = load(itemId = item.id)

    fun load(itemId: String) = viewModelScope.launch {
        try {
            _error.update { null }
            _loading.update { true }
            _previewUrl.update {
                graphAPI.previewItem(driveId, itemId).getUrl
            }
        } catch (ex: Exception) {
            _error.update { ex.message }
        } finally {
            _loading.update { false }
        }
    }
}
