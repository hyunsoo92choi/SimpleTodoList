package com.hschoi.todo.common.response

import com.hschoi.todo.common.code.Errors

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
data class ApiErrorResponseDto(
    val errorsCode: Errors,
    val message: String?,
    val status: String
)