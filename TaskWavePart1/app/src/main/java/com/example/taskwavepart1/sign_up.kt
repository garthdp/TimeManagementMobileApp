package com.example.taskwavepart1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginfunction.User

class sign_up : AppCompatActivity() {
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
        var checkUsers = false

        btnSignSignup.setOnClickListener{
            for(i in arUsers.indices){
                if(txtSignUpUsername.text.toString() == arUsers[i].Username){
                    checkUsers = true
                }
            }
            if (txtSignUpPassword.text.toString() == txtSignUpCheckPassword.text.toString()
                && txtSignUpPassword.text.isNotEmpty() && txtSignUpUsername.text.isNotEmpty() && txtSignUpCheckPassword.text.isNotEmpty()
                && !checkUsers){
                arUsers.add(User(txtSignUpUsername.text.toString(),txtSignUpPassword.text.toString()))
                Toast.makeText(
                    this,
                    "User created",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            else{
                if (txtSignUpPassword.text.toString() != txtSignUpCheckPassword.text.toString() ){
                    txtSignUpPassword.error = "Passwords do not match"
                    txtSignUpCheckPassword.error = "Passwords do not match"
                    Toast.makeText(
                        this,
                        "Passwords do not match",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (checkUsers){
                    txtSignUpUsername.error = "Username taken"
                    Toast.makeText(
                        this,
                        "Username taken",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (txtSignUpPassword.text.isEmpty()){
                    txtSignUpPassword.error = "Fill in"
                    Toast.makeText(
                        this,
                        "Fill in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (txtSignUpCheckPassword.text.isEmpty()){
                    txtSignUpCheckPassword.error = "Fill in"
                    Toast.makeText(
                        this,
                        "Fill in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (txtSignUpUsername.text.isEmpty()){
                    txtSignUpUsername.error = "Fill in"
                    Toast.makeText(
                        this,
                        "Fill in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        btnSignLogin.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}