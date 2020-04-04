package com.hschoi.todo.app.services

import com.hschoi.todo.app.dto.request.TaskRequestDto
import com.hschoi.todo.app.entities.Task
import com.hschoi.todo.app.repository.TaskRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val modelMapper: ModelMapper
) {

    @Transactional
    fun registerTask(taskRequest: TaskRequestDto): Task {
        // 새로운 할 일을 등록
        val newTask = taskRepository.save(this.modelMapper.map(taskRequest, Task::class.java))

        return newTask
    }
}