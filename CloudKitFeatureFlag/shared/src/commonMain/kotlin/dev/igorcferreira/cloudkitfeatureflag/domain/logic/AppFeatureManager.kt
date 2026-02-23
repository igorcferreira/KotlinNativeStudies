package dev.igorcferreira.cloudkitfeatureflag.domain.logic

import dev.igorcferreira.cloudkitfeatureflag.domain.repository.AppFeatureRepository
import dev.igorcferreira.cloudkitfeatureflag.model.AppFeatures
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppFeatureManager(
    private val appFeatureRepository: AppFeatureRepository,
    private val coroutineScope: CoroutineScope
) {
    private val _recordState = MutableStateFlow(AppFeatures())
    val recordState: StateFlow<AppFeatures> get() = _recordState.asStateFlow()
    val state: AppFeatures get() = _recordState.value

    init { loadInitialState() }

    fun refresh() = coroutineScope.launch { updateState() }

    private suspend fun updateState() = _recordState.update {
        appFeatureRepository.getAppFeatures()
    }

    private fun loadInitialState() = coroutineScope.launch {
        //TODO: Implement local storage/load
        _recordState.update { AppFeatures() }
        updateState()
    }
}
