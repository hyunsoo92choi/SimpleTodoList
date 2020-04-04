package com.hschoi.todo.common.code

import org.springframework.http.HttpStatus

enum class Errors(override val code: HttpStatus, override val value: String): HttpStatusCode {
    FAIL(HttpStatus.BAD_REQUEST, "실패"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "패스워드가 올바르지 않습니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않습니다."),
    CONFLICT(HttpStatus.CONFLICT,"이미 존재 합니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN,"접근 권한이 없습니다");
}