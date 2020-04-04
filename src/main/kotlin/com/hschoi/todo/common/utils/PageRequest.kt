package com.hschoi.todo.common.utils

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
class PageRequest {
    companion object {
        fun of(page: Int, size: Int): PageRequest {
            return of(page, size, Sort.unsorted())
        }

        fun of(page: Int, size: Int, sort: Sort): PageRequest {
            return PageRequest.of(page - 1, size, sort)
        }
    }
}