package dev.igorcferreira.appfunctiondemo.di

import dev.igorcferreira.appfunctiondemo.functions.Board
import dev.igorcferreira.appfunctiondemo.functions.Task
import dev.igorcferreira.appfunctiondemo.repository.TaskRepository
import org.koin.dsl.module
import java.util.UUID

val repositoryModule = module {
   single<TaskRepository> { object : TaskRepository{
       override suspend fun createTask(
           title: String,
           description: String?,
           board: Board
       ): Task = Task(id = UUID.randomUUID().toString(), title = title, description = description, board = board.name)
       override suspend fun updateTask(
           id: String,
           title: String,
           description: String?,
           board: Board
       ): Task = Task(id = id, title = title, description = description, board = board.name)
       override suspend fun getAll(): List<Task> = emptyList()
       override suspend fun findByBoard(board: Board): List<Task> = emptyList()
   } }
}
