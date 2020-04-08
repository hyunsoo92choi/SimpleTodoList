package com.hschoi.todo.app.controllers

import com.hschoi.todo.app.dto.request.TaskRequestDto
import com.hschoi.todo.app.dto.response.TaskResponse
import com.hschoi.todo.app.services.TaskService
import com.hschoi.todo.common.code.Errors
import com.hschoi.todo.common.code.TaskStatus
import com.hschoi.todo.common.exception.BizException
import com.hschoi.todo.common.response.ResultBody
import com.hschoi.todo.common.response.ResultGenerator
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.String
import java.net.URI
import java.time.LocalDateTime
import javax.validation.constraints.Max


/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
@Api(tags = ["할일"])
@RestController
@RequestMapping( "/api/{version}/tasks")
class TaskController(private val taskService: TaskService) {
    companion object {
        private val log = LogManager.getLogger()
    }

    @ApiOperation(value = "전체조회")
    @GetMapping
    fun tasks(@RequestParam page: Int, @RequestParam @Max(50) size: Int) = ResponseEntity
        .status(HttpStatus.OK)
        .body(ResultGenerator.genSuccessResult(taskService.findAll(page, size)))

    @ApiOperation(value = "단건 조회")
    @GetMapping("{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<ResultBody> = ResponseEntity
        .status(HttpStatus.OK)
        .body(ResultGenerator.genSuccessResult(taskService.findById(id)))

    @ApiOperation(value = "등록")
    @PostMapping()
    fun registerTask(@RequestBody taskRequest: TaskRequestDto): ResponseEntity<ResultBody> {
        val createdTask = taskService.registerTask(taskRequest)
        val url: URI = URI.create(String.format("/api/tasks/%d", createdTask.id))
        log.info("created task url : {}", url)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ResultGenerator.genSuccessResult(TaskResponse.of(createdTask)))
    }

    @ApiOperation("수정")
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody taskRequest: TaskRequestDto): ResponseEntity<ResultBody> {
        val foundTask = taskService.findById(id)
        // 상태 값 업데이트가 들어온다면 체크
        if (TaskStatus.DONE.equals(taskRequest.taskStatus)) {
            // 하위 할일들이 모두 완료인지 체크.
            val isItTrue = taskService.checkSubTaskStatus(foundTask.id!!)
            if (isItTrue) {
                foundTask.taskStatus = TaskStatus.DONE
                foundTask.complatedAt = LocalDateTime.now()
            }
        } else {
            //TODO: 하위 할일의 상태가 완료 -> 진행 중, 할일 상태로 변경 될 경우 상위 상태를 업데이트하는 하위 상태로 변경
            // todo start

            // todo end
            foundTask.taskStatus = taskRequest.taskStatus!!
            foundTask.complatedAt = null
        }

        foundTask.title = taskRequest.title ?: foundTask.title
        foundTask.description = taskRequest.description ?: foundTask.description

        val updatedTask = taskService.update(foundTask)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResultGenerator.genSuccessResult(TaskResponse.of(updatedTask)))
    }

    @ApiOperation("삭제")
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ResultBody> {
        // 하위 Task의 ID를 조회 함.
        var subTasks = taskService.getSubTasks(id)

        // 단일 할일은 그냥 삭제
        if (subTasks.isNullOrEmpty()) {
            taskService.remove(taskService.findById(id))
        } else {
            // 먼저 하위의 모든 일들을 삭제 후 자신의 할일 삭제.
            taskService.removeAllSubTask(subTasks)
            taskService.remove(taskService.findById(id))
        }

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(ResultGenerator.genSuccessResult())
    }
}