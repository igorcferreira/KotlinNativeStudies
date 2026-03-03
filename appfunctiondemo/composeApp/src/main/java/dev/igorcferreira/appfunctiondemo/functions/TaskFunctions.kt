package dev.igorcferreira.appfunctiondemo.functions

import androidx.appfunctions.AppFunctionContext
import androidx.appfunctions.AppFunctionInvalidArgumentException
import androidx.appfunctions.service.AppFunction
import dev.igorcferreira.appfunctiondemo.repository.TaskRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale.getDefault

class TaskFunctions: KoinComponent {
    private val repository: TaskRepository by inject()

    /**
     * List the available Boards
     *
     * @param appFunctionContext    The context in which the AppFunction is executed
     *
     * @return The list of valid Board options
     */
    @AppFunction(isDescribedByKdoc = true)
    fun listBoards(
        appFunctionContext: AppFunctionContext
    ): List<String> = Board.entries.map { it.capitalize() }

    /**
     * List all the current tasks which are present in the general board.
     *
     * @param appFunctionContext    The context in which the AppFunction is executed
     *
     * @return The list of [Task] objects currently saved
     */
    @AppFunction(isDescribedByKdoc = true)
    suspend fun listTasks(
        appFunctionContext: AppFunctionContext
    ): List<Task> = repository.getAll()

    /**
     * List all the tasks in a specific board
     *
     * @param appFunctionContext    The context in which the AppFunction is executed
     * @param board                 The specific board where the task should be listed
     *
     * @return The created [Task]
     */
    @AppFunction(isDescribedByKdoc = true)
    suspend fun listTasksOnBoard(
        appFunctionContext: AppFunctionContext,
        board: String
    ): List<Task> = repository.findByBoard(board.toBoard())

    /**
     * Allows the update of a Task information. Including title and description
     *
     * @param appFunctionContext    The context in which the AppFunction is executed
     * @param task                  The task which will be updated
     * @param title                 The new title of the task. It can be omitted to keep the old title as it is
     * @param description           The new description of the task. It can be omitted to keep the old description as it is
     * @param board                 A new board to where the task will be moved to. It can be omitted to keep the task in the current board
     *
     * @return The updated [Task]
     */
    @AppFunction(isDescribedByKdoc = true)
    suspend fun editTask(
        appFunctionContext: AppFunctionContext,
        task: Task,
        title: String? = null,
        description: String? = null,
        board: String? = null,
    ): Task = repository.updateTask(
        id = task.id,
        title = title ?: task.title,
        description = description ?: task.description,
        board = board?.toBoard() ?: task.board.toBoard()
    )

    /**
     * Adds a new task to the general board.
     *
     * @param appFunctionContext    The context in which the AppFunction is executed
     * @param title                 The task title. This will be the text shown to the user in the general list of tasks
     * @param description           The optional description of the task. The user will be able to see this text when entering in the task details
     * @param board                 The board where the task will be assigned to. Defaults to "General"
     *
     * @return The created [Task]
     */
    @AppFunction(isDescribedByKdoc = true)
    suspend fun createTask(
        appFunctionContext: AppFunctionContext,
        title: String,
        description: String? = null,
        board: String? = null,
    ): Task = repository.createTask(title, description, board?.toBoard() ?: Board.GENERAL)
}

fun Board.capitalize(): String =
    name
        .lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }

@Throws(AppFunctionInvalidArgumentException::class)
fun String.toBoard(): Board = Board.entries.firstOrNull {
        this.equals(it.name, ignoreCase = true)
    } ?: throw AppFunctionInvalidArgumentException("""
                    The board must be of of ${Board.entries.joinToString(", ") { it.capitalize() }}
                """.trimIndent())
