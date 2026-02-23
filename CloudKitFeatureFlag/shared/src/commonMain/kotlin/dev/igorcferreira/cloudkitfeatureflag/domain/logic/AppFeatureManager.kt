package dev.igorcferreira.cloudkitfeatureflag.domain.logic

import dev.igorcferreira.cloudkitfeatureflag.domain.repository.AppFeatureRepository
import dev.igorcferreira.cloudkitfeatureflag.domain.repository.FileRepository
import dev.igorcferreira.cloudkitfeatureflag.model.AppFeatures
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class AppFeatureManager(
    private val appFeatureRepository: AppFeatureRepository,
    private val fileRepository: FileRepository,
    private val json: Json,
    private val coroutineScope: CoroutineScope
) {
    private val _recordState = MutableStateFlow(AppFeatures())
    val recordState: StateFlow<AppFeatures> get() = _recordState.asStateFlow()
    val state: AppFeatures get() = _recordState.value

    init { loadInitialState() }

    fun refresh() = coroutineScope.launch { updateState() }

    private suspend fun updateState() {
        val features = appFeatureRepository.getAppFeatures()
        val content = json.encodeToString(features)
        fileRepository.writeFile(".app_remote.config", content)
        _recordState.update { features }
    }

    private fun loadInitialState() = coroutineScope.launch {
        _recordState.update { fetchStoredContent() }
        updateState()
    }

    private fun fetchStoredContent(): AppFeatures {
        val content = fileRepository.readFile("_remote.config")
            ?: return AppFeatures()
        return json.decodeFromString(content)
    }
}
