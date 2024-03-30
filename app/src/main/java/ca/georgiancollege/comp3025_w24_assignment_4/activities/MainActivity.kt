package ca.georgiancollege.comp3025_w24_assignment_4.activities

/**
 * Nicolas Vicente
 * 200539594
 * March 3rd, 2023
 * Main Activity file
 *To do list UI app. This is part one of the todo list application that uses firebase and and authentication.
 * this part is only the UI for the application with limited functionality, of which will be built upon in assignment 4
 * Version 1.00
 */

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.comp3025_w24_assignment_4.databinding.ActivityMainBinding
import android.content.Intent
import android.os.Build
import android.view.View
import ca.georgiancollege.comp3025_w24_assignment_4.adapters.RecyclerViewAdapter
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the navigation bar icons color to gray
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        // Recycler view setup
        val recyclerView = binding.FirstRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val todoList = listOf(
            TodoItem("Walk dog", "Take dog on a walk a round the block", "2024-02-23"),
            TodoItem("Finish homework"),
            TodoItem("Read Other book", "Read chapters 4 and 5 of book", "2024-03-23"),
            TodoItem("Do laundry", "Take clothes out of dryer and iron them"),
            TodoItem("Read book", "Read chapters 4 and 5 of book", "2024-05-23")
        )


        // Recycler view adapter setup with context parameter passed
        val adapter = RecyclerViewAdapter(todoList, this)
        recyclerView.adapter = adapter

        // Find the FloatingActionButton and set OnClickListener
        val fab = binding.addMovieFAB
        fab.setOnClickListener {
            // Start the CreateNewTodoActivity
            val intent = Intent(this, CreateNewTodoActivity::class.java)
            startActivity(intent)
        }


    }


}
