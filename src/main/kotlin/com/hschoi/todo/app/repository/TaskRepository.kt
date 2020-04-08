package com.hschoi.todo.app.repository

import com.hschoi.todo.app.entities.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository: JpaRepository<Task, Long> {
    fun findAllByIdIn(ids: List<Long>): List<Task>
}