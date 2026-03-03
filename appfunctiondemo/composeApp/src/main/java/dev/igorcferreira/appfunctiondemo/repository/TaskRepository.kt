package dev.igorcferreira.appfunctiondemo.repository

import dev.igorcferreira.appfunctiondemo.functions.Board
import dev.igorcferreira.appfunctiondemo.functions.Task

interface TaskRepository {
    suspend fun createTask(title: String, description: String?, board: Board): Task
    suspend fun updateTask(id: String, title: String, description: String?, board: Board): Task
    suspend fun getAll(): List<Task>
    suspend fun findByBoard(board: Board): List<Task>
}
