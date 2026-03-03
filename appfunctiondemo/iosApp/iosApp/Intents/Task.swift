//
//  Task.swift
//  iosApp
//
//  Created by Igor Ferreira on 2/3/26.
//
import Foundation
import AppIntents

enum Board: String, Sendable, AppEnum, Codable {
    static var typeDisplayRepresentation = TypeDisplayRepresentation(name: "Board")
    static var caseDisplayRepresentations: [Board : DisplayRepresentation] = [
        .general: DisplayRepresentation(title: "General")
    ]
    
    case general
}

protocol TaskRepository: Sendable {
    func getAll() async -> [Task]
    func fetch(ids: [String]) async -> [Task]
    func fetch(board: Board) async -> [Task]
    func createTask(title: String, description: String?, board: Board) async -> Task
    func updateTask(id: String, title: String, description: String?, board: Board) async -> Task
}

struct MockedTaskRepository: TaskRepository {
    func getAll() async -> [Task] {
        []
    }
    func fetch(ids: [String]) async -> [Task] {
        []
    }
    func fetch(board: Board) async -> [Task] {
        []
    }
    func createTask(title: String, description: String?, board: Board) async -> Task {
        Task(id: UUID().uuidString, title: title, description: description, board: board)
    }
    func updateTask(id: String, title: String, description: String?, board: Board) async -> Task {
        Task(id: id, title: title, description: description, board: board)
    }
}

final class DependencyBag: Sendable {
    static func make() -> any TaskRepository {
        MockedTaskRepository()
    }
}

struct Task: Identifiable, Sendable, Codable, AppEntity {
    static let typeDisplayRepresentation = TypeDisplayRepresentation(name: "Task")
    
    var displayRepresentation: DisplayRepresentation {
        DisplayRepresentation(title: "A Task", subtitle: "A task created by the user in the general board")
    }
    
    @Property(title: "ID")
    var id: String
    @Property(title: "Title")
    var title: String
    @Property(title: "Description")
    var description: String?
    @Property(title: "Board")
    var board: Board
    
    static let defaultQuery: TaskQuery = TaskQuery()
    
    init(id: String, title: String, description: String? = nil, board: Board = .general) {
        self.id = id
        self.title = title
        self.description = description
        self.board = board
    }
}

extension Task: URLRepresentableEntity {
    static var urlRepresentation: URLRepresentation {
        "https://igorcferreira.dev/board/task/\(.id)"
    }
}

struct TaskQuery: EntityQuery {
    let repository: TaskRepository
    
    init(repository: TaskRepository) {
        self.repository = repository
    }
    init() {
        self.repository = DependencyBag.make()
    }
    
    func suggestedEntities() async throws -> [Task] {
        await repository.getAll()
    }
    
    func entities(for identifiers: [Task.ID]) async throws -> [Task] {
        await repository.fetch(ids: identifiers)
    }
}
