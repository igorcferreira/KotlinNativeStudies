package dev.igorcferreira.sharepointembedded

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.reflect.KClass

@Composable
inline fun <reified T: Any> rememberSerialSaveable(
    vararg inputs: Any?,
    noinline init: () -> MutableState<T>,
): MutableState<T> = rememberSaveable(
    inputs = inputs,
    stateSaver = SerializerSaver.of<T>(),
    init = init
)

@Composable
inline fun <reified T: Any> rememberOptionalSerialSaveable(
    vararg inputs: Any?,
    noinline init: () -> MutableState<T?>,
): MutableState<T?> = rememberSaveable(
    inputs = inputs,
    stateSaver = OptionalSerializerSaver.of<T>(),
    init = init
)

@OptIn(InternalSerializationApi::class)
open class SerializerSaver<T: Any>(
    private val json: Json = Json { isLenient = true; ignoreUnknownKeys = true },
    private val clazz: KClass<T>
): Saver<T, String> {
    override fun SaverScope.save(value: T): String = json.encodeToString(clazz.serializer(), value)
    override fun restore(value: String): T = json.decodeFromString(clazz.serializer(),value)

    companion object {
        inline fun <reified T: Any> of() = SerializerSaver(clazz = T::class)
    }
}

@OptIn(InternalSerializationApi::class)
open class OptionalSerializerSaver<T: Any>(
    private val json: Json = Json { isLenient = true; ignoreUnknownKeys = true },
    private val clazz: KClass<T>
): Saver<T?, String> {
    override fun SaverScope.save(value: T?): String? = if (value == null) {
        null
    } else {
        json.encodeToString(clazz.serializer(), value)
    }
    override fun restore(value: String): T? = json.decodeFromString(clazz.serializer(), value)

    companion object {
        inline fun <reified T: Any> of() = OptionalSerializerSaver(clazz = T::class)
    }
}


/// Example of usage:

@Serializable
private data class SampleObject(
    val name: String
)

@Suppress("unused")
@Composable
private fun SampleViewList(
    data: List<SampleObject>,
    modifier: Modifier = Modifier,
    onSelection: (SampleObject) -> Unit = {}
) {
    var currentItem by rememberOptionalSerialSaveable<SampleObject> { mutableStateOf(null) }

    fun choose(item: SampleObject) {
        currentItem = item
        onSelection(item)
    }

    LazyColumn(modifier = modifier.padding(16.dp)) {
        item { Text(currentItem?.name ?: stringResource(R.string.no_item_selected)) }
        items(data) { item ->
            Button(onClick = { choose(item) }) {
                Text(item.name)
            }
        }
    }
}

@Composable
@Preview("Sample", showBackground = true)
private fun SampleViewList() = Scaffold { innerPadding ->
    val data = remember { mutableStateListOf(
        SampleObject("First"),
        SampleObject("Second"),
        SampleObject("Third"),
    ) }
    SampleViewList(data, onSelection = {}, modifier = Modifier.padding(innerPadding))
}
