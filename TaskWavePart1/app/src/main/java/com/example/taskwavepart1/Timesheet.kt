package com.example.taskwavepart1

import android.media.Image
import android.net.Uri
import com.example.loginfunction.User
import java.sql.Time
import java.util.Date

data class Timesheet (
    val description: String,
    val category: Category,
    val image: Uri?,
    val date: String?,
    val startTime: String,
    val endTime: String
)