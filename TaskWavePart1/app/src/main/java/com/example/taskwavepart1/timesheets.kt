package com.example.taskwavepart1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class timesheets : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SetTextI18n")
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
        val btnFilterDate1 : Button = findViewById(R.id.btnFilterDate1)
        val btnFilterDate2 : Button = findViewById(R.id.btnFilterDate2)
        val txtTotalHours : TextView = findViewById(R.id.txtTotalHours)
        val btnFilter : Button = findViewById(R.id.btnFilter)
        val arrTimesheet = ArrayList<Timesheet>()
        var totalCategoryHours = 0
        var date1 = ""
        var date2 = ""

        for (i in arrTimesheets.indices){
            if (arrTimesheets[i].category == currentCategory && currentUser == arrTimesheets[i].category.user){
                arrTimesheet.add(arrTimesheets[i])
            }
        }

        loadList(arrTimesheet)

        btnFilterDate1.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
                date1 = dateFormatter.format(Date(it)).toString()
                Toast.makeText(this, "$date1 is selected", Toast.LENGTH_LONG).show()
                btnFilterDate1.text = date1
                btnFilterDate1.textSize = 10F
            }
        }
        btnFilterDate2.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
                date2 = dateFormatter.format(Date(it)).toString()
                Toast.makeText(this, "$date2 is selected", Toast.LENGTH_LONG).show()
                btnFilterDate2.text = date2
                btnFilterDate2.textSize = 10F
            }
        }

        btnFilter.setOnClickListener {
            val arrFilteredList = ArrayList<Timesheet>()
            totalCategoryHours = 0
            for (i in arrTimesheets.indices ){
                if (arrTimesheets[i].category == currentCategory && currentUser == arrTimesheets[i].category.user
                    && arrTimesheets[i].date.toString() >= date1 && arrTimesheets[i].date.toString() <= date2)
                {
                    val startTimeTotal : Int = (arrTimesheets[i].startTime.substring(0, 2).toInt() * 60) + arrTimesheets[i].startTime.substring(3, 5).toInt()
                    val endTimeTotal : Int = (arrTimesheets[i].endTime.substring(0, 2).toInt() * 60) + arrTimesheets[i].endTime.substring(3, 5).toInt()
                    arrFilteredList.add(arrTimesheets[i])
                    totalCategoryHours += endTimeTotal - startTimeTotal

                    loadList(arrFilteredList)
                }
                else{
                    loadList(arrFilteredList)
                }
            }
            totalCategoryHours /= 60
            txtTotalHours.text = "Total Hours: $totalCategoryHours"
        }

        txtTsCatName.text = currentCategory?.name

        btnTsListBack.setOnClickListener{
            val intent = Intent(this, categories::class.java)
            startActivity(intent)
        }
        btnTsListAdd.setOnClickListener{
            val intent = Intent(this, timesheet_entry::class.java)
            startActivity(intent)
        }
    }
    private fun loadList(list: ArrayList<Timesheet>){
        val feed : RecyclerView = findViewById(R.id.rcTimesheets)
        val timesheetAdapter = TimesheetAdpater()
        feed.apply {
            layoutManager = LinearLayoutManager(this@timesheets)
            adapter=timesheetAdapter
        }
        Handler(Looper.getMainLooper()).post{
            timesheetAdapter.submitList(list)
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