package com.hschoi.todo.app.controllers

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.annotations.ApiIgnore

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/06
 */
@Controller
@ApiIgnore
class IndexController(
    @Value("\${index.page}") val page: String?
) {
    @RequestMapping("/", method = [RequestMethod.GET])
    fun index(): String {
        return "redirect:${getIndexPage()}"
    }

    private fun getIndexPage(): String {
        return page ?: "/swagger-ui.html"
    }
}
