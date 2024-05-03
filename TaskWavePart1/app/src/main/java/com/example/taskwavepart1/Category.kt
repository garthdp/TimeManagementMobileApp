package com.example.taskwavepart1

import com.example.loginfunction.User

data class Category (
    val name: String,
    val user: User?,
    val minHours: Int,
    val maxHours: Int
)
