//
//  AppIntents.swift
//  iosApp
//
//  Created by Igor Ferreira on 2/3/26.
//
import Foundation
import AppIntents

struct ListTasksIntent: Sendable, AppIntent {
    static var title: LocalizedStringResource = "List the Tasks"
    let repository: TaskRepository = DependencyBag.make()
    
    func perform() async throws -> some ReturnsValue<[Task]> {
        await .result(value: repository.getAll())
    }
}

struct ListTasksOnBoardIntent: Sendable, AppIntent {
    static var title: LocalizedStringResource = "List the Tasks on Board"
    let repository: TaskRepository = DependencyBag.make()
    
    @Parameter(
        title: "Board",
        description: "The specific board where the task should be listed",
        default: Board.general
    )
    var board: Board
    
    func perform() async throws -> some ReturnsValue<[Task]> {
        await .result(value: repository.fetch(board: board))
    }
}

struct EditTaskIntent: Sendable, AppIntent {
    static var title: LocalizedStringResource = "Edit an existing board"
    let repository: TaskRepository = DependencyBag.make()
    
    @Parameter(
        title: "Task",
        description: "The task which will be updated"
    )
    var task: Task
    
    @Parameter(
        title: "Title",
        description: "The new title of the task. It can be omitted to keep the old title as it is",
        default: nil
    )
    var title: String?
    
    @Parameter(
        title: "Title",
        description: "The new description of the task. It can be omitted to keep the old description as it is",
        default: nil
    )
    var description: String?
    
    @Parameter(
        title: "Board",
        description: "A new board to where the task will be moved to. It can be omitted to keep the task in the current board",
        default: nil
    )
    var board: Board?
    
    func perform() async throws -> some ReturnsValue<Task> {
        await .result(value: repository.updateTask(
            id: task.id,
            title: title ?? task.title,
            description: description ?? task.description,
            board: board ?? task.board
        ))
    }
}

struct CreateTaskIntent: Sendable, AppIntent {
    static var title: LocalizedStringResource = "Create a new task"
    let repository: TaskRepository = DependencyBag.make()
    
    @Parameter(
        title: "Title",
        description: "The task title. This will be the text shown to the user in the general list of tasks"
    )
    var title: String
    
    @Parameter(
        title: "Description",
        description: "The optional description of the task. The user will be able to see this text when entering in the task details"
    )
    var description: String?
    
    @Parameter(
        title: "Board",
        description: "The board where the task will be assigned to. Defaults to \"General\"",
        default: Board.general
    )
    var board: Board
    
    func perform() async throws -> some ReturnsValue<Task> {
        await .result(value: repository.createTask(title: title, description: description, board: board))
    }
}

struct AppIntentProvider: AppShortcutsProvider, Sendable {
    @AppShortcutsBuilder
    static var appShortcuts: [AppShortcut] {
        AppShortcut(
            intent: ListTasksIntent(),
            phrases: [
                "List all tasks on \(.applicationName)"
            ],
            shortTitle: "List Tasks",
            systemImageName: "paperplane.fill"
        )
        AppShortcut(
            intent: ListTasksOnBoardIntent(),
            phrases: [
                "List the tasks on \(\.$board) on \(.applicationName)",
                "Find all tasks on \(.applicationName)'s \(\.$board)"
            ],
            shortTitle: "List Tasks on Board",
            systemImageName: "paperplane.fill"
        )
        AppShortcut(
            intent: EditTaskIntent(),
            phrases: [
                "Edit \(\.$task) on \(.applicationName)",
            ],
            shortTitle: "Edit a specific task",
            systemImageName: "paperplane.fill"
        )
        AppShortcut(
            intent: CreateTaskIntent(),
            phrases: [
                "Create a new task on \(.applicationName)",
                "Add a new taks into the \(\.$board) board on \(.applicationName)"
            ],
            shortTitle: "Create a new task",
            systemImageName: "paperplane.fill"
        )
    }
}
