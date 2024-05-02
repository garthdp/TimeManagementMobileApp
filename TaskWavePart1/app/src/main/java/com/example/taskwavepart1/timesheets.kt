package com.example.taskwavepart1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class timesheets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_timesheets)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        currentTimesheet = null
        val txtTsCatName : TextView = findViewById(R.id.txtTsCatName)
        val btnTsListBack : FloatingActionButton = findViewById(R.id.btnTsListBack)
        val btnTsListAdd : FloatingActionButton = findViewById(R.id.btnTsListAdd)

        txtTsCatName.text = currentCategory?.name

        btnTsListBack.setOnClickListener{
            val intent = Intent(this, categories::class.java)
            startActivity(intent)
        }
        btnTsListAdd.setOnClickListener{
            val intent = Intent(this, timesheet_entry::class.java)
            startActivity(intent)
        }
        val feed : RecyclerView = findViewById(R.id.rcTimesheets)
        val timesheetAdapter = TimesheetAdpater()
        var catTimesheets = ArrayList<Timesheet>()

        for (i in arrTimesheets.indices){
            if (arrTimesheets[i].category == currentCategory){
                catTimesheets.add(arrTimesheets[i])
            }
        }
        feed.apply {
            layoutManager = LinearLayoutManager(this@timesheets)
            adapter=timesheetAdapter
        }
        Handler(Looper.getMainLooper()).post{
            timesheetAdapter.submitList(catTimesheets)
        }

        /*
        Code attribution
        Title = "How to Apply OnClickListener to RecyclerView Items in Android?"
        Website link = https://www.geeksforgeeks.org/how-to-apply-onclicklistener-to-recyclerview-items-in-android/
        Author = GeeksForGeeks
        Usage = Used to add a on click listener to recycleview items
        */
        timesheetAdapter.setOnClickListener(object : TimesheetAdpater.OnClickListener{
            override fun onClick(position: Int, model: Timesheet) {
                currentTimesheet = model
                val intent = Intent(this@timesheets, timesheet_details::class.java)
                startActivity(intent)
            }
        })
    }
}