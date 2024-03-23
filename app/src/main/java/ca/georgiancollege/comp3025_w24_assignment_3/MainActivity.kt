package ca.georgiancollege.comp3025_w24_assignment_3

/**
 * Nicolas Vicente
 * 200539594
 * March 3rd, 2023
 * Main Activity file
 *To do list UI app. This is part one of the todo list application that uses firebase and and authentication.
 * this part is only the UI for the application with limited functionality, of which will be built upon in assignment 4
 */

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.comp3025_w24_assignment_3.databinding.ActivityMainBinding
import ca.georgiancollege.comp3025_w24_assignment_3.TodoItem
import android.content.Intent
import android.os.Build
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
            TodoItem("Title 1", "Description 1", "2024-02-23"),
            TodoItem("Title 2"),
            TodoItem("Title 3", "Description 3"),
            TodoItem("Title 4", "Description 4", "2024-05-23")
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
