package ca.georgiancollege.comp3025_w24_assignment_4.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.comp3025_w24_assignment_4.adapters.RecyclerViewAdapter
import ca.georgiancollege.comp3025_w24_assignment_4.databinding.ActivityMainBinding
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem
import ca.georgiancollege.comp3025_w24_assignment_4.viewmodels.TodoItemViewModel
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val todoItemViewModel: TodoItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        val recyclerView = binding.FirstRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observe todo list changes
        todoItemViewModel.getAllTodoItems(
            onSuccess = { todoList ->
                // Update RecyclerViewAdapter with the new list of todos
                val adapter = RecyclerViewAdapter(todoList, this)
                recyclerView.adapter = adapter
            },
            onFailure = { exception ->
                // Handle failure if needed
            }
        )

        // Set up FloatingActionButton OnClickListener
        val fab = binding.addMovieFAB
        fab.setOnClickListener {
            // Start the CreateNewTodoActivity
            // Add your implementation
        }
    }
}
