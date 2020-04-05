package com.hschoi.todo.app.repository

import com.hschoi.todo.app.entities.SubTask
import com.hschoi.todo.app.entities.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubTaskRepository: JpaRepository<SubTask, Long> {
}