package com.example.taskwavepart1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.concurrent.Executors

class categories : AppCompatActivity() {
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
        currentCategory = null
        val btnCateListBack : FloatingActionButton = findViewById(R.id.btnCateListBack)
        val btnCateListAdd : FloatingActionButton = findViewById(R.id.btnCateListAdd)

        btnCateListBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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
        Handler(Looper.getMainLooper()).post{
            categoryAdapter.submitList(arrCategories)
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