package com.luminopedia


data class Task(
    val id: Int,
    var title: String,
    var isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)