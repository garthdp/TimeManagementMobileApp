package com.example.taskwavepart1

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date

class timesheet_entry : AppCompatActivity() {

    var imageUri : Uri? = null

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_timesheet_entry)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnPickImage : Button = findViewById(R.id.btnPickImage)
        val btnPickDateTimesheet : Button = findViewById(R.id.btnPickDateTimesheet)

        /*
        Code attribution
        Title = "Material Design Date Picker in Android using Kotlin"
        Website link = https://www.geeksforgeeks.org/material-design-date-picker-in-android-using-kotlin/
        Author = GeeksForGeeks
        Usage = Used for a date picker
        */
        btnPickDateTimesheet.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormatter.format(Date(it))
                Toast.makeText(this, "$date is selected", Toast.LENGTH_LONG).show()

            }

            btnPickImage.setOnClickListener {
                val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
                startActivityForResult(intent, 1)
            }
        }
    }
    /*
    Code attribution
    Title = "Photo Picker in Android 13 with Example Project"
    Website link = https://www.geeksforgeeks.org/photo-picker-in-android-13-with-example-project/
    Author = GeeksForGeeks
    Usage = function which takes image from user.
    */
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var imageView : ImageView = findViewById(R.id.imageView)
        if (resultCode === RESULT_OK) {
            if (requestCode === 1) {
                val selectedImageUri: Uri = data?.data!!
                if (null != selectedImageUri) {
                    imageView.setImageURI(selectedImageUri)
                    imageUri = selectedImageUri
                }
            }
        }
    }
}