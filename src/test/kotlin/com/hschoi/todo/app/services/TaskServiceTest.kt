package com.hschoi.todo.app.services

import com.hschoi.todo.app.entities.SubTask
import com.hschoi.todo.app.entities.Task
import com.hschoi.todo.app.repository.SubTaskRepository
import com.hschoi.todo.app.repository.TaskRepository
import com.hschoi.todo.common.code.TaskStatus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.assertj.core.api.BDDAssertions.then
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.modelmapper.ModelMapper
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class TaskServiceTest {

    @InjectMocks
    private lateinit var taskService: TaskService

    @Mock
    private lateinit var taskRepository: TaskRepository

    @Mock
    private lateinit var subTaskRepository: SubTaskRepository

    @Mock
    private lateinit var modelMapper: ModelMapper

    @Test
    fun `단일 Task_단건_조회_성공`() {
        // init
        val id = 1L
        val createdTestTask = Task(
            id = id, title = "메인 할일 조회",
            description = "메인 할일 조회 테스트를 만든다", taskStatus = TaskStatus.TODO
        )

        //given
        given(taskRepository.findById(id)).willReturn(Optional.of(createdTestTask))

        //when
        val foundTask = taskService.findById(id)

        //then
        then(foundTask.id).isEqualTo(id)
        then(foundTask.title).isEqualTo("메인 할일 조회")
        then(foundTask.description).isEqualTo("메인 할일 조회 테스트를 만든다")
        then(foundTask.taskStatus).isEqualTo(TaskStatus.TODO)
    }

    @Test
    fun `하위_Task_있을 경우_단건_조회_성공`() {
        // init
        val id = 1L
        val createdFirstSubTask = SubTask(id = 1L, parentTaskId = 1L, subTaskId = 1L)
        val createdSecondSubTask = SubTask(id = 2L, parentTaskId = 1L, subTaskId = 2L)

        var subTasks = mutableListOf<SubTask>()
        subTasks.add(createdFirstSubTask)
        subTasks.add(createdSecondSubTask)

        val createdTestTask = Task(
            id = id, title = "메인 할일 조회",
            description = "메인 할일 조회 테스트를 만든다", taskStatus = TaskStatus.TODO,
            subTask = subTasks
        )

        //given
        given(taskRepository.findById(id)).willReturn(Optional.of(createdTestTask))

        //when
        val foundTask = taskService.findById(id)

        //then
        then(foundTask.id).isEqualTo(id)
        then(foundTask.title).isEqualTo("메인 할일 조회")
        then(foundTask.description).isEqualTo("메인 할일 조회 테스트를 만든다")
        then(foundTask.taskStatus).isEqualTo(TaskStatus.TODO)

        // 메인 할일 아래 하위 할일 정보 일치 확인
        then(foundTask.subTask!!.size).isEqualTo(subTasks.size)
        then(foundTask.subTask!!.get(0).parentTaskId).isEqualTo(subTasks.get(0).parentTaskId)
        then(foundTask.subTask!!.get(0).subTaskId).isEqualTo(subTasks.get(0).subTaskId)

    }

}
