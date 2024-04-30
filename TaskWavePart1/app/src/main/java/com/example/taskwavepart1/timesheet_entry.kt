package com.example.taskwavepart1

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class timesheet_entry : AppCompatActivity() {


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

        val REQUEST_PICK_IMAGE = 1000

        val btnPickImage : Button = findViewById(R.id.btnPickImage)
        var imageView : ImageView = findViewById(R.id.imageView)

        btnPickImage.setOnClickListener{
            val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
            startActivityForResult(intent, 1)
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var imageView : ImageView = findViewById(R.id.imageView)
        if (resultCode === RESULT_OK) {
            if (requestCode === 1) {
                val selectedImageUri: Uri = data?.data!!
                if (null != selectedImageUri) {
                    imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }
}