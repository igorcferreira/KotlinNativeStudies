@file:Suppress("unused")
@file:OptIn(ExperimentalObjCRefinement::class)

package dev.igorcferreira.sharepointembedded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkanakeys.ArkanaKeys
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import dev.igorcferreira.msgraphapi.MSGraphAPI
import dev.igorcferreira.msgraphapi.authentication.MSAuthenticationProvider
import dev.igorcferreira.msgraphapi.files.listChildren
import dev.igorcferreira.msgraphapi.files.response.DriveItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

class AppViewModel(
    private val graphAPI: MSGraphAPI
): ViewModel() {
    private val _loading = MutableStateFlow(false)
    private val _list = MutableStateFlow<List<DriveItem>>(emptyList())
    private val _error = MutableStateFlow<Exception?>(null)
    private val _username = MutableStateFlow<String?>(null)

    @NativeCoroutinesState
    val list: StateFlow<List<DriveItem>>
        get() = _list.asStateFlow()

    @NativeCoroutinesState
    val loading: StateFlow<Boolean>
        get() = _loading.asStateFlow()

    @NativeCoroutinesState
    val error: StateFlow<Exception?>
        get() = _error.asStateFlow()

    @NativeCoroutinesState
    val username: StateFlow<String?>
        get() = _username.asStateFlow()

    fun load() = load(item= null)
    fun load(item: DriveItem?) {
        if (item != null && !item.isFolder) { return }
        load(itemId = item?.id)
    }

    fun load(itemId: String?) = viewModelScope.launch {
        try {
            _loading.update { true }
            _error.update { null }
            _list.update {
                graphAPI.listChildren(ArkanaKeys.Global.containerId, itemId)
            }
        } catch (e: Exception) {
            _error.update { e }
        } finally {
            _loading.update { false }
        }
    }

    fun authenticate() = authenticate(scopes = DEFAULT_SCOPES)

    fun authenticate(
        scopes: List<String>
    ) = authenticate(scopes) {}

    @HiddenFromObjC
    fun authenticate(
        scopes: List<String> = DEFAULT_SCOPES,
        prepare: suspend MSAuthenticationProvider.() -> Unit,
    ) = viewModelScope.launch {
        try {
            _error.update { null }

            val provider = MSAuthenticationProvider(
                tenantId = ArkanaKeys.Global.tenantId,
                clientId = ArkanaKeys.Global.clientId,
                scopes = scopes
            )
            prepare(provider)
            provider.getToken()

            _username.update { provider.getUserName() }
        } catch (e: Exception) {
            _error.update { e }
        }
    }

     private companion object {
         val DEFAULT_SCOPES = listOf("https://graph.microsoft.com/.default")
     }
}
