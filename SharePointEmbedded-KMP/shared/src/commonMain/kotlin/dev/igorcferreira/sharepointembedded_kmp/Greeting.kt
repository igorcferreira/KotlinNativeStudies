package dev.igorcferreira.sharepointembedded_kmp

import com.arkanakeys.ArkanaKeys
import dev.igorcferreira.msgraphapi.MSGraphAPI
import dev.igorcferreira.msgraphapi.files.listChildren
import dev.igorcferreira.msgraphapi.files.model.DriveItem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WrappedStateFlow<T>(
    private val emitter: StateFlow<T>
) {
    private val scope = MainScope()

    fun listenTo(
        block: (T) -> Unit
    ): T {
        scope.launch {
            emitter.collect { block(it) }
        }
        return emitter.value
    }
}

class Greeting {
    private val _loadedDrives = MutableStateFlow<String>("")
    val loadedDrives: WrappedStateFlow<String>
        get() = WrappedStateFlow(_loadedDrives.asStateFlow())

    private val platform = getPlatform()
    private val graphAPI = MSGraphAPI(
        tenantId = ArkanaKeys.Global.tenantId,
        clientID = ArkanaKeys.Global.clientId,
        clientSecret = { _ -> ArkanaKeys.Global.clientSecret }
    )

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }

    suspend fun loadItems() = _loadedDrives.update {
        try {
            graphAPI.listChildren(ArkanaKeys.Global.containerId, null)
                .joinToString("\n") { it.name }
        } catch (_: Exception) {
            ""
        }
    }

    suspend fun listFiles() = listFiles(itemId = null)
    suspend fun listFiles(
        itemId: String?,
    ) = try {
        graphAPI.listChildren(ArkanaKeys.Global.containerId, itemId)
            .joinToString("\n", transform = DriveItem::name)
    } catch (e: Exception) {
        e.message ?: "Error"
    }
}
