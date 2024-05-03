package com.example.taskwavepart1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
        var check = true

        btnCateBack.setOnClickListener{
            val intent = Intent(this, categories::class.java)
            startActivity(intent)
        }

        btnAddCategory.setOnClickListener{
            for(i in arrCategories.indices){
                if (txtCategoryName.text.toString() == arrCategories[i].name){
                    check = false
                }
            }
            if(txtCategoryName.text.isNotEmpty() && txtMinHours.text.isNotEmpty() && txtMaxHours.text.isNotEmpty())
            {
                if(check && txtMaxHours.text.toString().toInt() <= 12 && txtMinHours.text.toString().toInt() < txtMaxHours.text.toString().toInt())
                {
                    val category = Category(txtCategoryName.text.toString(), currentUser, txtMinHours.text.toString().toInt(), txtMaxHours.text.toString().toInt())
                    arrCategories.add(category)
                    val intent = Intent(this, categories::class.java)
                    startActivity(intent)
                }
                else
                {
                    if (!check){
                        txtCategoryName.error = "Name already in use"
                    }
                    if (txtMaxHours.text.toString().toInt() > 12){
                        txtMaxHours.error = "Can't be bigger than 12hrs"
                    }
                    if (txtMinHours.text.toString().toInt() >= txtMaxHours.text.toString().toInt()){
                        txtMinHours.error = "Min can't be bigger than max"
                    }
                    if (txtMinHours.text.toString().toInt() == txtMaxHours.text.toString().toInt()){
                        txtMinHours.error = "Min can't be equal to max"
                    }
                }
            }
            else{
                if (txtCategoryName.text.isEmpty()){
                    txtCategoryName.error = "Needs to be filled"
                }
                if (txtMaxHours.text.isEmpty()){
                    txtMaxHours.error = "Needs to be filled"
                }
                if (txtMinHours.text.isEmpty()){
                    txtMinHours.error = "Needs to be filled"
                }
            }
        }
    }
}