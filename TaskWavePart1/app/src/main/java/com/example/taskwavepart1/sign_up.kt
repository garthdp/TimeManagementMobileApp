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
import com.google.firebase.auth.FirebaseAuth

public class sign_up : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtSignUpUsername : EditText = findViewById(R.id.txtSignUpUsername)
        val txtSignUpPassword : EditText = findViewById(R.id.txtSignUpPassword)
        val txtSignUpCheckPassword : EditText = findViewById(R.id.txtSignUpCheckPassword)
        val btnSignSignup : Button = findViewById(R.id.btnSignSignup)
        val btnSignLogin : Button = findViewById(R.id.btnSignLogin)
        val progressBar : ProgressBar = findViewById(R.id.progressBar)
        auth = FirebaseAuth.getInstance()

        btnSignSignup.setOnClickListener{
            var isEmptyCheck = false
            var passMatchCheck = true
            var isLong = true
            progressBar.visibility = View.VISIBLE
            if (txtSignUpPassword.text.isEmpty()){
                txtSignUpPassword.error = "Fill in"
                Toast.makeText(
                    this,
                    "Fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.INVISIBLE
                isEmptyCheck = true
            }
            if (txtSignUpCheckPassword.text.isEmpty()){
                txtSignUpCheckPassword.error = "Fill in"
                Toast.makeText(
                    this,
                    "Fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.INVISIBLE
                isEmptyCheck = true
            }
            if (txtSignUpUsername.text.isEmpty()){
                txtSignUpUsername.error = "Fill in"
                Toast.makeText(
                    this,
                    "Fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.INVISIBLE
                isEmptyCheck = true
            }
            if (!isEmptyCheck && txtSignUpPassword.text.toString() != txtSignUpCheckPassword.text.toString() ){
                txtSignUpPassword.error = "Passwords do not match"
                txtSignUpCheckPassword.error = "Passwords do not match"
                Toast.makeText(
                    this,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.INVISIBLE
                passMatchCheck = false
            }
            if (passMatchCheck && txtSignUpPassword.length() < 6){
                txtSignUpPassword.error = "Password needs to be 6 characters long"
                txtSignUpCheckPassword.error = "Password needs to be 6 characters long"
                Toast.makeText(
                    this,
                    "Password needs to be 6 characters long",
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.INVISIBLE
                isLong = false
            }
            if (!isEmptyCheck && passMatchCheck && isLong){
                var email = txtSignUpUsername.text.toString()
                var password = txtSignUpPassword.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                "Account created",
                                Toast.LENGTH_SHORT,
                            ).show()
                            progressBar.visibility = View.INVISIBLE
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Sign in failed",
                                Toast.LENGTH_SHORT,
                            ).show()
                            progressBar.visibility = View.INVISIBLE
                        }
                    }
            }
            else{
                progressBar.visibility = View.INVISIBLE
            }
        }
        btnSignLogin.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}