package com.hschoi.todo.common.code

import org.springframework.http.HttpStatus

enum class Success(var code: HttpStatus, var message: String) {
    SUCCESS(HttpStatus.OK, "성공"),
    NOCONTENTS(HttpStatus.NO_CONTENT, "성공")
}