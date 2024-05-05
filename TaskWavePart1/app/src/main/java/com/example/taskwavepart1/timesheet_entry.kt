package com.example.taskwavepart1

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.TimeFormat
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date

class timesheet_entry : AppCompatActivity() {

    var imageUri : Uri? = null
    var pickedDate : String? = null
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
        val btnPickImage : FloatingActionButton = findViewById(R.id.btnPickImage)
        val btnPickDateTimesheet : Button = findViewById(R.id.btnPickDateTimesheet)
        val txtDescription : EditText = findViewById(R.id.txtDescription)
        val txtStartTime : EditText = findViewById(R.id.txtStartTime)
        val txtEndTime : EditText = findViewById(R.id.txtEndTime)
        val spinner : Spinner = findViewById(R.id.spinner)
        val btnAddTimesheet : Button = findViewById(R.id.btnAddTimesheet)
        val txtSelectedDate : TextView = findViewById(R.id.txtSelectedDate)
        val btnBack : FloatingActionButton = findViewById(R.id.btnBack)
        val arrCatNames = ArrayList<String>()

        arrCatNames.clear()
        for(i in arrCategories.indices){
            if (arrCategories[i].user == currentUser){
                arrCatNames.add(arrCategories[i].name)
            }
        }
        if (spinner != null){
            /*
            Code attribution
            Title = "Spinner in Kotlin"
            Website link = https://www.geeksforgeeks.org/spinner-in-kotlin/
            Author = GeeksForGeeks
            Usage = used to see how to populate spinner
            */
            val adapter = ArrayAdapter(this, R.layout.spinner_list, arrCatNames)
            spinner.adapter = adapter
        }
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

            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
                val date = dateFormatter.format(Date(it)).toString()
                Toast.makeText(this, "$date is selected", Toast.LENGTH_LONG).show()
                pickedDate = date
                txtSelectedDate.text = "Date: " + date
            }
        }
        btnBack.setOnClickListener{
            val intent = Intent(this, timesheets::class.java)
            startActivity(intent)
        }
        btnPickImage.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
            startActivityForResult(intent, 1)
        }
        btnAddTimesheet.setOnClickListener{
            val spinnerItem = spinner.selectedItem as String
            if (txtDescription.text.toString().isNotEmpty() && pickedDate != null
                && txtStartTime.text.toString().isNotEmpty() && txtEndTime.text.toString().isNotEmpty()
                && txtStartTime.text.length == 5 && txtEndTime.text.length == 5 && txtEndTime.text.contains(":") && txtStartTime.text.contains(":")){

                val startTimeTotal : Int = (txtStartTime.text.toString().substring(0, 2).toInt() * 60) + txtStartTime.text.toString().substring(3, 5).toInt()
                val endTimeTotal : Int = (txtEndTime.text.toString().substring(0, 2).toInt() * 60) + txtEndTime.text.toString().substring(3, 5).toInt()

                if (txtStartTime.text.toString().substring(0, 2).toInt() < 24 && txtStartTime.text.toString().substring(3, 5).toInt() < 60
                    && txtEndTime.text.toString().substring(0, 2).toInt() < 24 && txtEndTime.text.toString().substring(3, 5).toInt() < 60
                    && startTimeTotal < endTimeTotal) {

                    for (i in arrCategories.indices) {

                        if (spinnerItem == arrCategories[i].name) {

                            val selectedCategory: Category = arrCategories[i]
                            val timesheet = Timesheet(
                                txtDescription.text.toString(),
                                selectedCategory,
                                imageUri,
                                pickedDate,
                                txtStartTime.text.toString(),
                                txtEndTime.text.toString()
                            )

                            arrTimesheets.add(timesheet)
                            val intent = Intent(this, timesheets::class.java)
                            startActivity(intent)
                        }
                    }
                }
                else{
                    if(txtStartTime.text.toString().substring(0, 2).toInt() >= 24){
                        txtStartTime.error = "Hours can't be more than 23"
                    }
                    if(txtStartTime.text.toString().substring(3, 5).toInt() > 59){
                        txtStartTime.error = "Minutes can't be more than 59"
                    }
                    if(txtEndTime.text.toString().substring(0, 2).toInt() >= 24){
                        txtEndTime.error = "Hours can't be more than 23"
                    }
                    if(txtEndTime.text.toString().substring(3, 5).toInt() > 59){
                        txtEndTime.error = "Minutes can't be more than 59"
                    }
                    if(startTimeTotal > endTimeTotal){
                        txtEndTime.error = "End time can't be before start time"
                    }
                }
            }
            else{
                if(pickedDate == null){
                    Toast.makeText(this, "Date required", Toast.LENGTH_LONG).show()
                }
                if (txtDescription.text.isEmpty()){
                    txtDescription.error = "Needs to be filled"
                }
                if (txtEndTime.text.isEmpty()){
                    txtEndTime.error = "Needs to be filled"
                }
                if(txtEndTime.text.length != 5 && txtEndTime.text.isNotEmpty()){
                    txtEndTime.error = "Use format HH:mm"
                }
                if (txtStartTime.text.isEmpty()){
                    txtStartTime.error = "Needs to be filled"
                }
                if(txtStartTime.text.length != 5 && txtStartTime.text.isNotEmpty()){
                    txtStartTime.error = "Use format HH:mm"
                }
                if (!txtStartTime.text.contains(":") && txtStartTime.text.isNotEmpty()){
                    txtStartTime.error = "Use format HH:mm"
                }
                if (!txtEndTime.text.contains(":") && txtEndTime.text.isNotEmpty()){
                    txtEndTime.error = "Use format HH:mm"
                }
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