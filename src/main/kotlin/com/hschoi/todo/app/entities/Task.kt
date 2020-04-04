package com.hschoi.todo.app.entities

import com.hschoi.todo.common.code.TaskStatus
import com.hschoi.todo.common.entities.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
@Entity
@Table(name = "tasks")
class Task(
    override var id: Long? = null,
    //제목
    var title: String,
    // 내용
    var description: String? = null,
    // 상태
    @Enumerated(EnumType.STRING)
    var taskStatus: TaskStatus = TaskStatus.TODO,
    // 완료일자
    var complatedAt: LocalDateTime? = null

) : BaseEntity()