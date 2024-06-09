package com.example.taskwavepart1

import android.media.Image
import android.net.Uri
import com.example.loginfunction.User
import java.sql.Time
import java.util.Date

data class Timesheet (
    var description: String? = "",
    var category: String? = "",
    var image: String? = "",
    var date: String? = "",
    var startTime: String? = "",
    var user: String? = "",
    var endTime: String? = ""
)