package com.hschoi.todo.app.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.hschoi.todo.common.entities.BaseEntity
import javax.persistence.*

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/05
 */
@Entity
@Table(name = "sub_tasks")
class SubTask(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_id")
    var id: Long? = null,
    /**
     * 상위 할일 번호
     */
    @Column(name = "parent_task_id")
    var parentTaskId: Long,

    /**
     * 참조한 할일 번호
     */
    @Column(name = "sub_task_id")
    var subTaskId: Long,

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "parent_task_id", insertable = false, updatable = false)
    var taskJoinInfo: Task? = null


): BaseEntity() {
}