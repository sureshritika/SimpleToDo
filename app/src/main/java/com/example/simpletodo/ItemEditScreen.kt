package com.example.simpletodo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class ItemEditScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_edit_screen)

        setTitle("Edit Item")

        val clickedText = intent.getStringExtra("clickedText")
        Log.i("Caren" , "clickedText: " + clickedText)

        val updatedEdit = findViewById<EditText>(R.id.id_updatedEdit)
        val saveButton = findViewById<Button>(R.id.id_saveButton)

        updatedEdit.setText(clickedText)

        saveButton.setOnClickListener {
            // 1. Grab the updated text has inputting into @id/updatedEdit
            val updatedText = updatedEdit.text.toString()

            // 2. Return to main activity
            val intent = Intent()
            intent.putExtra("updatedText" , updatedText)
            setResult(RESULT_OK , intent)
            finish()
        }


    }
}