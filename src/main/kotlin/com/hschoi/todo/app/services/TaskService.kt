package com.hschoi.todo.app.services

import com.hschoi.todo.app.dto.request.TaskRequestDto
import com.hschoi.todo.app.entities.SubTask
import com.hschoi.todo.app.entities.Task
import com.hschoi.todo.app.repository.SubTaskRepository
import com.hschoi.todo.app.repository.TaskRepository
import com.hschoi.todo.common.code.Errors
import com.hschoi.todo.common.code.TaskStatus
import com.hschoi.todo.common.exception.BizException
import com.hschoi.todo.common.utils.PageRequest
import org.apache.logging.log4j.LogManager
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
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
    companion object {
        private val log = LogManager.getLogger()
    }
    @Transactional
    fun registerTask(taskRequest: TaskRequestDto): Task {
        verifyNewTask(taskRequest)
        // 새로운 할 일을 등록
        val newTask = taskRepository.save(this.modelMapper.map(taskRequest, Task::class.java))
        // 생성한 신규 할일을 가지고 하위 할일 등록.
        addSubTasks(newTask.id!!, taskRequest.subTaskNo)

        return newTask
    }
    // 1번이 3번을 하위로 갖음, 3번이 다시 1번을 하위로 갖음.

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

    fun findAll(page: Int, size: Int): Page<Task> =
        taskRepository.findAll( PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")) )

    fun findById(id: Long): Task =
        taskRepository.findById(id).orElseThrow { BizException(Errors.NOT_FOUND, "존재하지 않는 할일 입니다.") }

    fun checkSubTaskStatus (id: Long): Boolean {
        var isAllSubTaskDone = false
        var foundParentsTask = subTaskRepository.findByParentTaskId(id)
        var subTaskIds = foundParentsTask.map { t-> t.subTaskId }

        // 부모 아이디로 조회 하였을 때 조회 결과가 없다면, 단일 할일.
        if (subTaskIds.isNullOrEmpty()) {
            //어떤 상태이든 업데이트 가능.
            isAllSubTaskDone = true
        } else {
            var subTasks = taskRepository.findAllByIdIn(subTaskIds)
            // 모든 하위 작업의 상태 코드가 DONE인 경우
            if( subTasks.stream().allMatch { t -> (TaskStatus.DONE.equals(t.taskStatus)) } == true )
                isAllSubTaskDone = true
            else
                throw BizException(Errors.FAIL, "완료되지 않은 하위 할일이 존재 합니다.")
        }

        return isAllSubTaskDone;
    }

    fun update(task: Task): Task = taskRepository.save(task)

    private fun verifyNewTask(taskRequest: TaskRequestDto) {
        var parentTaskIds = subTaskRepository.findAll().map { it.parentTaskId }

        for (parentTaskId in parentTaskIds) {
            taskRequest.subTaskNo!!.forEach {
                if (it == parentTaskId)
                    throw BizException(Errors.CONFLICT, "Task 간 순환 참조는 불가능 합니다.")
            }
        }
    }
}