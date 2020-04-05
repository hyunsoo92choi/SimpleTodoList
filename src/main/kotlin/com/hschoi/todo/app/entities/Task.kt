package com.hschoi.todo.app.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.hschoi.todo.common.code.TaskStatus
import com.hschoi.todo.common.entities.BaseEntity
import java.time.LocalDateTime
import javax.persistence.*


/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
@Entity
@Table(name = "tasks")
class Task(
    @Column(name = "task_id")
    override var id: Long? = null,
    //제목
    var title: String,
    // 내용
    var description: String? = null,
    // 상태
    @Enumerated(EnumType.STRING)
    var taskStatus: TaskStatus = TaskStatus.TODO,
    // 완료일자
    var complatedAt: LocalDateTime? = null,

    @JsonManagedReference
    @OneToMany(mappedBy = "taskJoinInfo", fetch = FetchType.EAGER, cascade = arrayOf(CascadeType.ALL))
    @OrderBy("sub_task_id DESC")
    var subTask: List<SubTask>? = null

): BaseEntity() {
    fun wasCompleted(): Boolean {
        return complatedAt != null
    }


}

