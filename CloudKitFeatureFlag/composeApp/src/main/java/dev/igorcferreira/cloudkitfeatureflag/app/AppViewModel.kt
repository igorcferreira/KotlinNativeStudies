package dev.igorcferreira.cloudkitfeatureflag.app

import androidx.lifecycle.ViewModel
import dev.igorcferreira.cloudkitfeatureflag.domain.logic.AppFeatureManager
import dev.igorcferreira.cloudkitfeatureflag.model.AppFeatures
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class AppViewModel: ViewModel(), KoinComponent {
    private val manager by inject<AppFeatureManager>()
    val state: StateFlow<AppFeatures>
        get() = manager.recordState
}
