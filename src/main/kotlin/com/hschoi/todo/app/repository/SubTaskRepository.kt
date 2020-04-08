package com.hschoi.todo.app.repository

import com.hschoi.todo.app.entities.SubTask
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SubTaskRepository: JpaRepository<SubTask, Long> {
    fun findByParentTaskId(todoId: Long?): MutableList<SubTask>//mutableList<SubTask>
    fun findBySubTaskId(subTaskId: Long):List<SubTask>
}