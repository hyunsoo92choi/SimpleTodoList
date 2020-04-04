package com.hschoi.todo.common.config

import com.hschoi.todo.common.BizException
import com.hschoi.todo.common.response.ApiErrorResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
@RestControllerAdvice
class ApiResponseHandler: ResponseEntityExceptionHandler() {
    @ExceptionHandler
    fun exceptionHandler(ex: BizException) =
        ResponseEntity(ApiErrorResponseDto(ex.resultCode, ex.exactMessage, ex.resultCode.code.value().toString()), ex.resultCode.code)
}