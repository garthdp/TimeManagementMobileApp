package com.example.taskwavepart1

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskwavepart1.graphTest.arrTimeJava
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

var arTimesheets = ArrayList<Timesheet>()
class timesheets : AppCompatActivity() {
    private lateinit var rootNode: FirebaseDatabase
    private lateinit var userReference : DatabaseReference
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
        rootNode = FirebaseDatabase.getInstance()
        userReference = rootNode.getReference("Timesheet")
        currentTimesheet = null
        val txtTsCatName : TextView = findViewById(R.id.txtTsCatName)
        val btnTsListBack : FloatingActionButton = findViewById(R.id.btnTsListBack)
        val btnTsListAdd : FloatingActionButton = findViewById(R.id.btnTsListAdd)
        val btnGraph : FloatingActionButton = findViewById(R.id.btnGraph)
        val btnFilterDate1 : Button = findViewById(R.id.btnFilterDate1)
        val btnFilterDate2 : Button = findViewById(R.id.btnFilterDate2)
        val txtTotalHours : TextView = findViewById(R.id.txtTotalHours)
        val btnFilter : Button = findViewById(R.id.btnFilter)
        val arrTimesheet = ArrayList<Timesheet>()
        var totalCategoryHours = 0
        var date1 = ""
        var date2 = ""
        val progressBar : ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        txtTsCatName.text = currentCategory?.name

        val feed : RecyclerView = findViewById(R.id.rcTimesheets)
        val timesheetAdapter = TimesheetAdpater()
        arTimesheets.clear()

        feed.apply {
            layoutManager = LinearLayoutManager(this@timesheets)
            adapter=timesheetAdapter
        }

        userReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                var totalHours = 0

                for (ds in snapshot.getChildren()) {
                    val user = ds.child("user").getValue<String>(String::class.java)
                    val category = ds.child("category").getValue<String>(String::class.java)
                    if (user == Firebase.auth.currentUser!!.uid && category == currentCategory!!.name){
                        val description = ds.child("description").getValue<String>(String::class.java)
                        val date = ds.child("date").getValue<String>(String::class.java)
                        val endTime = ds.child("endTime").getValue<String>(String::class.java)
                        val startTime = ds.child("startTime").getValue<String>(String::class.java)
                        val image = ds.child("image").getValue<String>(String::class.java)
                        Log.d("TAG", "$user / $description / $date / $category / $endTime / $startTime")
                        val cat = Timesheet(description, category, image, date, startTime, user, endTime)

                        arTimesheets.add(cat)
                    }
                }
                Handler(Looper.getMainLooper()).post{
                    timesheetAdapter.submitList(arTimesheets)
                }
                totalHours = calc(arTimesheets)
                txtTotalHours.text = "Total Hours: $totalHours"
                progressBar.visibility = View.INVISIBLE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                progressBar.visibility = View.INVISIBLE
            }
        })


        btnFilterDate1.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
                date1 = dateFormatter.format(Date(it)).toString()
                Toast.makeText(this, "$date1 is selected", Toast.LENGTH_LONG).show()
                btnFilterDate1.text = "Picked"
                btnFilterDate1.textSize = 20F
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
                btnFilterDate2.text = "Picked"
                btnFilterDate2.textSize = 20F
            }
        }

        btnFilter.setOnClickListener {
            val arrFilteredList = ArrayList<Timesheet>()
            totalCategoryHours = 0
            for (i in arTimesheets.indices ){
                if (arTimesheets[i].category == currentCategory?.name && Firebase.auth.currentUser!!.uid == arTimesheets[i].user
                    && arTimesheets[i].date.toString() >= date1 && arTimesheets[i].date.toString() <= date2)
                {
                    val startTimeTotal : Int = (arTimesheets[i].startTime!!.substring(0, 2).toInt() * 60) + arTimesheets[i].startTime!!.substring(3, 5).toInt()
                    val endTimeTotal : Int = (arTimesheets[i].endTime!!.substring(0, 2).toInt() * 60) + arTimesheets[i].endTime!!.substring(3, 5).toInt()
                    totalCategoryHours += endTimeTotal - startTimeTotal
                    arrFilteredList.add(arTimesheets[i])
                }
            }
            //loadList(arrFilteredList)
            Handler(Looper.getMainLooper()).post{
                timesheetAdapter.submitList(arrFilteredList)
            }
            totalCategoryHours /= 60
            txtTotalHours.text = "Total Hours: $totalCategoryHours"
        }

        btnTsListBack.setOnClickListener{
            val intent = Intent(this, categories::class.java)
            startActivity(intent)
        }
        btnTsListAdd.setOnClickListener{
            val intent = Intent(this, timesheet_entry::class.java)
            startActivity(intent)
        }
        btnGraph.setOnClickListener{
            arrTimeJava = ArrayList(arTimesheets)
            val intent = Intent(this, graphTest::class.java)
            startActivity(intent)
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
    fun calc(arrTimesheet: ArrayList<Timesheet>) : Int{
        var totalCategoryHours = 0
        for (i in arrTimesheet.indices ){
            if (arrTimesheet[i].category == currentCategory?.name && Firebase.auth.currentUser!!.uid == arrTimesheet[i].user)
            {
                val startTimeTotal : Int = (arrTimesheet[i].startTime!!.substring(0, 2).toInt() * 60) + arrTimesheet[i].startTime!!.substring(3, 5).toInt()
                val endTimeTotal : Int = (arrTimesheet[i].endTime!!.substring(0, 2).toInt() * 60) + arrTimesheet[i].endTime!!.substring(3, 5).toInt()
                totalCategoryHours += endTimeTotal - startTimeTotal
            }
        }
        totalCategoryHours /= 60
        return totalCategoryHours
    }
}