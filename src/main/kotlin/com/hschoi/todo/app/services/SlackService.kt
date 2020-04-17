package com.hschoi.todo.app.services

import com.google.gson.Gson
import com.hschoi.todo.common.utils.SlackMessage
import io.github.rybalkinsd.kohttp.dsl.async.httpPostAsync
import io.github.rybalkinsd.kohttp.ext.url
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/17
 */
@Service
class SlackService(
    val environment: Environment,
    @Value("\${slack.web-hook-url}")
    val slackWebHookUrl: String
) {
    fun send(slackMessage: SlackMessage) {
        client(slackMessage)
    }

    private fun client(slackMessage: SlackMessage) {
        httpPostAsync {
            url(slackWebHookUrl)
            body("application/json") {
                string(messageBuilder(slackMessage))
            }
        }
    }

    private fun messageBuilder(slackMessage: SlackMessage): String {
        val messages = arrayListOf(
            messageTextSection("*${slackMessage.title}* -> 서버 환경: ${environment.activeProfiles.last()}"),
            messageBodySection(slackMessage)
        )

        return Gson().toJson(
            mapOf(
                "blocks" to messages,
                "username" to "사나",
                "icon_url" to "https://media.vingle.net/images/ca_l/jb2wd4yl9l.jpg"
            )
        )
    }

    private fun messageBodySection(slackMessage: SlackMessage): Map<String, Any> {
        return mapOf(
            "type" to "section",
            "text" to mapOf(
                "type" to "mrkdwn",
                "text" to slackMessage.toString()
            ),
            "accessory" to mapOf(
                "type" to "image",
                "image_url" to "https://api.slack.com/img/blocks/bkb_template_images/approvalsNewDevice.png",
                "alt_text" to "icon"
            )
        )
    }

    private fun messageTextSection(message: String): Map<String, Any> {
        return mapOf(
            "type" to "section",
            "text" to mapOf(
                "type" to "mrkdwn",
                "text" to message
            )
        )
    }
}