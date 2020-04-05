package com.hschoi.todo.app.services

import com.hschoi.todo.app.dto.request.TaskRequestDto
import com.hschoi.todo.app.entities.SubTask
import com.hschoi.todo.app.entities.Task
import com.hschoi.todo.app.repository.SubTaskRepository
import com.hschoi.todo.app.repository.TaskRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val subTaskRepository: SubTaskRepository,
    private val modelMapper: ModelMapper
) {

    @Transactional
    fun registerTask(taskRequest: TaskRequestDto): Task {
        // 새로운 할 일을 등록
        val newTask = taskRepository.save(this.modelMapper.map(taskRequest, Task::class.java))
        // 생성한 신규 할일을 가지고 하위 할일 등록.
        addSubTasks(newTask.id!!, taskRequest.subTaskNo)

        return newTask
    }

    private fun addSubTasks(taskId: Long, subTaskIds: LongArray?) {
        // 하위 할일이 있을 경우
        if (subTaskIds != null && subTaskIds.size > 0) {
            var subTasks: MutableList<SubTask> = ArrayList<SubTask>()

            for (subTaskId in subTaskIds) {
                // 부모의 아이디는 조금전 생성한 신규 할일의 ID,
                // subTaskId는 파라미터로 받아온 하위 할일 ID인 하위 할일 객체를 생성
                var subTask: SubTask = SubTask(parentTaskId = taskId, subTaskId = subTaskId)
                //하위 할일들의 List에 담음
                subTasks.add(subTask)
            }
            // 하위 할일들의 List를 저장.
            subTaskRepository.saveAll(subTasks)
        }
    }

}