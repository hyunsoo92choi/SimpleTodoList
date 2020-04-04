package com.hschoi.todo.common.code

import com.hschoi.todo.common.exception.BizException
import java.util.*

enum class TaskStatus(override val code: String, override val value: String): Code {
    TODO("TODO", "할일"),
    INPROGRESS("INPROGRESS", "진행중"),
    DONE("DONE", "완료")
    ;
    companion object {
        fun of(string: String): TaskStatus {
            return Arrays.stream(TaskStatus.values())
                .filter { exception -> exception.name.equals(string.toUpperCase()) }
                .findFirst()
                .orElseThrow {
                    BizException(
                        Errors.NOT_FOUND,
                        "존재하지 않는 상태 입니다."
                    )
                }
        }
    }
}