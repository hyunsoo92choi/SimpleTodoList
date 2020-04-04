package com.hschoi.todo.app.controllers

import com.hschoi.todo.common.response.ResultBody
import com.hschoi.todo.common.response.ResultGenerator
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
@RestController
class HealthController {
    @GetMapping("/health")
    fun healthy() : ResponseEntity<ResultBody> = ResponseEntity.ok(ResultGenerator.genSuccessResult())
}