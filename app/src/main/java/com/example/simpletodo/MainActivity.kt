package com.example.simpletodo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    var listOfStars = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter
    var pos = 0
    val activity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult? ->
        if (result?.resultCode == Activity.RESULT_OK) {
            val updatedText = result?.data?.getStringExtra("updatedText").toString()
            Log.i("Caren" , "result: " + updatedText + " @ pos: " + pos)
            listOfTasks.set(pos , updatedText)
            adapter.notifyItemChanged(pos)
            saveItems()
        }
        else {
            Log.i("Caren" , "fail result")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // 1. Remove the item from the list
                listOfTasks.removeAt(position)
                listOfStars.removeAt(position)
                // 2. Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        val onShortClickListener = object : TaskItemAdapter.OnShortClickListener {
            override fun onItemShortClicked(position: Int) {
                // 1. Bring up an edit screen for todo item
                val intent = Intent(this@MainActivity , ItemEditScreen::class.java)
                pos = position
                intent.putExtra("clickedText" , listOfTasks.get(position))
                activity.launch(intent)
            }
        }

        val onStarClickListener = object : TaskItemAdapter.OnStarClickListener {
            override fun onStarClicked(position: Int , isStarChecked: Boolean) {
                Log.i("Caren" , "pos: " + position + "; isStarChecked: " + isStarChecked)
                listOfStars.set(position , isStarChecked.toString())
                saveItems()
            }
        }

        loadItems()

        // 1. Let's detect when the user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            // Code in here is going to be executed when the user click on a button
//            Log.i("Caren" , "User clicked on button")
//        }

        // Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks , listOfStars , onLongClickListener , onShortClickListener , onStarClickListener)
        // Attach the adapter to the recylcerview to populate items
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the button and input field, so that the user can enter a task and add it to the list

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Get a reference to the button
        // and then set an onclicklistener
        findViewById<Button>(R.id.button).setOnClickListener {
            // 1. Grab the text the user has inputted into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            // 2. Add the string to out list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)
            listOfStars.add("false")

            // Notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // 3. Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // Save the data that the user has inputted
    // Save data by writing and reading from a file

    // Get the file we need
    fun getDataFile() : File {
        // Every line is going to represent a specific task in our list of tasks
        return File(filesDir , "data.txt")
    }
    fun getStarDataFile() : File {
        // Every line is going to represent a specific task in our list of tasks
        return File(filesDir , "stardata.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile() , Charset.defaultCharset())
            listOfStars = org.apache.commons.io.FileUtils.readLines(getStarDataFile() , Charset.defaultCharset())

            Log.i("Caren" , "tasks: " + listOfTasks)
            Log.i("Caren" , "stars: " + listOfStars)
        } catch (ioException : IOException) {
            ioException.printStackTrace()
        }
    }

    // Save items by writing them into our data file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile() , listOfTasks)
            org.apache.commons.io.FileUtils.writeLines(getStarDataFile() , listOfStars)
        } catch (ioException : IOException) {
            ioException.printStackTrace()
        }
    }
}