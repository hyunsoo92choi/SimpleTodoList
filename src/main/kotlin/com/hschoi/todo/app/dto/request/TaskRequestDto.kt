package com.hschoi.todo.app.dto.request

import com.hschoi.todo.common.code.TaskStatus
import java.time.LocalDateTime
import java.util.*


/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
data class TaskRequestDto(
    val subTaskNo: LongArray?,
    val title: String?, val description: String?,
    val taskStatus: TaskStatus? = TaskStatus.TODO, val complatedAt: LocalDateTime?
)