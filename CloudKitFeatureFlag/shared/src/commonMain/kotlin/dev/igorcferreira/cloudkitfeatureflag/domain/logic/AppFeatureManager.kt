package dev.igorcferreira.cloudkitfeatureflag.domain.logic

import dev.igorcferreira.cloudkitfeatureflag.domain.repository.AppFeatureRepository
import dev.igorcferreira.cloudkitfeatureflag.domain.repository.FileRepository
import dev.igorcferreira.cloudkitfeatureflag.model.AppFeatures
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

class AppFeatureManager internal constructor(
    private val appFeatureRepository: AppFeatureRepository,
    private val fileRepository: FileRepository,
    private val json: Json,
    private val coroutineScope: CoroutineScope
) {
    private val _recordState = MutableStateFlow(AppFeatures.empty)
    private var refreshJob: Job? = null

    val recordState: StateFlow<AppFeatures> get() = _recordState.asStateFlow()
    val state: AppFeatures get() = _recordState.value

    init { loadInitialState() }

    fun startRefresh() {
        refreshJob?.cancel()
        refreshJob = coroutineScope.launch {
            while (isActive) {
                updateState()
                delay(60_000) //Delay 1 minute
            }
        }
    }

    fun stopRefresh() {
        refreshJob?.cancel()
        refreshJob = null
    }

    private suspend fun updateState() {
        val features = appFeatureRepository.getAppFeatures()
        val content = json.encodeToString(features)
        fileRepository.writeFile(FILE_NAME, content)
        _recordState.update { features }
    }

    private fun loadInitialState() = coroutineScope.launch {
        _recordState.update { fetchStoredContent() }
        updateState()
    }

    private fun fetchStoredContent(): AppFeatures {
        val content = fileRepository.readFile(FILE_NAME)
            ?: return state
        return json.decodeFromString(content)
    }

    private companion object {
        const val FILE_NAME = ".app_remote.config"
    }
}
