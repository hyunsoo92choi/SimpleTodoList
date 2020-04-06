package com.hschoi.todo.app.controllers

import com.hschoi.todo.app.dto.request.TaskRequestDto
import com.hschoi.todo.app.dto.response.TaskResponse
import com.hschoi.todo.app.services.TaskService
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
    fun users(@RequestParam page: Int, @RequestParam @Max(50) size: Int) = ResponseEntity
        .status(HttpStatus.OK)
        .body(ResultGenerator.genSuccessResult(taskService.findAll(page, size)))

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
}