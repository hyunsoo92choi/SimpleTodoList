package com.hschoi.todo.common.utils

import com.hschoi.todo.common.code.Status
import java.net.URLEncoder

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/17
 */

class SlackMessage(
    var title: String,
    var path: Any,
    var status: Status,
    var message: String?,
    var params: String?
) {
    constructor(
        title: String, path: Any, status: Status, message: String
    ): this(title, path, status, message, null)

    override fun toString(): String {
        return "${asPath()}\n" +
                "${asMessage()}\n" +
                "${asStatus()}\n" +
                asParams()
    }

    private fun asPath(): String {
        return "*경로*\n " + path::class.simpleName
    }

    private fun asMessage(): String {
        return "*메시지*\n $message ${if (isError()) "@channel" else "" }"
    }

    private fun asStatus(): String {
        return "*상태*\n ${toErrorName()}"
    }

    private fun toErrorName(): String {
        return if (isError()) "*`${status.name}`*" else status.name
    }

    private fun asParams(): String {
        if(params.isNullOrEmpty()) return ""

        return "*Parameters*\n ```$params```"
    }

    private fun isError(): Boolean {
        return status == Status.ERROR
    }
}