package com.hschoi.todo.app.controllers

import com.hschoi.todo.app.dto.request.TaskRequestDto
import com.hschoi.todo.app.services.TaskService
import com.hschoi.todo.common.response.ResultBody
import com.hschoi.todo.common.response.ResultGenerator
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
@RestController
@RequestMapping( "/api/{version}/tasks")
class TaskController(private val taskService: TaskService) {
    companion object {
        private val log = LogManager.getLogger()
    }

    @PostMapping()
    fun registerTask(@RequestBody dto: TaskRequestDto): ResponseEntity<ResultBody> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ResultGenerator.genSuccessResult())
    }
}