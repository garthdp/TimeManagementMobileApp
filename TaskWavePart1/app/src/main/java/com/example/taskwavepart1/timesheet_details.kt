package com.example.taskwavepart1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class timesheet_details : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_timesheet_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtTsdCatName : TextView = findViewById(R.id.txtTsdCatName)
        val txtTsdTime : TextView = findViewById(R.id.txtTsdTime)
        val txtTsdDate : TextView = findViewById(R.id.txtTsdDate)
        val txtTimesheetDescription : TextView = findViewById(R.id.txtTimesheetDescription)
        val btnTsdBack : FloatingActionButton = findViewById(R.id.btnTsdBack)
        val imgTsDetail : ImageView = findViewById(R.id.imgTsDetail)

        txtTsdDate.text = currentTimesheet?.date
        txtTsdCatName.text = currentTimesheet?.category
        txtTsdTime.text = currentTimesheet?.startTime + " - " + currentTimesheet?.endTime
        imgTsDetail.setImageURI(currentTimesheet?.image!!.toUri())
        txtTimesheetDescription.text = currentTimesheet?.description

        btnTsdBack.setOnClickListener{
            val intent = Intent(this, timesheets::class.java)
            startActivity(intent)
        }
    }
}