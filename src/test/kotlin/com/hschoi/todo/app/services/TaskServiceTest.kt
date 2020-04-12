package com.hschoi.todo.app.services

import com.hschoi.todo.app.dto.request.TaskRequestDto
import com.hschoi.todo.app.entities.SubTask
import com.hschoi.todo.app.entities.Task
import com.hschoi.todo.app.repository.SubTaskRepository
import com.hschoi.todo.app.repository.TaskRepository
import com.hschoi.todo.common.code.TaskStatus
import com.hschoi.todo.common.exception.BizException
import com.hschoi.todo.common.utils.PageRequest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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

    @Test
    fun `단일 Task_단건_조회_실패`() {
        Assertions.assertThrows(BizException::class.java, { taskService.findById(1L) })
    }

    @Test
    fun `단일 Task_전체_조회_성공`() {
        // init
        val id = 1L
        var size = 10
        var page = 1

        val createdTestTask1 = Task(
            id = 1L, title = "할일 조회",
            description = "할일 조회 테스트를 만든다", taskStatus = TaskStatus.TODO
        )

        val createdTestTask2 = Task(
            id = 2L, title = " 할일 전체 조회",
            description = "할일 전체 조회 테스트를 만든다", taskStatus = TaskStatus.TODO
        )
        val result: List<Task> = mutableListOf(createdTestTask1,createdTestTask2)
        val pageable: Pageable = PageRequest.of(page, size)
        var taskList: Page<Task> = PageImpl(result, pageable, 2L);

        //given
        given(taskRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")))).willReturn(taskList)

        //when
        val foundTask = taskService.findAll(page, size)

        //then
        then(foundTask.totalElements).isEqualTo(taskList.totalElements)
        then(foundTask.content.get(0).title).isEqualTo("할일 조회")
        then(foundTask.content.get(1).title).contains("할일 전체 조회")
    }

    @Test
    fun `단일 Task_등록_성공`() {
        val taskRequest: TaskRequestDto = TaskRequestDto(
            subTaskNo = null, title = "할일 등록",
            description = "할일 등록 테스트를 만든다", taskStatus = TaskStatus.TODO, complatedAt = null
        )
        val createdTestTask = Task(
            id = 1L, title = "할일 등록",
            description = "할일 등록 테스트를 만든다", taskStatus = TaskStatus.TODO
        )

        `when`(taskRepository.save(this.modelMapper.map(taskRequest, Task::class.java))).thenReturn(createdTestTask)
//
        val createdTask = taskService.registerTask(taskRequest)


        //then
        then(createdTask.id).isEqualTo(createdTestTask.id)
        then(createdTask.title).isEqualTo(createdTestTask.title)
        then(createdTask.description).isEqualTo(createdTestTask.description)
        then(createdTask.complatedAt).isNull()

    }

    @Test
    fun `하위 Task_등록_성공`() {
        val parentId = 1L
        // given
        val taskRequest: TaskRequestDto = TaskRequestDto(subTaskNo = longArrayOf(2,3) , title = "할일 등록",
            description = "할일 등록 테스트를 만든다",taskStatus = TaskStatus.TODO,complatedAt = null)

        val createdFirstSubTask = SubTask(id = 1L, parentTaskId = 1L, subTaskId = 1L)
        val createdSecondSubTask = SubTask(id = 2L, parentTaskId = 1L, subTaskId = 2L)

        var subTasks = mutableListOf<SubTask>()
        subTasks.add(createdFirstSubTask)
        subTasks.add(createdSecondSubTask)

        val createdTestTask = Task(
            id = parentId, title = "메인 할일 조회",
            description = "메인 할일 조회 테스트를 만든다", taskStatus = TaskStatus.TODO,
            subTask = subTasks
        )

        `when`(taskRepository.save(this.modelMapper.map(taskRequest, Task::class.java))).thenReturn(createdTestTask)

        val createdTask = taskService.registerTask(taskRequest)

        // then
        then(createdTask.id).isEqualTo(createdTestTask.id)
        then(createdTask.subTask!!.size).isEqualTo(subTasks.size)
        then(createdTask.subTask!!.get(0).subTaskId).isEqualTo(subTasks.get(0).subTaskId)
        then(createdTask.subTask!!.get(0).parentTaskId).isEqualTo(subTasks.get(0).parentTaskId)
        then(createdTask.subTask!!.get(1).subTaskId).isEqualTo(subTasks.get(1).subTaskId)
        then(createdTask.subTask!!.get(1).parentTaskId).isEqualTo(subTasks.get(1).parentTaskId)
    }

    @Test
    fun `Task_수정_성공`() {
        // init
        val id = 1L
        val taskRequest: TaskRequestDto = TaskRequestDto(subTaskNo = null , title = "할일 등록",
            description = "할일 등록 테스트를 만든다",taskStatus = TaskStatus.TODO,complatedAt = null)
        val createdTestTask = Task(
            id = id, title = "메인 할일 조회",
            description = "메인 할일 조회 테스트를 만든다", taskStatus = TaskStatus.TODO
        )
        //given
        given(taskRepository.findById(id)).willReturn(Optional.of(createdTestTask))

        //when
        val foundTask = taskService.findById(id)

        // update part
        val updatetitle = "메인 할일 업데이트"
        val updateStatus = TaskStatus.INPROGRESS
        val updateTask = Task(id= id, title = updatetitle,description = foundTask.description ,taskStatus = updateStatus)
        createdTestTask.title = updatetitle
        createdTestTask.taskStatus = updateStatus

        `when`(taskRepository.save(createdTestTask)).thenReturn(updateTask)

        val updated = taskService.update(createdTestTask)

        then(foundTask.id).isEqualTo(updated.id)
        then(updated.title).isEqualTo(updatetitle)
        then(updated.taskStatus).isEqualTo(TaskStatus.INPROGRESS)

    }

    @Test
    fun `Task_상태_수정_성공`() {
        // init
        val id = 1L
        val taskRequest: TaskRequestDto = TaskRequestDto(subTaskNo = null , title = "할일 등록",
            description = "할일 등록 테스트를 만든다",taskStatus = TaskStatus.TODO, complatedAt = null)
        val createdTestTask = Task(
            id = id, title = "메인 할일 조회",
            description = "메인 할일 조회 테스트를 만든다", taskStatus = TaskStatus.TODO
        )
        //given
        given(taskRepository.findById(id)).willReturn(Optional.of(createdTestTask))

        //when
        val foundTask = taskService.findById(id)

        // update part
        val updatetitle = "메인 할일 상태 업데이트"
        val updateStatus = TaskStatus.DONE
        val updateTask = Task(id= id, title = updatetitle,description = foundTask.description ,taskStatus = updateStatus)
        createdTestTask.title = updatetitle
        createdTestTask.taskStatus = updateStatus

        `when`(taskRepository.save(createdTestTask)).thenReturn(updateTask)

        val updated = taskService.update(createdTestTask)

        then(foundTask.id).isEqualTo(updated.id)
        then(updated.title).isEqualTo(updatetitle)
        then(updated.taskStatus).isEqualTo(TaskStatus.DONE)

    }

    @Test
    fun `하위 Task_상태_완료_아닐경우_상위_할일_완료_실패`() {
        var task2 = Task(2L, "빨래", taskStatus = TaskStatus.TODO)
        var subTask1 = SubTask(1L,parentTaskId = 1L, subTaskId = 2L)
        var task3 = Task(3L, "청소", taskStatus = TaskStatus.TODO)
        var subTask2 = SubTask(2L,parentTaskId = 1L, subTaskId = 3L)
        var task1 = Task(id= 1L, title = "집안일", taskStatus = TaskStatus.TODO, subTask = mutableListOf(subTask1,subTask2))

        `when`(taskRepository.findById(1L)).thenReturn(Optional.of(task1))
        `when`(taskRepository.findAllByIdIn(mutableListOf(2L,3L))).thenReturn(mutableListOf(task2,task3))
        `when`(subTaskRepository.findByParentTaskId(1L)).thenReturn(mutableListOf(subTask1,subTask2))

        //when
        val foundTask = taskService.findById(1L)

        Assertions.assertThrows(BizException::class.java, { taskService.checkSubTaskStatus(1L) })
    }

    @Test
    fun `하위 Task_상태_완료_아닐경우_상위_할일_완료_성공`() {
        var task2 = Task(2L, "빨래", taskStatus = TaskStatus.DONE)
        var subTask1 = SubTask(1L,parentTaskId = 1L, subTaskId = 2L)
        var task3 = Task(3L, "청소", taskStatus = TaskStatus.DONE)
        var subTask2 = SubTask(2L,parentTaskId = 1L, subTaskId = 3L)
        var task1 = Task(id= 1L, title = "집안일", taskStatus = TaskStatus.TODO, subTask = mutableListOf(subTask1,subTask2))

        `when`(taskRepository.findById(1L)).thenReturn(Optional.of(task1))
        `when`(taskRepository.findAllByIdIn(mutableListOf(2L,3L))).thenReturn(mutableListOf(task2,task3))
        `when`(subTaskRepository.findByParentTaskId(1L)).thenReturn(mutableListOf(subTask1,subTask2))

        //when
        val foundTask = taskService.findById(1L)

        val result = taskService.checkSubTaskStatus(1L)

        then(result).isTrue()
    }

}
