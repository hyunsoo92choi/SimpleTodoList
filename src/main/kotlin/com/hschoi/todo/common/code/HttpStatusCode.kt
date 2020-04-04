package com.hschoi.todo.common.code

import org.springframework.http.HttpStatus

interface HttpStatusCode {
    val code: HttpStatus

    val value: String
}