package com.example.taskwavepart1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Agenda : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agenda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val listview1 : ListView = findViewById(R.id.rcGoals)
        val listview2 : ListView = findViewById(R.id.rcCompleted)
        val textView10 = findViewById<TextView>(R.id.textView10)
        val textView11 = findViewById<TextView>(R.id.textView11)
        val btnLoad = findViewById<Button>(R.id.btnLoad)
        arrCatNames.clear()
        val bottomBar = findViewById<BottomNavigationView>(R.id.NavBar)
        val btnSignOut = findViewById<FloatingActionButton>(R.id.btnSignOut)

        btnSignOut.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        bottomBar.setOnItemSelectedListener{
            when(it.itemId)
            {
                R.id.ic_home->{
                    val intent = Intent(this, Agenda::class.java)
                    startActivity(intent)
                }
                R.id.ic_category->{
                    val intent = Intent(this, categories::class.java)
                    startActivity(intent)
                }
                R.id.ic_timesheet->{
                    val intent = Intent(this, timesheet_entry::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        val rootNode = FirebaseDatabase.getInstance()
        val userReference = rootNode.getReference("Categories")
        val arrCategories = ArrayList<Category>()
        userReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (ds in snapshot.getChildren()) {
                    val user = ds.child("user").getValue<String>(String::class.java)
                    if (user == Firebase.auth.currentUser!!.uid){
                        val name = ds.child("name").getValue<String>(String::class.java)
                        val min = ds.child("minHours").getValue<Long>(Long::class.java)
                        val max = ds.child("maxHours").getValue<Long>(Long::class.java)
                        val cat : Category = Category(name, min!!.toInt(), user, max!!.toInt())
                        arrCategories.add(cat)
                        arrCatNames.add(cat.name)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }) // end of category arraymaking

        val timesheetsRef = rootNode.getReference("Timesheet")
        val arrTimesheets = ArrayList<Timesheet>()
        timesheetsRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                var totalHours = 0

                for(i in arrCategories.indices){
                    for (ds in snapshot.getChildren()) {
                        val user = ds.child("user").getValue<String>(String::class.java)
                        val category = ds.child("category").getValue<String>(String::class.java)
                        if (user == Firebase.auth.currentUser!!.uid && category == arrCategories[i].name){
                            val description = ds.child("description").getValue<String>(String::class.java)
                            val date = ds.child("date").getValue<String>(String::class.java)
                            val endTime = ds.child("endTime").getValue<String>(String::class.java)
                            val startTime = ds.child("startTime").getValue<String>(String::class.java)
                            val image = ds.child("image").getValue<String>(String::class.java)
                            val cat = Timesheet(description.toString(), category, image, date, startTime, user, endTime)
                            arrTimesheets.add(cat)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        }) // end of timesheet arraymaking

        btnLoad.setOnClickListener{
            listview1.visibility = View.VISIBLE
            listview2.visibility = View.VISIBLE
            textView10.visibility = View.VISIBLE
            textView11.visibility = View.VISIBLE
            val arrNotCompletedCategories = ArrayList<String?>()
            val arrCompletedCategories = ArrayList<String?>()
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) //date format method
            val currentDate = Date()
            val today = sdf.format(currentDate)
            for (c in arrCategories.indices){
                var totalCategoryHours = 0
                for (i in arrTimesheets.indices ) {
                    if (arrTimesheets[i].category == arrCategories[c].name && Firebase.auth.currentUser!!.uid == arrTimesheets[i].user
                        && arrTimesheets[i].date.toString() == today
                    ) {
                        val startTimeTotal: Int = (arrTimesheets[i].startTime!!.substring(0, 2)
                            .toInt() * 60) + arrTimesheets[i].startTime!!.substring(3, 5).toInt()
                        val endTimeTotal: Int = (arrTimesheets[i].endTime!!.substring(0, 2)
                            .toInt() * 60) + arrTimesheets[i].endTime!!.substring(3, 5).toInt()
                        totalCategoryHours += endTimeTotal - startTimeTotal
                    }

                }
                if (totalCategoryHours < arrCategories[c].minHours!!){
                    arrNotCompletedCategories.add(arrCategories[c].name)
                }
                if (totalCategoryHours >= arrCategories[c].minHours!!){
                    arrCompletedCategories.add(arrCategories[c].name)
                }

            }
            val adapter1 = ArrayAdapter<String>(this, R.layout.listitem, arrNotCompletedCategories)
            val adapter2 = ArrayAdapter<String>(this, R.layout.listitem, arrCompletedCategories)

            listview1.adapter = adapter1
            listview2.adapter = adapter2
        }
    }
}