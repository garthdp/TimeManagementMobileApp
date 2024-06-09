package com.example.taskwavepart1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase

class category_entry : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_entry)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtCategoryName : TextView = findViewById(R.id.txtCategoryName)
        val txtMinHours : TextView = findViewById(R.id.txtMinHours)
        val txtMaxHours : TextView = findViewById(R.id.txtMaxHours)
        val btnAddCategory : Button = findViewById(R.id.btnAddCategory)
        val btnCateBack : FloatingActionButton = findViewById(R.id.btnCateBack)

        btnCateBack.setOnClickListener{
            val intent = Intent(this, categories::class.java)
            startActivity(intent)
        }

        btnAddCategory.setOnClickListener{
            var error = false
            var isEmpty = false

            if (txtCategoryName.text.isEmpty()){
                txtCategoryName.error = "Needs to be filled"
                isEmpty = true
            }
            if (txtMaxHours.text.isEmpty()){
                txtMaxHours.error = "Needs to be filled"
                isEmpty = true
            }
            if (txtMinHours.text.isEmpty()){
                txtMinHours.error = "Needs to be filled"
                isEmpty = true
            }
            if (!isEmpty && txtMaxHours.text.toString().toInt() > 12){
                txtMaxHours.error = "Can't be bigger than 12hrs"
                error = true
            }
            if (!isEmpty && txtMinHours.text.toString().toInt() >= txtMaxHours.text.toString().toInt()){
                txtMinHours.error = "Min can't be bigger than max"
                error = true
            }
            if (!isEmpty && !isEmpty && txtMinHours.text.toString().toInt() == txtMaxHours.text.toString().toInt()){
                txtMinHours.error = "Min can't be equal to max"
                error = true
            }
            if (!error && !isEmpty){
                add(txtCategoryName.text.toString(), txtMinHours.text.toString().toInt(), txtMaxHours.text.toString().toInt())
                val intent = Intent(this, categories::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(
                    baseContext,
                    "Failed",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
    fun add(name: String, min: Int, max: Int){
        val addReference = FirebaseDatabase.getInstance().getReference("Categories")
        val category : Category = Category(name, min, Firebase.auth.currentUser!!.uid, max)
        addReference.child(category.name!!).setValue(category)
    }
}