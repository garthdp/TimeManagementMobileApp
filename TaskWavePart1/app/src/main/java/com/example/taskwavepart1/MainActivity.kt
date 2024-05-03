package com.example.taskwavepart1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginfunction.User

var arrCategories = ArrayList<Category>()
var arrTimesheets = ArrayList<Timesheet>()
var currentCategory : Category? = null
var currentTimesheet : Timesheet? = null
var arUsers = ArrayList<User>()
var currentUser : User? = null

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bLogin : Button = findViewById(R.id.btnLogin);
        val bSignup : Button = findViewById(R.id.btnSignup);
        val user : EditText = findViewById(R.id.edtUsername)
        val pass : EditText = findViewById(R.id.edtPassword)
        var login = false

        bLogin.setOnClickListener(){
            for (i in arUsers.indices)
            {
                if ((arUsers[i].Username == user.text.toString()) && arUsers[i].Password == pass.text.toString())
                {
                    login = true
                    Toast.makeText(
                        this,
                        "User verified",
                        Toast.LENGTH_SHORT
                    ).show()
                    currentUser = arUsers[i]
                    val intent = Intent(this, categories::class.java)
                    startActivity(intent)
                }
            }
            if (pass.text.isEmpty() || user.text.isEmpty()){
                Toast.makeText(
                    this,
                    "Fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (!login){
                Toast.makeText(
                    this,
                    "Imposter",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (user.text.isEmpty()){
                user.error = "Fill in"
            }
            if (pass.text.isEmpty()){
                pass.error = "Fill in"
            }
        }

        bSignup.setOnClickListener(){
            val intent = Intent(this, sign_up::class.java)
            startActivity(intent)
        }
    }
}