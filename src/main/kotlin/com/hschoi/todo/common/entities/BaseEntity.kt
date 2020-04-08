package com.hschoi.todo.common.entities

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
@MappedSuperclass
abstract class BaseEntity {
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    val createdAt: LocalDateTime = LocalDateTime.now()

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    var updatedAt: LocalDateTime = LocalDateTime.now()
}