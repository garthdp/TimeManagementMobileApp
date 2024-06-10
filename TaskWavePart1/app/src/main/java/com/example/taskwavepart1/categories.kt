package com.example.taskwavepart1

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskwavepart1.graphTest.max
import com.example.taskwavepart1.graphTest.min
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class categories : AppCompatActivity() {
    private lateinit var rootNode: FirebaseDatabase
    private lateinit var userReference : DatabaseReference
// ...
// Initialize Firebase Auth
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_categories)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        rootNode = FirebaseDatabase.getInstance()
        userReference = rootNode.getReference("Categories")
        currentCategory = null
        val btnCateListAdd : FloatingActionButton = findViewById(R.id.btnCateListAdd)
        val progressBar : ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        min = 0
        max = 0
        val categories = ArrayList<Category>()
        val bottomBar = findViewById<BottomNavigationView>(R.id.NavBar)
        bottomBar.menu.findItem(R.id.ic_category).setChecked(true)

        bottomBar.setOnItemSelectedListener{
            when(it.itemId)
            {
                R.id.ic_home->{
                    val intent = Intent(this, Agenda::class.java)
                    startActivity(intent)
                }
                R.id.ic_category->{
                    val intent = Intent(this, com.example.taskwavepart1.categories::class.java)
                    startActivity(intent)
                }
                R.id.ic_timesheet->{
                    val intent = Intent(this, timesheet_entry::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        btnCateListAdd.setOnClickListener{
            val intent = Intent(this, category_entry::class.java)
            startActivity(intent)
        }


        val feed : RecyclerView = findViewById(R.id.rcCategories)
        val categoryAdapter = CategoryAdapter()

        feed.apply {
            layoutManager = LinearLayoutManager(this@categories)
            adapter=categoryAdapter
        }


        // Read from the database
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
                        Log.d("TAG", "$user / $name / $min / $max")
                        val cat : Category = Category(name, min!!.toInt(), user, max!!.toInt())
                        categories.add(cat)
                    }
                }
                Log.d("Tag", "${categories.toString()}")
                Handler(Looper.getMainLooper()).post{
                    categoryAdapter.submitList(categories)
                }
                progressBar.visibility = View.INVISIBLE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
                progressBar.visibility = View.INVISIBLE
            }
        })

        /*
        Code attribution
        Title = "How to Apply OnClickListener to RecyclerView Items in Android?"
        Website link = https://www.geeksforgeeks.org/how-to-apply-onclicklistener-to-recyclerview-items-in-android/
        Author = GeeksForGeeks
        Usage = Used to add a on click listener to recycleview items
        */

        categoryAdapter.setOnClickListener(object : CategoryAdapter.OnClickListener{
            override fun onClick(position: Int, model: Category) {
                arrCatNames.clear()
                for (cat in categories){
                    arrCatNames.add(cat.name)
                }
                currentCategory = model
                min = model.minHours!!
                max = model.maxHours!!
                val intent = Intent(this@categories, timesheets::class.java)
                startActivity(intent)
            }
        })
    }
}