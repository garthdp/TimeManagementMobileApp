package com.example.taskwavepart1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

var arrCategories = ArrayList<Category>()
var arrTimesheets = ArrayList<Timesheet>()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnChange : Button = findViewById(R.id.btnChange)
        val textView2 : TextView = findViewById(R.id.textView2)
        val btnCateAdd : Button = findViewById(R.id.btnCateAdd)
        var count = ""

        if(arrTimesheets.isNotEmpty()){
            count = arrTimesheets[0].description + arrTimesheets[0].date + arrTimesheets[0].startTime + arrTimesheets[0].image
        }

        textView2.text = count

        btnChange.setOnClickListener{
            val intent = Intent(this, timesheet_entry::class.java)
            startActivity(intent)
        }
        btnCateAdd.setOnClickListener{
            val intent = Intent(this, category_entry::class.java)
            startActivity(intent)
        }
    }
}