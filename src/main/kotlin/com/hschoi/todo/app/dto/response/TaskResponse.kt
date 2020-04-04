package com.hschoi.todo.app.dto.response

import com.hschoi.todo.app.entities.Task
import com.hschoi.todo.common.code.TaskStatus
import java.time.LocalDateTime

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
data class TaskResponse(
    val title: String, val description: String?,
    val taskStatus: TaskStatus?, val complatedAt: LocalDateTime?
) {
    companion object {
        fun of(task: Task): TaskResponse {
            return TaskResponse(
                title = task.title,
                description = task.description,
                taskStatus = task.taskStatus,
                complatedAt = task.complatedAt
            )
        }
    }
}