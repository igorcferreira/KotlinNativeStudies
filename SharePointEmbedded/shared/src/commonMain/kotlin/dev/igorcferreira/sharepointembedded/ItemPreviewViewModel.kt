package dev.igorcferreira.sharepointembedded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkanakeys.ArkanaKeys
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import dev.igorcferreira.msgraphapi.MSGraphAPI
import dev.igorcferreira.msgraphapi.files.previewItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ItemPreviewViewModel(
    private val graphAPI: MSGraphAPI
): ViewModel() {
    private val _loading = MutableStateFlow(false)
    private val _previewUrl = MutableStateFlow<String?>(null)
    private val _error = MutableStateFlow<String?>(null)
    private val driveId: String
        get() = ArkanaKeys.Global.containerId

    @NativeCoroutinesState
    val loading: StateFlow<Boolean>
        get() = _loading.asStateFlow()
    @NativeCoroutinesState
    val previewUrl: StateFlow<String?>
        get() = _previewUrl.asStateFlow()
    @NativeCoroutinesState
    val error: StateFlow<String?>
        get() = _error.asStateFlow()

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
