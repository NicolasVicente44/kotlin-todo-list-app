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
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.comp3025_w24_assignment_3.databinding.ActivityMainBinding
import ca.georgiancollege.comp3025_w24_assignment_3.TodoItem


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //view binding setup
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //recycler view set up
        val recyclerView = binding.FirstRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val todoList = listOf(
            TodoItem("Title 1", "Description 1", "Due Date 1"),
            TodoItem("Title 2", "Description 2", "Due Date 2"),
            TodoItem("Title 3", "Description 3", "Due Date 3")
        )


        //recycler view adapter setup
        val adapter = RecyclerViewAdapter(todoList)
        recyclerView.adapter = adapter
    }
}
