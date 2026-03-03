package dev.igorcferreira.appfunctiondemo.functions

import androidx.appfunctions.AppFunctionSerializable
import kotlinx.serialization.SerialName

/**
 * A Task
 *
 * @param id            The task ID (UUID string)
 * @param title         Title of the task, as entered by the user
 * @param description   The task description, as entered by the user, giving more context about what is the task
 * @param board         The board where the task will be put
 */
@AppFunctionSerializable(isDescribedByKdoc = true)
data class Task(val id: String, val title: String, val description: String?, val board: String)

/**
 * The Board in which the task is posted
 */
enum class Board {
    @SerialName("General")
    GENERAL
}
