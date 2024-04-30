package com.example.taskwavepart1

import android.media.Image
import android.net.Uri
import java.sql.Time
import java.util.Date

data class Timesheet (
    val name: String,
    val description: String,
    val category: Category,
    val image: Uri,
    val date: Date,
    val startTime: Time,
    val endTime: Time
)