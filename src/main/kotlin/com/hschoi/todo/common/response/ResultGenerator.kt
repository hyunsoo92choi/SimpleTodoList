package com.hschoi.todo.common.response

import com.hschoi.todo.common.code.Success

object ResultGenerator {
    fun genSuccessResult(): ResultBody {
        return ResultBody()
            .setCode(Success.NOCONTENTS.code)
            .setMessage(Success.NOCONTENTS.message)
    }

    fun genSuccessResult(data: Any): ResultBody {
        return ResultBody()
            .setCode(Success.SUCCESS.code)
            .setMessage(Success.SUCCESS.message)
            .setResult(data)
    }
}