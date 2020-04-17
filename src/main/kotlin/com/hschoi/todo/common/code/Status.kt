package com.hschoi.todo.common.code

enum class Status(override val code: String, override val value: String): Code {
    SUCCESS("SUCCESS", "성공"),
    ERROR("ERROR", "에러"),
}