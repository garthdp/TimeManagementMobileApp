package com.example.taskwavepart1

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date


val arrCatNames = ArrayList<String?>()

class timesheet_entry : AppCompatActivity() {
    private lateinit var rootNode: FirebaseDatabase
    private lateinit var userReference : DatabaseReference
    var imageUri : Uri? = null
    var pickedDate : String? = null
    @SuppressLint("MissingInflatedId")
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
        rootNode = FirebaseDatabase.getInstance()
        userReference = rootNode.getReference("Categories")
        val btnPickImage : FloatingActionButton = findViewById(R.id.btnPickImage)
        val btnPickDateTimesheet : Button = findViewById(R.id.btnPickDateTimesheet)
        val txtDescription : EditText = findViewById(R.id.txtDescription)
        val txtStartTime : EditText = findViewById(R.id.txtStartTime)
        val txtEndTime : EditText = findViewById(R.id.txtEndTime)
        val btnAddTimesheet : Button = findViewById(R.id.btnAddTimesheet)
        val txtSelectedDate : TextView = findViewById(R.id.txtSelectedDate)

        /*
        Code attribution
        Title = "Spinner in Kotlin"
        Website link = https://www.geeksforgeeks.org/spinner-in-kotlin/
        Author = GeeksForGeeks
        Usage = used to see how to populate spinner
        */
        val spinner : Spinner = findViewById(R.id.spinner)
        val adapter = ArrayAdapter(this, R.layout.spinner_list, arrCatNames)
        spinner.adapter = adapter

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
        btnPickImage.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
            startActivityForResult(intent, 1)
        }
        val bottomBar = findViewById<BottomNavigationView>(R.id.NavBar)

        bottomBar.setOnItemSelectedListener{
            when(it.itemId)
            {
                R.id.ic_home->{
                    val intent = Intent(this, Agenda::class.java)
                    startActivity(intent)
                }
                R.id.ic_category->{
                    val intent = Intent(this, categories::class.java)
                    startActivity(intent)
                }
                R.id.ic_timesheet->{
                    val intent = Intent(this, timesheet_entry::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        btnAddTimesheet.setOnClickListener{

            val spinnerItem = spinner.selectedItem as String
            var emptyTest = false
            var checkFormat = true

            if(pickedDate == null){
                Toast.makeText(this, "Date required", Toast.LENGTH_LONG).show()
                emptyTest = true
            }
            if (txtDescription.text.isEmpty()){
                txtDescription.error = "Needs to be filled"
                emptyTest = true
            }
            if (txtEndTime.text.isEmpty()){
                txtEndTime.error = "Needs to be filled"
                emptyTest = true
            }
            if (txtStartTime.text.isEmpty()){
                txtStartTime.error = "Needs to be filled"
                emptyTest = true
            }
            if(txtEndTime.text.length != 5 && txtEndTime.text.isNotEmpty()){
                txtEndTime.error = "Use format HH:mm"
                checkFormat = false
            }
            if(txtStartTime.text.length != 5 && txtStartTime.text.isNotEmpty()){
                txtStartTime.error = "Use format HH:mm"
                checkFormat = false
            }
            if (!txtStartTime.text.contains(":") && txtStartTime.text.isNotEmpty()){
                txtStartTime.error = "Use format HH:mm"
                checkFormat = false
            }
            if (!txtEndTime.text.contains(":") && txtEndTime.text.isNotEmpty()){
                txtEndTime.error = "Use format HH:mm"
                checkFormat = false
            }
            if (!emptyTest && checkFormat){

                val startTimeTotal : Int = (txtStartTime.text.toString().substring(0, 2).toInt() * 60) + txtStartTime.text.toString().substring(3, 5).toInt()
                val endTimeTotal : Int = (txtEndTime.text.toString().substring(0, 2).toInt() * 60) + txtEndTime.text.toString().substring(3, 5).toInt()
                if(startTimeTotal > endTimeTotal){
                    txtEndTime.error = "End time can't be before start time"
                    checkFormat = false
                }
                if(txtStartTime.text.toString().substring(0, 2).toInt() >= 24){
                    txtStartTime.error = "Hours can't be more than 23"
                    checkFormat = false
                }
                if(txtStartTime.text.toString().substring(3, 5).toInt() > 59){
                    txtStartTime.error = "Minutes can't be more than 59"
                    checkFormat = false
                }
                if(txtEndTime.text.toString().substring(0, 2).toInt() >= 24){
                    txtEndTime.error = "Hours can't be more than 23"
                    checkFormat = false
                }
                if(txtEndTime.text.toString().substring(3, 5).toInt() > 59){
                    txtEndTime.error = "Minutes can't be more than 59"
                    checkFormat = false
                }

                if (checkFormat) {
                    val timesheet = Timesheet(
                        txtDescription.text.toString(),
                        spinnerItem,
                        imageUri.toString(),
                        pickedDate,
                        txtStartTime.text.toString(),
                        Firebase.auth.currentUser!!.uid,
                        txtEndTime.text.toString()
                    )

                    add(timesheet)

                    val intent = Intent(this, Agenda::class.java)
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
    fun add(timesheet: Timesheet){
        val addReference = FirebaseDatabase.getInstance().getReference("Timesheet")
        addReference.child(timesheet.description!!).setValue(timesheet)
    }
}