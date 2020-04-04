package com.hschoi.todo.common.response

import com.hschoi.todo.common.code.Success
import org.springframework.http.HttpStatus
import java.io.Serializable

class ResultBody : Serializable {
    private var code = Success.SUCCESS.code
    private var message: String? = null
    private var result: Any? = null

    fun setCode(code: HttpStatus): ResultBody {
        this.code = code
        return this
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String): ResultBody {
        this.message = message
        return this
    }

    fun getResult(): Any? {
        return result
    }

    fun setResult(result: Any): ResultBody {
        this.result = result
        return this
    }

}