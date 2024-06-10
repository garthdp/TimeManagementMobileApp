package com.example.taskwavepart1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginfunction.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

var currentCategory : Category? = null
var currentTimesheet : Timesheet? = null
var userEmail : String? = null

class MainActivity : AppCompatActivity() {
    lateinit var rootNode: FirebaseDatabase
    lateinit var userReference : DatabaseReference
    lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        userEmail = auth.currentUser?.email.toString()
        if (currentUser != null) {
            val intent = Intent(this, Agenda::class.java)
            startActivity(intent)
        }
    }
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
        val progressBar : ProgressBar = findViewById(R.id.progressBar)
        auth = FirebaseAuth.getInstance()

        bLogin.setOnClickListener(){
            var isEmptyCheck = false
            progressBar.visibility = View.VISIBLE
            if (user.text.isEmpty()){
                user.error = "Fill in"
                progressBar.visibility = View.INVISIBLE
                isEmptyCheck = true
            }
            if (pass.text.isEmpty()){
                pass.error = "Fill in"
                progressBar.visibility = View.INVISIBLE
                isEmptyCheck = true
            }
            if (!isEmptyCheck) {
                val email = user.text.toString()
                val password = pass.text.toString()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            progressBar.visibility = View.INVISIBLE
                            val intent = Intent(this, Agenda::class.java)
                            startActivity(intent)
                        } else {
                            progressBar.visibility = View.INVISIBLE
                            Toast.makeText(
                                baseContext,
                                "Incorrect details",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }

        bSignup.setOnClickListener(){
            val intent = Intent(this, sign_up::class.java)
            startActivity(intent)
        }
    }
}