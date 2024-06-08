package com.example.taskwavepart1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.Executors

class categories : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
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
        auth = FirebaseAuth.getInstance()
        currentCategory = null
        val btnCateListBack : FloatingActionButton = findViewById(R.id.btnCateListBack)
        val btnCateListAdd : FloatingActionButton = findViewById(R.id.btnCateListAdd)
        auth = FirebaseAuth.getInstance()
        rootNode = FirebaseDatabase.getInstance()

        btnCateListBack.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btnCateListAdd.setOnClickListener{
            userReference = rootNode.getReference("users")
            userReference.child("Test").setValue("Hello, World!")
            val intent = Intent(this, category_entry::class.java)
            startActivity(intent)
        }

        val feed : RecyclerView = findViewById(R.id.rcCategories)
        val categoryAdapter = CategoryAdapter()

        feed.apply {
            layoutManager = LinearLayoutManager(this@categories)
            adapter=categoryAdapter
        }

        val arrUserCategories = ArrayList<Category>()
        for(i in arrCategories.indices){
            if (arrCategories[i].user == currentUser){
                arrUserCategories.add(arrCategories[i])
            }
        }

        Handler(Looper.getMainLooper()).post{
            categoryAdapter.submitList(arrUserCategories)
        }

        /*
        Code attribution
        Title = "How to Apply OnClickListener to RecyclerView Items in Android?"
        Website link = https://www.geeksforgeeks.org/how-to-apply-onclicklistener-to-recyclerview-items-in-android/
        Author = GeeksForGeeks
        Usage = Used to add a on click listener to recycleview items
        */
        categoryAdapter.setOnClickListener(object : CategoryAdapter.OnClickListener{
            override fun onClick(position: Int, model: Category) {
                currentCategory = model
                val intent = Intent(this@categories, timesheets::class.java)
                startActivity(intent)
            }
        })
    }
}